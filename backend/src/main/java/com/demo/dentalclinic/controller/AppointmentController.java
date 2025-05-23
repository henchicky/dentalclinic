package com.demo.dentalclinic.controller;

import com.demo.dentalclinic.dto.AppointmentRequest;
import com.demo.dentalclinic.dto.AvailableTimeSlotDTO;
import com.demo.dentalclinic.model.Appointment;
import com.demo.dentalclinic.model.AppointmentType;
import com.demo.dentalclinic.service.AppointmentService;
import com.demo.dentalclinic.service.AppointmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentTypeService appointmentTypeService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, 
                               AppointmentTypeService appointmentTypeService) {
        this.appointmentService = appointmentService;
        this.appointmentTypeService = appointmentTypeService;
    }

    @PostMapping
    public Appointment createAppointment(@RequestBody AppointmentRequest request) {
        return appointmentService.createAppointment(request);
    }

    @GetMapping("/appointmentTypes")
    public List<AppointmentType> getAllAppointmentTypes() {
        return appointmentTypeService.getAllAppointmentTypes();
    }
    
    @GetMapping("/availableSlots")
    public List<AvailableTimeSlotDTO> getAvailableAppointmentSlots() {
        return appointmentService.findAllAvailableTimeSlots();
    }
}