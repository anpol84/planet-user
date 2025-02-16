package ru.planet.hotel.controller;

import lombok.RequiredArgsConstructor;
import ru.planet.hotel.api.HotelApiDelegate;
import ru.planet.hotel.api.HotelApiResponses;
import ru.planet.hotel.model.CreateHotelRequest;
import ru.planet.hotel.model.UpdateHotelRequest;
import ru.planet.hotel.operation.*;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class HotelController implements HotelApiDelegate {

    private final CreateHotelOperation createHotelOperation;
    private final GetHotelOperation getHotelOperation;
    private final GetHotelsOperation getHotelsOperation;
    private final GetFilteredHotelsOperation getFilteredHotelsOperation;
    private final UpdateHotelOperation updateHotelOperation;
    private final DeleteHotelOperation deleteHotelOperation;

    @Override
    public HotelApiResponses.CreateHotelApiResponse createHotel(String token,
                                                                CreateHotelRequest createHotelRequest)
            throws Exception {
        createHotelOperation.activate(createHotelRequest);
        return new HotelApiResponses.CreateHotelApiResponse.CreateHotel200ApiResponse();
    }

    @Override
    public HotelApiResponses.DeleteHotelApiResponse deleteHotel(String token, long hotelId) throws Exception {
        deleteHotelOperation.activate(hotelId);
        return new HotelApiResponses.DeleteHotelApiResponse.DeleteHotel200ApiResponse();
    }

    @Override
    public HotelApiResponses.GetFilteredHotelsApiResponse getFilteredHotels(String token, String city, double minPrice)
            throws Exception {
        var hotels = getFilteredHotelsOperation.activate(city, minPrice);
        if (hotels.hotels() == null) {
            return new HotelApiResponses.GetFilteredHotelsApiResponse.GetFilteredHotels202ApiResponse();
        }
        return new HotelApiResponses.GetFilteredHotelsApiResponse.GetFilteredHotels200ApiResponse(hotels);
    }

    @Override
    public HotelApiResponses.GetHotelApiResponse getHotel(long hotelId) throws Exception {
        return new HotelApiResponses.GetHotelApiResponse.GetHotel200ApiResponse(getHotelOperation.activate(hotelId));
    }

    @Override
    public HotelApiResponses.GetHotelsApiResponse getHotels() throws Exception {
        return new HotelApiResponses.GetHotelsApiResponse.GetHotels200ApiResponse(getHotelsOperation.activate());
    }

    @Override
    public HotelApiResponses.UpdateHotelApiResponse updateHotel(String token, long hotelId,
                                                                UpdateHotelRequest updateHotelRequest) throws Exception {
        updateHotelOperation.activate(updateHotelRequest, hotelId);
        return new HotelApiResponses.UpdateHotelApiResponse.UpdateHotel200ApiResponse();
    }
}
