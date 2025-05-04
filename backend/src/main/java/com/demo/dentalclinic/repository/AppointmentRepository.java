package com.demo.dentalclinic.repository;

import com.demo.dentalclinic.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDentistIdAndAppointmentTimeGreaterThanEqualAndAppointmentEndTimeLessThanEqual(Long dentistId, LocalDateTime startTime, LocalDateTime endTime);

    List<Appointment> findByDentistId(Long dentistId);

    @Query("SELECT a FROM Appointment a WHERE a.dentist.id = :dentistId AND " +
            // Case 1: Appointment starts before or at the requested start time and ends after or at the requested start time
            "(a.appointmentTime <= :startTime AND a.appointmentEndTime > :startTime) OR " +
            // Case 2: Appointment starts after the requested start time but before the requested end time
            "(a.appointmentTime >= :startTime AND a.appointmentTime < :endTime) OR " +
            // Case 3: Appointment completely encompasses the requested time range
            "(a.appointmentTime <= :startTime AND a.appointmentEndTime >= :endTime)")
    List<Appointment> findOverlappingAppointments(
            @Param("dentistId") Long dentistId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}