package com.demo.dentalclinic.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.demo.dentalclinic.enums.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "appointments")
@Setter
@Getter
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private String identificationNumber;
    
    @Column(nullable = false)
    private LocalDateTime appointmentTime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id", nullable = false)
    private Long dentistId;

    @Column
    private String description;

    @Column(nullable = false)
    private AppointmentStatus appointmentStatus;
} 