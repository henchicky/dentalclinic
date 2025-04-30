package com.demo.dentalclinic.controller;

import com.demo.dentalclinic.dto.DentistScheduleRequest;
import com.demo.dentalclinic.dto.LoginRequest;
import com.demo.dentalclinic.model.DentistSchedulePeriod;
import com.demo.dentalclinic.service.DentistScheduleService;
import com.demo.dentalclinic.service.DentistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dentists")
@Tag(name = "Dentist Controller")
public class DentistController {

    private final DentistScheduleService dentistScheduleService;
    private final DentistService dentistService;

    @Autowired
    public DentistController(DentistScheduleService dentistScheduleService, DentistService dentistService) {
        this.dentistScheduleService = dentistScheduleService;
        this.dentistService = dentistService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a dentist", description = "Validates dentist credentials and returns the dentist ID if successful")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<Long> login(@RequestBody LoginRequest loginRequest) {
        Long response = dentistService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/schedule")
    public DentistSchedulePeriod createSchedulePeriod(@RequestBody DentistScheduleRequest request) {
        return dentistScheduleService.createSchedulePeriod(request);
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