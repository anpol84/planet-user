package ru.planet.feedback.helper;

import org.mapstruct.Mapper;
import ru.planet.feedback.dto.CreateFeedbackDto;
import ru.planet.feedback.dto.GetFeedbackDto;
import ru.planet.feedback.dto.UpdateFeedbackDto;
import ru.planet.hotel.model.CreateFeedbackRequest;
import ru.planet.hotel.model.Feedback;
import ru.planet.hotel.model.GetFeedbackResponse;
import ru.planet.hotel.model.UpdateFeedbackRequest;

@Mapper
public interface FeedbackMapper {
    CreateFeedbackDto toCreateFeedbackDto(CreateFeedbackRequest request);

    Feedback toFeedback(GetFeedbackDto feedback, String userLogin);

    UpdateFeedbackDto toUpdateFeedbackDto(UpdateFeedbackRequest request);

    GetFeedbackResponse toGetFeedbackResponse(GetFeedbackDto feedbackDto);
}
