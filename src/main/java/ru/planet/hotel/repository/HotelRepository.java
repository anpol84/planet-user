package ru.planet.hotel.repository;


import jakarta.annotation.Nullable;
import ru.planet.hotel.dto.*;
import ru.tinkoff.kora.common.Mapping;
import ru.tinkoff.kora.database.common.UpdateCount;
import ru.tinkoff.kora.database.common.annotation.Query;
import ru.tinkoff.kora.database.common.annotation.Repository;
import ru.tinkoff.kora.database.jdbc.JdbcRepository;
import ru.tinkoff.kora.database.jdbc.mapper.result.JdbcRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JdbcRepository {


    @Query("""
               INSERT INTO hotel (name, city, stars, image_url, additions, positions, min_price, max_price)
               VALUES (:createHotel.name, :createHotel.city, :createHotel.stars, :createHotel.imageUrl,
                       string_to_array(trim(both '[]' from :additions), ', ')::addition[],
                       string_to_array(trim(both '[]' from :positions), ', ')::"position"[],
                       :minPrice, :maxPrice)
               RETURNING id
        """)
    Long saveHotel(CreateHotel createHotel, String additions, String positions, double minPrice, double maxPrice);

    @Query("""
            UPDATE hotel SET name = :hotel.name, city = :hotel.city, stars = :hotel.stars,
            image_url = :hotel.imageUrl, min_price = :hotel.minPrice, max_price = :hotel.maxPrice,
            additions =  string_to_array(trim(both '[]' from :additions), ', ')::addition[],
            positions =  string_to_array(trim(both '[]' from :positions), ', ')::"position"[]
            WHERE id = :hotel.id
            """)
    UpdateCount updateHotel(GetHotel hotel, String additions, String positions);

    @Query("""
               INSERT INTO room_view (room_view_type, price)
               VALUES (CAST(:type AS room_view_type), :price)
               RETURNING id
            """)
    Long saveRoomView(String type, double price);

    @Query("""
               INSERT INTO room_type (room_type_type, price)
               VALUES (CAST(:type AS room_type_type), :price)
               RETURNING id
            """)
    Long saveRoomType(String type, double price);

    @Query("""
               INSERT INTO room_people (room_people_type, price)
               VALUES (CAST(:type AS room_people_type), :price)
               RETURNING id
            """)
    Long saveRoomPeople(String type, double price);

    @Query("""
               SELECT id FROM room_view WHERE room_view_type = CAST(:type AS room_view_type) AND price = :price
            """)
    Optional<Long> getRoomView(String type, double price);

    @Query("""
               SELECT id FROM room_type WHERE room_type_type = CAST(:type AS room_type_type) AND price = :price
            """)
    Optional<Long> getRoomType(String type, double price);

    @Query("""
               SELECT id FROM room_people WHERE room_people_type = CAST(:type AS room_people_type) AND price = :price
            """)
    Optional<Long> getRoomPeople(String type, double price);

    @Query("""
            INSERT INTO hotel_room_view (hotel_id, room_view_id)
            VALUES (:hotelId, :roomViewId)
            """)
    void saveHotelWithRoomView(Long hotelId, Long roomViewId);

    @Query("""
            INSERT INTO hotel_room_type (hotel_id, room_type_id)
            VALUES (:hotelId, :roomTypeId)
            """)
    void saveHotelWithRoomType(Long hotelId, Long roomTypeId);

    @Query("""
            INSERT INTO hotel_room_people (hotel_id, room_people_id)
            VALUES (:hotelId, :roomPeopleId)
            """)
    void saveHotelWithRoomPeople(Long hotelId, Long roomPeopleId);

    @Query("""
            SELECT * FROM hotel WHERE id = :id
            """)
    @Mapping(GetHotelRowMapper.class)
    @Nullable
    GetHotel getHotel(Long id);

    @Query("""
            SELECT * FROM hotel
            """)
    @Mapping(GetHotelRowMapper.class)
    List<GetHotel> getHotels();

    @Query("""
            SELECT * FROM hotel
            WHERE city = :city AND min_price <= :minPrice
            """)
    @Mapping(GetHotelRowMapper.class)
    @Nullable
    List<GetHotel> getHotelsWithFilter(String city, double minPrice);

    @Query("""
            SELECT rv.id, rv.room_view_type, rv.price FROM hotel_room_view hv JOIN room_view rv ON hv.room_view_id = rv.id
            WHERE hv.hotel_id = :hotelId
            """)
    @Mapping(RoomViewRowMapper.class)
    List<RoomView> getHotelsView(Long hotelId);

    @Query("""
            SELECT rt.id, rt.room_type_type, rt.price FROM hotel_room_type ht JOIN room_type rt ON ht.room_type_id = rt.id
            WHERE ht.hotel_id = :hotelId
            """)
    @Mapping(RoomTypeRowMapper.class)
    List<RoomType> getHotelType(Long hotelId);

    @Query("""
            SELECT rp.id, rp.room_people_type, rp.price FROM hotel_room_people hp JOIN room_people rp ON hp.room_people_id = rp.id
            WHERE hp.hotel_id = :hotelId
            """)
    @Mapping(RoomPeopleRowMapper.class)
    List<RoomPeople> getHotelPeople(Long hotelId);

    @Query("""
            DELETE FROM hotel_room_view WHERE hotel_id = :id
            """)
    void deleteHotelViews(Long id);

    @Query("""
            DELETE FROM hotel_room_type WHERE hotel_id = :id
            """)
    void deleteHotelTypes(Long id);

    @Query("""
            DELETE FROM hotel_room_people WHERE hotel_id = :id
            """)
    void deleteHotelPeople(Long id);

    @Query("""
            DELETE FROM room_view WHERE id NOT IN (SELECT room_view_id FROM hotel_room_view)
            """)
    void deleteUnusedRoomView();

    @Query("""
            DELETE FROM room_type WHERE id NOT IN (SELECT room_type_id FROM hotel_room_type)
            """)
    void deleteUnusedRoomType();

    @Query("""
            DELETE FROM room_people WHERE id NOT IN (SELECT room_people_id FROM hotel_room_people)
            """)
    void deleteUnusedRoomPeople();

    @Query("""
            DELETE from hotel WHERE id = :id
            """)
    UpdateCount deleteHotel(Long id);

    final class GetHotelRowMapper implements JdbcRowMapper<GetHotel> {

        @Override
        public GetHotel apply(ResultSet rs) throws SQLException {
            var additionsArray = (String[]) rs.getArray("additions").getArray();
            List<Addition> additions = new ArrayList<>();
            for (var str : additionsArray) {
                additions.add(Addition.valueOf(str));
            }

            var positionArray = (String[]) rs.getArray("positions").getArray();
            List<Position> positions = new ArrayList<>();
            for (var str : positionArray) {
                positions.add(Position.valueOf(str));
            }

            return GetHotel.builder()
                    .id(rs.getLong("id"))
                    .city(rs.getString("city"))
                    .name(rs.getString("name"))
                    .stars(rs.getInt("stars"))
                    .avgRate(rs.getDouble("avg_rate"))
                    .minPrice(rs.getDouble("min_price"))
                    .maxPrice(rs.getDouble("max_price"))
                    .imageUrl(rs.getString("image_url"))
                    .additions(additions)
                    .positions(positions)
                    .build();
        }
    }

    final class RoomViewRowMapper implements JdbcRowMapper<RoomView> {

        @Override
        public RoomView apply(ResultSet rs) throws SQLException {
            return new RoomView(rs.getLong("id"),
                        RoomView.Type.valueOf(rs.getString("room_view_type")),
                        rs.getDouble("price"));
        }
    }

    final class RoomTypeRowMapper implements JdbcRowMapper<RoomType> {

        @Override
        public RoomType apply(ResultSet rs) throws SQLException {
            return new RoomType(rs.getLong("id"),
                    RoomType.Type.valueOf(rs.getString("room_type_type")),
                    rs.getDouble("price"));
        }
    }

    final class RoomPeopleRowMapper implements JdbcRowMapper<RoomPeople> {

        @Override
        public RoomPeople apply(ResultSet rs) throws SQLException {
            return new RoomPeople(rs.getLong("id"),
                    RoomPeople.Type.valueOf(rs.getString("room_people_type")),
                    rs.getDouble("price"));
        }
    }
}
