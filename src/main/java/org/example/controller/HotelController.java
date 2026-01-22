package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.DTO.HotelCreateRequest;
import org.example.DTO.HotelFullInfo;
import org.example.DTO.HotelShortInfo;
import org.example.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public List<HotelShortInfo> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/{id}")
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HotelShortInfo> createHotel(@Valid @RequestBody HotelCreateRequest request) {
        HotelShortInfo createdHotel = hotelService.createHotel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    @PostMapping("{id}/amenities")
    public ResponseEntity<?> addAmenities(@PathVariable Long id, @RequestBody List<String> amenities) {
        hotelService.addAmenities(id, amenities);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/histogram/{param}")
    public Map<String, Long> getHistogram(@PathVariable String param) {
        return hotelService.getHistogram(param);
    }
}
