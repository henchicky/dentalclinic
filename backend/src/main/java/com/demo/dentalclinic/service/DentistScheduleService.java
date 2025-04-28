package com.demo.dentalclinic.service;

import com.demo.dentalclinic.model.DentistSchedulePeriod;
import com.demo.dentalclinic.repository.DentistSchedulePeriodRepository;
import com.demo.dentalclinic.enums.AvailabilityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class DentistScheduleService {

    private final DentistSchedulePeriodRepository dentistSchedulePeriodRepository;

    @Autowired
    public DentistScheduleService(DentistSchedulePeriodRepository dentistSchedulePeriodRepository) {
        this.dentistSchedulePeriodRepository = dentistSchedulePeriodRepository;
    }

    public List<DentistSchedulePeriod> getDentistSchedule(Long dentistId, LocalDate date) {
        return dentistSchedulePeriodRepository.findByDentistIdAndDate(dentistId, date);
    }

    public List<DentistSchedulePeriod> getDentistScheduleBetweenDates(Long dentistId, LocalDate startDate, LocalDate endDate) {
        return dentistSchedulePeriodRepository.findByDentistIdAndDateBetween(dentistId, startDate, endDate);
    }

    public DentistSchedulePeriod createSchedulePeriod(DentistSchedulePeriod schedulePeriod) {
        // Validate time range
        if (schedulePeriod.getStartTime().isAfter(schedulePeriod.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        // Check for overlapping periods
        List<DentistSchedulePeriod> existingPeriods = dentistSchedulePeriodRepository
            .findByDentistIdAndDate(schedulePeriod.getDentist().getId(), schedulePeriod.getDate());

        for (DentistSchedulePeriod existing : existingPeriods) {
            if (isOverlapping(existing, schedulePeriod)) {
                throw new IllegalArgumentException("Schedule period overlaps with existing period");
            }
        }

        return dentistSchedulePeriodRepository.save(schedulePeriod);
    }

    private boolean isOverlapping(DentistSchedulePeriod existing, DentistSchedulePeriod newPeriod) {
        LocalTime existingStart = existing.getStartTime();
        LocalTime existingEnd = existing.getEndTime();
        LocalTime newStart = newPeriod.getStartTime();
        LocalTime newEnd = newPeriod.getEndTime();

        return !(newEnd.isBefore(existingStart) || newStart.isAfter(existingEnd));
    }

    public List<DentistSchedulePeriod> getAvailablePeriods(Long dentistId, LocalDate date) {
        return dentistSchedulePeriodRepository.findByDentistIdAndDateAndType(
            dentistId, date, AvailabilityType.AVAILABLE);
    }

    public List<DentistSchedulePeriod> getUnavailablePeriods(Long dentistId, LocalDate date) {
        return dentistSchedulePeriodRepository.findByDentistIdAndDateAndType(
            dentistId, date, AvailabilityType.UNAVAILABLE);
    }
} 