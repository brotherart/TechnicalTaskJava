package org.example.service;

import org.example.DTO.HotelCreateRequest;
import org.example.DTO.HotelFullInfo;
import org.example.entity.Address;
import org.example.entity.ArrivalTime;
import org.example.entity.Contacts;
import org.example.entity.Hotel;
import org.example.repository.HotelRepository;
import org.example.service.impl.HotelServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void testGetHotelById_WhenHotelExists_ShouldReturnHotel() {
        Hotel testHotel = Hotel.builder()
                .id(1L)
                .name("Test Hotel")
                .description("Test Description")
                .brand("Test Brand")
                .address(new Address(1, "Street", "City", "Country", "12345"))
                .contacts(new Contacts("+123", "test@test.com"))
                .arrivalTime(new ArrivalTime("14:00", "12:00"))
                .amenities(Arrays.asList("WiFi", "Parking"))
                .build();

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(testHotel));

        HotelFullInfo result = hotelService.getHotelById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Hotel");
        assertThat(result.getAmenities()).containsExactly("WiFi", "Parking");
    }

    @Test
    void testCreateHotel_ShouldSaveAndReturnShortResponse() {
        HotelCreateRequest request = new HotelCreateRequest();
        request.setName("New Hotel");
        request.setBrand("New Brand");
        request.setAddress(new Address(10, "New Street", "New City", "New Country", "000000"));
        request.setContacts(new Contacts("+375290000000", "new@gmail.com"));
        request.setArrivalTime(new ArrivalTime("15:00", "11:00"));

        Hotel savedHotel = Hotel.builder()
                .id(100L)
                .name(request.getName())
                .brand(request.getBrand())
                .address(request.getAddress())
                .contacts(request.getContacts())
                .arrivalTime(request.getArrivalTime())
                .amenities(List.of())
                .build();

        when(hotelRepository.save(any(Hotel.class))).thenReturn(savedHotel);

        var result = hotelService.createHotel(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getName()).isEqualTo("New Hotel");
        assertThat(result.getAddress()).contains("10", "New Street", "New City");
    }
}