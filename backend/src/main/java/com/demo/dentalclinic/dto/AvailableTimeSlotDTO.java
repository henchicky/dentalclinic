package com.demo.dentalclinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableTimeSlotDTO {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
