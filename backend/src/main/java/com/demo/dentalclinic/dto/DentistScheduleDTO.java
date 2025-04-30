package com.demo.dentalclinic.dto;

import com.demo.dentalclinic.enums.AvailabilityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DentistScheduleDTO {
    private Long id;
    private Long dentistId;
    private String dentistName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private AvailabilityType type;
    private String description;
}
