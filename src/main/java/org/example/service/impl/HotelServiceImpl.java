package org.example.service.impl;

import org.example.DTO.HotelCreateRequest;
import org.example.DTO.HotelFullInfo;
import org.example.DTO.HotelShortInfo;
import org.example.entity.Hotel;
import org.example.exceptions.HotelNotFoundException;
import org.example.repository.HotelRepository;
import org.example.service.HotelService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    public final HotelRepository hotelRepository;

    @Override
    public List<HotelShortInfo> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::convertToShortInfo)
                .collect(Collectors.toList());
    }

    @Override
    public HotelFullInfo getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found with id: " + id));
        return convertToFullInfo(hotel);
    }

    @Override
    public List<HotelShortInfo> searchHotels(String name, String brand,
                                                 String city, String country,
                                                 List<String> amenities) {
        List<Hotel> hotels;

        if (amenities != null && !amenities.isEmpty()) {
            hotels = hotelRepository.searchByAmenities(amenities, amenities.size());

            hotels = hotels.stream()
                    .filter(hotel ->
                            (name == null || hotel.getName().toLowerCase().contains(name.toLowerCase())) &&
                                    (brand == null || hotel.getBrand().equalsIgnoreCase(brand)) &&
                                    (city == null || hotel.getAddress().getCity().equalsIgnoreCase(city)) &&
                                    (country == null || hotel.getAddress().getCountry().equalsIgnoreCase(country)))
                    .collect(Collectors.toList());
        } else {
            hotels = hotelRepository.searchHotels(name, brand, city, country);
        }

        return hotels.stream()
                .map(this::convertToShortInfo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HotelShortInfo createHotel(HotelCreateRequest request) {
        Hotel hotel = Hotel.builder()
                .name(request.getName())
                .description(request.getDescription())
                .brand(request.getBrand())
                .address(request.getAddress())
                .contacts(request.getContacts())
                .arrivalTime(request.getArrivalTime())
                .amenities(new ArrayList<>())
                .build();

        Hotel savedHotel = hotelRepository.save(hotel);
        return convertToShortInfo(savedHotel);
    }

    @Override
    @Transactional
    public void addAmenities(Long hotelId, List<String> amenities) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + hotelId));

        List<String> existingAmenities = hotel.getAmenities();
        amenities.forEach(amenity -> {
            if (!existingAmenities.contains(amenity)) {
                existingAmenities.add(amenity);
            }
        });

        hotelRepository.save(hotel);
    }

    @Override
    public Map<String, Long> getHistogram(String param) {
        switch (param.toLowerCase()) {
            case "brand":
                return convertResultToMap(hotelRepository.countHotelsByBrand());
            case "city":
                return convertResultToMap(hotelRepository.countHotelsByCity());
            case "country":
                return convertResultToMap(hotelRepository.countHotelsByCountry());
            case "amenities":
                return convertResultToMap(hotelRepository.countHotelsByAmenities());
            default:
                throw new IllegalArgumentException("Invalid parameter: " + param);
        }
    }

    private HotelShortInfo convertToShortInfo(Hotel hotel) {
        String fullAddress = String.format("%d %s, %s, %s, %s",
                hotel.getAddress().getHouseNumber(),
                hotel.getAddress().getStreet(),
                hotel.getAddress().getCity(),
                hotel.getAddress().getPostCode(),
                hotel.getAddress().getCountry());

        return HotelShortInfo.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .address(fullAddress)
                .phone(hotel.getContacts().getPhone())
                .build();
    }

    private HotelFullInfo convertToFullInfo(Hotel hotel) {
        return HotelFullInfo.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .brand(hotel.getBrand())
                .address(hotel.getAddress())
                .contacts(hotel.getContacts())
                .arrivalTime(hotel.getArrivalTime())
                .amenities(hotel.getAmenities())
                .build();
    }

    private Map<String, Long> convertResultToMap(List<Object[]> result) {
        return result.stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> (Long) obj[1]
                ));
    }
}
