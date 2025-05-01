package com.demo.dentalclinic.dto;

import com.demo.dentalclinic.enums.AvailabilityType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DentistScheduleRequest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AvailabilityType type;
    private String description;
}