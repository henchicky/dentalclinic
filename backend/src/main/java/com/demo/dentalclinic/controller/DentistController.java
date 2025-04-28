package com.demo.dentalclinic.controller;

import com.demo.dentalclinic.model.DentistSchedulePeriod;
import com.demo.dentalclinic.dto.DentistScheduleRequest;
import com.demo.dentalclinic.service.DentistScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dentists")
@Tag(name = "Dentist Controller")
public class DentistController {

    private final DentistScheduleService dentistScheduleService;

    @Autowired
    public DentistController(DentistScheduleService dentistScheduleService) {
        this.dentistScheduleService = dentistScheduleService;
    }

    @PostMapping("/{dentistId}/schedule")
    public DentistSchedulePeriod createSchedulePeriod(
            @PathVariable Long dentistId,
            @RequestBody DentistScheduleRequest request) {
        return dentistScheduleService.createSchedulePeriod(request, dentistId);
    }

    @GetMapping("/{dentistId}/schedule")
    public List<DentistSchedulePeriod> getDentistSchedule(
            @PathVariable Long dentistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dentistScheduleService.getDentistSchedule(dentistId, date);
    }

    @GetMapping("/{dentistId}/schedule/range")
    public List<DentistSchedulePeriod> getDentistScheduleBetweenDates(
            @PathVariable Long dentistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return dentistScheduleService.getDentistScheduleBetweenDates(dentistId, startDate, endDate);
    }

    @GetMapping("/{dentistId}/schedule/available")
    public List<DentistSchedulePeriod> getAvailablePeriods(
            @PathVariable Long dentistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dentistScheduleService.getAvailablePeriods(dentistId, date);
    }

    @GetMapping("/{dentistId}/schedule/unavailable")
    public List<DentistSchedulePeriod> getUnavailablePeriods(
            @PathVariable Long dentistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dentistScheduleService.getUnavailablePeriods(dentistId, date);
    }
} 