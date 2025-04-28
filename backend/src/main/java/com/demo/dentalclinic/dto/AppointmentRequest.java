package com.demo.dentalclinic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentRequest {
    private String name;
    private String identificationNumber;
    private Long appointmentTypeId;
    private LocalDateTime appointmentTime;
}