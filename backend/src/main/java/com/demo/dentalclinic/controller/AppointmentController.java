package com.demo.dentalclinic.controller;

import com.demo.dentalclinic.model.Appointment;
import com.demo.dentalclinic.service.AppointmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointment Controller")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

//    @GetMapping
//    public List<Appointment> getAllAppointments() {
//        return appointmentService.getAllAppointments();
//    }

    @GetMapping("/{id}")
    public List<Appointment> getAppointmentsByDentistId(@PathVariable Long id) {
        return appointmentService.getAppointmentsByDentistId(id);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Appointment> getAppointmentById(
//            @PathVariable Long id) {
//        return appointmentService.getAppointmentById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

    @PostMapping
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        return appointmentService.createAppointment(appointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointmentDetails) {
        try {
            Appointment updatedAppointment = appointmentService.updateAppointment(id, appointmentDetails);
            return ResponseEntity.ok(updatedAppointment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 