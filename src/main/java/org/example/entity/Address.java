package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Column(name = "house_number")
    private Integer houseNumber;
    private String street;
    private String city;
    private String country;
    @Column(name = "post_code")
    private String postCode;
}
