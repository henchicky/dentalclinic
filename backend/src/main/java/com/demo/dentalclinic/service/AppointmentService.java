package com.demo.dentalclinic.service;

import com.demo.dentalclinic.enums.AppointmentStatus;
import com.demo.dentalclinic.model.Appointment;
import com.demo.dentalclinic.model.Dentist;
import com.demo.dentalclinic.model.DentistSchedule;
import com.demo.dentalclinic.repository.AppointmentRepository;
import com.demo.dentalclinic.repository.DentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DentistRepository dentistRepository;
    private final DentistScheduleService dentistScheduleService;

    @Autowired
    public AppointmentService(
            AppointmentRepository appointmentRepository,
            DentistRepository dentistRepository,
            DentistScheduleService dentistScheduleService) {
        this.appointmentRepository = appointmentRepository;
        this.dentistRepository = dentistRepository;
        this.dentistScheduleService = dentistScheduleService;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsByDentistId(Long id) {
        return appointmentRepository.findByDentistId(id);
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Transactional
    public Appointment createAppointment(Appointment appointment) {
        // Find available dentists for the appointment time
        List<Dentist> allDentists = dentistRepository.findAll();
        Optional<Dentist> availableDentist = allDentists.stream()
                .filter(dentist -> dentistScheduleService.isDentistAvailable(
                        dentist.getId(), appointment.getAppointmentTime()))
                .findFirst();

        if (availableDentist.isEmpty()) {
            throw new RuntimeException("No available dentists for the requested time slot");
        }

        appointment.setDentistId(availableDentist.get().getId());
        appointment.setAppointmentStatus(AppointmentStatus.Upcoming);
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long id, Appointment appointmentDetails) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        
        appointment.setPatientName(appointmentDetails.getPatientName());
        appointment.setAppointmentTime(appointmentDetails.getAppointmentTime());
        appointment.setDescription(appointmentDetails.getDescription());

        return appointmentRepository.save(appointment);
    }
} 