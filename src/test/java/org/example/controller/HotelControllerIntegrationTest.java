package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DTO.HotelCreateRequest;
import org.example.entity.Address;
import org.example.entity.ArrivalTime;
import org.example.entity.Contacts;
import org.example.entity.Hotel;
import org.example.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HotelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HotelRepository hotelRepository;

    private Hotel testHotel;

    @BeforeEach
    void setUp() {
        hotelRepository.deleteAll(); // Очищаем базу перед каждым тестом

        // Создаем тестовый отель
        testHotel = Hotel.builder()
                .name("Test Hotel")
                .description("Test Description")
                .brand("Test Brand")
                .address(new Address(123, "Main Street", "New York", "USA", "10001"))
                .contacts(new Contacts("+1-555-1234", "test@hotel.com"))
                .arrivalTime(new ArrivalTime("14:00", "12:00"))
                .amenities(Arrays.asList("WiFi", "Parking"))
                .build();

        testHotel = hotelRepository.save(testHotel);
    }

    @Test
    void testGetAllHotels_ShouldReturnNotEmptyList() throws Exception {
        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Hotel")));
    }

    @Test
    void testGetHotelById_WhenHotelExists_ShouldReturnHotel() throws Exception {
        mockMvc.perform(get("/hotels/{id}", testHotel.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testHotel.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Hotel")))
                .andExpect(jsonPath("$.brand", is("Test Brand")));
    }

    @Test
    void testGetHotelById_WhenHotelNotExists_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/hotels/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Hotel not found")));
    }

    @Test
    void testSearchHotels_ByCity_ShouldReturnHotels() throws Exception {
        mockMvc.perform(get("/hotels/search")
                        .param("city", "New York"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Hotel")));
    }

    @Test
    void testSearchHotels_ByNonExistingCity_ShouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/hotels/search")
                        .param("city", "London"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testCreateHotel_ShouldReturnCreatedHotel() throws Exception {
        HotelCreateRequest request = HotelCreateRequest.builder()
                .name("New Hotel")
                .description("New Description")
                .brand("New Brand")
                .address(new Address(456, "Second Street", "Los Angeles", "USA", "90001"))
                .contacts(new Contacts("+1-555-5678", "new@hotel.com"))
                .arrivalTime(new ArrivalTime("15:00", "11:00"))
                .build();

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Hotel")));
    }

    @Test
    void testAddAmenities_ShouldAddAmenities() throws Exception {
        String[] newAmenities = {"Spa", "Gym"};

        mockMvc.perform(post("/hotels/{id}/amenities", testHotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAmenities)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/hotels/{id}", testHotel.getId()))
                .andExpect(jsonPath("$.amenities", hasSize(4))) // Было 2, добавили 2
                .andExpect(jsonPath("$.amenities", containsInAnyOrder("WiFi", "Parking", "Spa", "Gym")));
    }
}