package com.demo.dentalclinic.repository;

import com.demo.dentalclinic.model.DentistSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DentistScheduleRepository extends JpaRepository<DentistSchedule, Long> {
    List<DentistSchedule> findByDentistIdAndStartTimeBetween(
            Long dentistId, LocalDateTime startTime, LocalDateTime endTime);
    
    List<DentistSchedule> findByDentistIdAndAvailableTrueAndStartTimeBetween(
            Long dentistId, LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("SELECT ds FROM DentistSchedule ds WHERE ds.dentist.id = :dentistId " +
           "AND ds.startTime <= :time AND ds.endTime > :time AND ds.isAvailable = true")
    List<DentistSchedule> findAvailableSlotsAtTime(
            @Param("dentistId") Long dentistId, @Param("time") LocalDateTime time);
} 