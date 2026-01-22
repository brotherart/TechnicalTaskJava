package org.example.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Address;
import org.example.entity.ArrivalTime;
import org.example.entity.Contacts;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelCreateRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;

    private String brand;

    @Valid
    private Address address;

    @Valid
    private Contacts contacts;

    @Valid
    private ArrivalTime arrivalTime;
}