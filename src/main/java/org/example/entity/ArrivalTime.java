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
public class ArrivalTime {
    @Column(name = "check_in")
    private String checkIn;
    @Column(name = "check_out")
    private String checkOut;
}
