package com.demo.dentalclinic.controller;

import com.demo.dentalclinic.dto.DentistScheduleRequest;
import com.demo.dentalclinic.dto.LoginRequest;
import com.demo.dentalclinic.dto.UnavailableDTO;
import com.demo.dentalclinic.model.Appointment;
import com.demo.dentalclinic.model.DentistSchedulePeriod;
import com.demo.dentalclinic.service.AppointmentService;
import com.demo.dentalclinic.service.DentistScheduleService;
import com.demo.dentalclinic.service.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dentists")
public class DentistController {

    private final DentistScheduleService dentistScheduleService;
    private final AppointmentService appointmentService;
    private final DentistService dentistService;

    @Autowired
    public DentistController(DentistScheduleService dentistScheduleService, AppointmentService appointmentService, DentistService dentistService) {
        this.dentistScheduleService = dentistScheduleService;
        this.appointmentService = appointmentService;
        this.dentistService = dentistService;
    }

    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody LoginRequest loginRequest) {
        Long response = dentistService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/schedule")
    public DentistSchedulePeriod createSchedulePeriod(@RequestBody DentistScheduleRequest request) {
        return dentistScheduleService.createSchedulePeriod(request);
    }

    @GetMapping("/{dentistId}/appointments")
    public List<Appointment> getDentistAppointmentsByDate(
            @PathVariable Long dentistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return appointmentService.getDentistAppointmentsByDate(dentistId, date);
    }

    @GetMapping("/{dentistId}/unavailablePeriods")
    public List<UnavailableDTO> getDentistUnavailablePeriodsByDate(
            @PathVariable Long dentistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dentistScheduleService.getUnavailablePeriods(dentistId, date);
    }
} 