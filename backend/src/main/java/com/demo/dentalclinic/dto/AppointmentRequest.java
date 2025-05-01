package com.demo.dentalclinic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentRequest {
    private String name;
    private String description;
    private Long appointmentType;
    private LocalDateTime appointmentTime;
}