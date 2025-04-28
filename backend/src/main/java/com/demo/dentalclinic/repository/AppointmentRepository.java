package com.demo.dentalclinic.repository;

import com.demo.dentalclinic.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDentistIdAndAppointmentTimeBetween(Long dentistId, LocalDateTime startTime, LocalDateTime endTime);
    List<Appointment> findByDentistId(Long dentistId);
    List<Appointment> findByPatientId(Long patientId);
}