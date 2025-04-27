package com.demo.dentalclinic.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.demo.dentalclinic.enums.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "appointments")
public class Appointment {
    
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Setter
    @Getter
    private String patientName;

    @Column(nullable = false)
    @Setter
    @Getter
    private String identificationNumber;
    
    @Column(nullable = false)
    @Setter
    @Getter
    private LocalDateTime appointmentTime;
    
    @Column
    @Setter
    @Getter
    private String description;

    @Column
    @Setter
    @Getter
    private Long dentistId;

    @Column
    @Setter
    @Getter
    private AppointmentStatus appointmentStatus;
} 