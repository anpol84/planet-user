package ru.planet.feedback.controller;

import lombok.RequiredArgsConstructor;
import ru.planet.feedback.operation.*;
import ru.planet.hotel.api.FeedbackApiDelegate;
import ru.planet.hotel.api.FeedbackApiResponses;
import ru.planet.hotel.model.CreateFeedbackRequest;
import ru.planet.hotel.model.UpdateFeedbackRequest;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class FeedbackController implements FeedbackApiDelegate {
    private final CreateFeedbackOperation createFeedbackOperation;
    private final GetHotelFeedbacksOperation getHotelFeedbacksOperation;
    private final UpdateFeedbackOperation updateFeedbackOperation;
    private final DeleteFeedbackOperation deleteFeedbackOperation;
    private final GetFeedbackOperation getFeedbackOperation;

    @Override
    public FeedbackApiResponses.CreateFeedbackApiResponse createFeedback(String token,
                                                                         CreateFeedbackRequest createFeedbackRequest)
            throws Exception {
        createFeedbackOperation.activate(createFeedbackRequest, token);
        return new FeedbackApiResponses.CreateFeedbackApiResponse.CreateFeedback200ApiResponse();
    }

    @Override
    public FeedbackApiResponses.DeleteFeedbackApiResponse deleteFeedback(String token, long feedbackId) throws Exception {
        deleteFeedbackOperation.activate(feedbackId, token);
        return new FeedbackApiResponses.DeleteFeedbackApiResponse.DeleteFeedback200ApiResponse();
    }

    @Override
    public FeedbackApiResponses.GetFeedbackApiResponse getFeedback(String token, long feedbackId) throws Exception {
        return new FeedbackApiResponses.GetFeedbackApiResponse.GetFeedback200ApiResponse(getFeedbackOperation.activate(feedbackId));
    }

    @Override
    public FeedbackApiResponses.GetHotelFeedbacksApiResponse getHotelFeedbacks(long hotelId) throws Exception {
        return new FeedbackApiResponses.GetHotelFeedbacksApiResponse.GetHotelFeedbacks200ApiResponse(getHotelFeedbacksOperation.activate(hotelId));
    }

    @Override
    public FeedbackApiResponses.UpdateFeedbackApiResponse updateFeedback(String token, long feedbackId, UpdateFeedbackRequest updateFeedbackRequest) throws Exception {
        updateFeedbackOperation.activate(updateFeedbackRequest, feedbackId, token);
        return new FeedbackApiResponses.UpdateFeedbackApiResponse.UpdateFeedback200ApiResponse();
    }
}
