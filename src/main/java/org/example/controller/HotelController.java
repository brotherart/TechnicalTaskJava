package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.DTO.HotelCreateRequest;
import org.example.DTO.HotelFullInfo;
import org.example.DTO.HotelShortInfo;
import org.example.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @GetMapping("/hotels")
    public List<HotelShortInfo> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/hotels/{id}")
    public HotelFullInfo getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    @GetMapping("/search")
    public List<HotelShortInfo> searchHotels(@RequestParam(required = false) String name,
                                             @RequestParam(required = false) String brand,
                                             @RequestParam(required = false) String city,
                                             @RequestParam(required = false) String country,
                                             @RequestParam(required = false) List<String> amenities) {
        return hotelService.searchHotels(name, brand, city, country, amenities);
    }

    @PostMapping("/hotels")
    public HotelShortInfo createHotel(@RequestBody HotelCreateRequest request) {
        return hotelService.createHotel(request);
    }

    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<?> addAmenities(@PathVariable Long id, @RequestBody List<String> amenities) {
        hotelService.addAmenities(id, amenities);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/histogram/{param}")
    public Map<String, Long> getHistogram(@PathVariable String param) {
        return hotelService.getHistogram(param);
    }
}
