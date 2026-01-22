package org.example.service;

import org.example.entity.Address;
import org.example.entity.ArrivalTime;
import org.example.entity.Contacts;
import org.example.entity.Hotel;
import org.example.repository.HotelRepository;
import org.example.DTO.HotelCreateRequest;
import org.example.DTO.HotelFullInfo;
import org.example.DTO.HotelShortInfo;
import org.example.service.impl.HotelServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel createTestHotel() {
        return Hotel.builder()
                .id(1L)
                .name("Test Hotel")
                .description("Test Description")
                .brand("Test Brand")
                .address(new Address(123, "Main Street", "New York", "USA", "10001"))
                .contacts(new Contacts("+1-555-1234", "test@hotel.com"))
                .arrivalTime(new ArrivalTime("14:00", "12:00"))
                .amenities(Arrays.asList("WiFi", "Parking"))
                .build();
    }

    private HotelCreateRequest createTestRequest() {
        return HotelCreateRequest.builder()
                .name("New Hotel")
                .description("New Description")
                .brand("New Brand")
                .address(new Address(456, "Second Street", "Los Angeles", "USA", "90001"))
                .contacts(new Contacts("+1-555-5678", "new@hotel.com"))
                .arrivalTime(new ArrivalTime("15:00", "11:00"))
                .build();
    }

    @Test
    void getAllHotels_ShouldReturnHotelList() {
        Hotel hotel = createTestHotel();
        when(hotelRepository.findAll()).thenReturn(Arrays.asList(hotel));

        List<HotelShortInfo> result = hotelService.getAllHotels();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Hotel");
        verify(hotelRepository).findAll();
    }

    @Test
    void getHotelById_WhenHotelExists_ShouldReturnHotel() {
        Hotel hotel = createTestHotel();
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        HotelFullInfo result = hotelService.getHotelById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Hotel");
        assertThat(result.getBrand()).isEqualTo("Test Brand");
        verify(hotelRepository).findById(1L);
    }

    @Test
    void getHotelById_WhenHotelNotExists_ShouldThrowException() {
        when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.getHotelById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Hotel not found");
        verify(hotelRepository).findById(999L);
    }

    @Test
    void searchHotels_ByCity_ShouldReturnHotels() {
        Hotel hotel = createTestHotel();
        when(hotelRepository.searchHotels(null, null, "New York", null))
                .thenReturn(Arrays.asList(hotel));

        List<HotelShortInfo> result = hotelService.searchHotels(
                null, null, "New York", null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Hotel");
        verify(hotelRepository).searchHotels(null, null, "New York", null);
    }

    @Test
    void searchHotels_WithAmenities_ShouldReturnHotels() {
        Hotel hotel = createTestHotel();
        List<String> amenities = Arrays.asList("WiFi", "Parking");
        when(hotelRepository.searchByAmenities(amenities, amenities.size()))
                .thenReturn(Arrays.asList(hotel));

        List<HotelShortInfo> result = hotelService.searchHotels(
                null, null, null, null, amenities);

        assertThat(result).hasSize(1);
        verify(hotelRepository).searchByAmenities(amenities, amenities.size());
    }

    @Test
    void createHotel_ShouldSaveAndReturnHotel() {
        Hotel hotel = createTestHotel();
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        HotelShortInfo result = hotelService.createHotel(createTestRequest());

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Hotel");
        verify(hotelRepository).save(any(Hotel.class));
    }

    @Test
    void addAmenities_WhenHotelNotExists_ShouldThrowException() {
        when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.addAmenities(999L, Arrays.asList("Spa")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Hotel not found");

        verify(hotelRepository).findById(999L);
        verify(hotelRepository, never()).save(any(Hotel.class));
    }

    @Test
    void getHistogram_ForBrand_ShouldReturnHistogram() {
        List<Object[]> mockResult = Arrays.asList(
                new Object[]{"Hilton", 5L},
                new Object[]{"Marriott", 3L}
        );
        when(hotelRepository.countHotelsByBrand()).thenReturn(mockResult);

        var result = hotelService.getHistogram("brand");

        assertThat(result).hasSize(2);
        assertThat(result.get("Hilton")).isEqualTo(5L);
        assertThat(result.get("Marriott")).isEqualTo(3L);
        verify(hotelRepository).countHotelsByBrand();
    }

    @Test
    void getHistogram_ForCity_ShouldReturnHistogram() {
        List<Object[]> mockResult = Arrays.asList(
                new Object[]{"New York", 10L},
                new Object[]{"Los Angeles", 7L}
        );
        when(hotelRepository.countHotelsByCity()).thenReturn(mockResult);

        var result = hotelService.getHistogram("city");

        assertThat(result).hasSize(2);
        assertThat(result.get("New York")).isEqualTo(10L);
        assertThat(result.get("Los Angeles")).isEqualTo(7L);
        verify(hotelRepository).countHotelsByCity();
    }

    @Test
    void getHistogram_ForCountry_ShouldReturnHistogram() {
        List<Object[]> mockResult = Arrays.asList(
                new Object[]{"USA", 15L},
                new Object[]{"Canada", 8L}
        );
        when(hotelRepository.countHotelsByCountry()).thenReturn(mockResult);

        var result = hotelService.getHistogram("country");

        assertThat(result).hasSize(2);
        assertThat(result.get("USA")).isEqualTo(15L);
        assertThat(result.get("Canada")).isEqualTo(8L);
        verify(hotelRepository).countHotelsByCountry();
    }

    @Test
    void getHistogram_ForAmenities_ShouldReturnHistogram() {
        List<Object[]> mockResult = Arrays.asList(
                new Object[]{"WiFi", 20L},
                new Object[]{"Parking", 15L}
        );
        when(hotelRepository.countHotelsByAmenities()).thenReturn(mockResult);

        var result = hotelService.getHistogram("amenities");

        assertThat(result).hasSize(2);
        assertThat(result.get("WiFi")).isEqualTo(20L);
        assertThat(result.get("Parking")).isEqualTo(15L);
        verify(hotelRepository).countHotelsByAmenities();
    }

    @Test
    void getHistogram_WithInvalidParameter_ShouldThrowException() {
        assertThatThrownBy(() -> hotelService.getHistogram("invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid parameter");

        verify(hotelRepository, never()).countHotelsByBrand();
        verify(hotelRepository, never()).countHotelsByCity();
        verify(hotelRepository, never()).countHotelsByCountry();
        verify(hotelRepository, never()).countHotelsByAmenities();
    }
}