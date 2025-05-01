package com.demo.dentalclinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableTimeSlotDTO {
    private LocalDate date;
    private List<LocalTime> availableTimings;
}
