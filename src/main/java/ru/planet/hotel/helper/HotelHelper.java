package ru.planet.hotel.helper;

import lombok.RequiredArgsConstructor;
import ru.planet.hotel.model.RoomPeople;
import ru.planet.hotel.model.RoomType;
import ru.planet.hotel.model.RoomView;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.common.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class HotelHelper {
    private final HotelRepository hotelRepository;

    public double calculateMin(List<RoomView> roomViews, List<RoomType> roomTypes, List<RoomPeople> roomPeople) {
        return Stream.concat(Stream.concat(roomViews.stream().map(RoomView::price),
                        roomTypes.stream().map(RoomType::price)),
                roomPeople.stream().map(RoomPeople::price)).min(Double::compare).get();
    }

    public List<Long> extractRoomViews(List<RoomView> roomViews) {
        return roomViews.stream()
                .map(roomView -> {
                    var viewInBd = hotelRepository.getRoomView(roomView.type().getValue(), roomView.price());
                    return viewInBd.orElseGet(() -> hotelRepository.saveRoomView(roomView.type().getValue(),
                            roomView.price()));

                })
                .toList();
    }

    public List<Long> extractRoomPeople(List<RoomPeople> roomPeople) {
        return roomPeople.stream()
                .map(room -> {
                    var roomInBd = hotelRepository.getRoomPeople(room.type().getValue(), room.price());
                    return roomInBd.orElseGet(() -> hotelRepository.saveRoomPeople(room.type().getValue(),
                            room.price())
                    );
                })
                .toList();
    }

    public List<Long> extractRoomTypes(List<RoomType> roomTypes) {
        return roomTypes.stream()
                .map(roomType -> {
                    var typeInBd = hotelRepository.getRoomType(roomType.type().getValue(), roomType.price());
                    return typeInBd.orElseGet(() -> hotelRepository.saveRoomType(roomType.type().getValue(),
                            roomType.price()));

                })
                .toList();
    }
}
