package com.demo.dentalclinic.dto;

import com.demo.dentalclinic.enums.AvailabilityType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnavailableDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
}