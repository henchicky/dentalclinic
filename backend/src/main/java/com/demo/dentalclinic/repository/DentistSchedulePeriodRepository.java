package com.demo.dentalclinic.repository;

import com.demo.dentalclinic.model.DentistSchedulePeriod;
import com.demo.dentalclinic.enums.AvailabilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DentistSchedulePeriodRepository extends JpaRepository<DentistSchedulePeriod, Long> {
    List<DentistSchedulePeriod> findByDentistIdAndDate(Long dentistId, LocalDate date);
    List<DentistSchedulePeriod> findByDentistIdAndDateBetween(Long dentistId, LocalDate startDate, LocalDate endDate);
    List<DentistSchedulePeriod> findByDentistIdAndDateAndType(Long dentistId, LocalDate date, AvailabilityType type);
} 