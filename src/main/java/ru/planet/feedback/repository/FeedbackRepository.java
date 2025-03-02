package ru.planet.feedback.repository;

import jakarta.annotation.Nullable;
import ru.planet.feedback.dto.CreateFeedbackDto;
import ru.planet.feedback.dto.GetFeedbackDto;
import ru.planet.feedback.dto.UpdateFeedbackDto;
import ru.tinkoff.kora.database.common.UpdateCount;
import ru.tinkoff.kora.database.common.annotation.Query;
import ru.tinkoff.kora.database.common.annotation.Repository;
import ru.tinkoff.kora.database.jdbc.JdbcRepository;
import ru.tinkoff.kora.database.jdbc.mapper.result.JdbcResultColumnMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedbackRepository extends JdbcRepository {

    @Query("""
            INSERT INTO feedback (hotel_id, user_id, body, mark)
            VALUES (:feedback.hotelId, :feedback.userId, :feedback.body, :feedback.mark)
            """)
    void createFeedback(CreateFeedbackDto feedback);

    @Query("""
            SELECT id, user_id, body, mark, created_at FROM feedback
            WHERE hotel_id = :hotelId
            """)
    List<GetFeedbackDto> getHotelFeedbacks(Long hotelId);

    @Query("""
            UPDATE feedback SET body = :feedback.body, mark = :feedback.mark
            WHERE id = :feedbackId
            RETURNING hotel_id
            """)
    @Nullable
    Long updateFeedback(UpdateFeedbackDto feedback, Long feedbackId);

    @Query("""
            SELECT user_id FROM feedback
            WHERE id = :feedbackId
            """)
    Long getUserByFeedback(Long feedbackId);

    @Query("""
            DELETE FROM feedback WHERE id = :feedbackId
            RETURNING hotel_id
            """)
    @Nullable
    Long deleteFeedback(Long feedbackId);

    @Query("""
            DELETE FROM feedback WHERE hotel_id = :hotelId
            """)
    void deleteFeedbacksByHotelId(Long hotelId);

    @Query("""
            DELETE FROM feedback WHERE user_id = :userId
            """)
    void deleteFeedbacksByUserId(Long userId);

    @Query("""
            SELECT hotel_id FROM feedback WHERE user_id = :userId
            """)
    List<Long> getHotelsByUserId(Long userId);

    @Query("""
            SELECT id, user_id, body, mark, created_at FROM feedback
            WHERE id = :feedbackId
            """)
    @Nullable
    GetFeedbackDto getFeedbackById(Long feedbackId);

    @Query("""
            UPDATE hotel set avg_rate = (
            SELECT AVG(mark) FROM feedback WHERE hotel_id = :hotelId
            )
            WHERE id = :hotelId
            """)
    void updateRate(Long hotelId);

    final class DateMapper implements JdbcResultColumnMapper<LocalDateTime> {

        @Override
        public LocalDateTime apply(ResultSet row, int index) throws SQLException {
            Timestamp timestamp = row.getTimestamp(index);

            return timestamp != null ? timestamp.toLocalDateTime() : null;
        }
    }
}
