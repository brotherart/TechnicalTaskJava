package org.example.service;

import org.example.DTO.HotelCreateRequest;
import org.example.DTO.HotelFullInfo;
import org.example.DTO.HotelShortInfo;

import java.util.List;
import java.util.Map;

public interface HotelService {
    List<HotelShortInfo> getAllHotels();
    HotelFullInfo getHotelById(Long id);
    List<HotelShortInfo> searchHotels(String name, String brand, String city, String country, List<String> amenities);
    HotelShortInfo createHotel(HotelCreateRequest request);
    void addAmenities(Long hotelId, List<String> amenities);
    Map<String, Long> getHistogram(String param);
}
