package com.demo.dentalclinic.service;

import com.demo.dentalclinic.dto.DentistScheduleRequest;
import com.demo.dentalclinic.model.DentistSchedulePeriod;
import com.demo.dentalclinic.repository.DentistRepository;
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
    private final DentistRepository dentistRepository;

    @Autowired
    public DentistScheduleService(DentistSchedulePeriodRepository dentistSchedulePeriodRepository, DentistRepository dentistRepository) {
        this.dentistSchedulePeriodRepository = dentistSchedulePeriodRepository;
        this.dentistRepository = dentistRepository;
    }

    public List<DentistSchedulePeriod> getDentistScheduleBetweenDates(Long dentistId, LocalDate startDate, LocalDate endDate) {
        return dentistSchedulePeriodRepository.findByDentistIdAndDateBetween(dentistId, startDate, endDate);
    }

    public DentistSchedulePeriod createSchedulePeriod(DentistScheduleRequest request) {
        // Validate time range
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        // Convert DentistScheduleRequest to DentistSchedulePeriod before saving
        DentistSchedulePeriod newPeriod = new DentistSchedulePeriod();
        newPeriod.setDentist(dentistRepository.getDentistsById(request.getDentistId()));
        newPeriod.setStartTime(request.getStartTime().toLocalTime());
        newPeriod.setEndTime(request.getEndTime().toLocalTime());
        newPeriod.setType(request.getType());

        return dentistSchedulePeriodRepository.save(newPeriod);
    }

    public List<DentistSchedulePeriod> getAvailablePeriods(Long dentistId, LocalDate date) {
        return dentistSchedulePeriodRepository.findByDentistIdAndDateAndType(
            dentistId, date, AvailabilityType.AVAILABLE);
    }
} 