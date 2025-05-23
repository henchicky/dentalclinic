package com.demo.dentalclinic.repository;

import com.demo.dentalclinic.model.DentistSchedulePeriod;
import com.demo.dentalclinic.enums.AvailabilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DentistSchedulePeriodRepository extends JpaRepository<DentistSchedulePeriod, Long> {
    List<DentistSchedulePeriod> findByDentistIdAndDateAndType(Long dentistId, LocalDate date, AvailabilityType type);
} 