package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DTO.HotelCreateRequest;
import org.example.entity.Address;
import org.example.entity.ArrivalTime;
import org.example.entity.Contacts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HotelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllHotels_ShouldReturnNotEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].name", is("DoubleTree by Hilton Minsk")));
    }

    @Test
    void testGetHotelById_WhenHotelExists_ShouldReturnHotel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("DoubleTree by Hilton Minsk")));
    }

    @Test
    void testSearchHotels_ByCity_ShouldReturnHotels() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/search")
                        .param("city", "Minsk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].address", containsString("Minsk")));
    }

    @Test
    void testCreateHotel_ShouldReturnCreatedHotel() throws Exception {
        HotelCreateRequest request = new HotelCreateRequest();
        request.setName("Test Hotel");
        request.setBrand("Test Brand");
        request.setAddress(new Address(1, "Test St", "Test City", "Test Country", "12345"));
        request.setContacts(new Contacts("+1234567890", "test@example.com"));
        request.setArrivalTime(new ArrivalTime("14:00", "12:00"));

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Test Hotel")));
    }
}