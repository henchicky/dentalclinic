package com.demo.dentalclinic.service;

import com.demo.dentalclinic.dto.DentistScheduleRequest;
import com.demo.dentalclinic.dto.UnavailableDTO;
import com.demo.dentalclinic.model.Appointment;
import com.demo.dentalclinic.model.DentistSchedulePeriod;
import com.demo.dentalclinic.repository.AppointmentRepository;
import com.demo.dentalclinic.repository.DentistRepository;
import com.demo.dentalclinic.repository.DentistSchedulePeriodRepository;
import com.demo.dentalclinic.enums.AvailabilityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DentistScheduleService {

    private final DentistSchedulePeriodRepository dentistSchedulePeriodRepository;
    private final DentistRepository dentistRepository;

    @Autowired
    public DentistScheduleService(DentistSchedulePeriodRepository dentistSchedulePeriodRepository, DentistRepository dentistRepository) {
        this.dentistSchedulePeriodRepository = dentistSchedulePeriodRepository;
        this.dentistRepository = dentistRepository;
    }

    public DentistSchedulePeriod createSchedulePeriod(DentistScheduleRequest request) {
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

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

    public List<UnavailableDTO> getUnavailablePeriods(Long dentistId, LocalDate date) {
        var unavailable = dentistSchedulePeriodRepository.findByDentistIdAndDateAndType(
                dentistId, date, AvailabilityType.UNAVAILABLE);

        return unavailable.stream()
                .map(period -> new UnavailableDTO(
                        period.getId(),
                        LocalDateTime.of(period.getDate(), period.getStartTime()),
                        LocalDateTime.of(period.getDate(), period.getEndTime()),
                        period.getDescription()
                ))
                .collect(Collectors.toList());
    }
} 