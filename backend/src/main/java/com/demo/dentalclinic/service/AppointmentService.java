package com.demo.dentalclinic.service;

import com.demo.dentalclinic.dto.AppointmentRequest;
import com.demo.dentalclinic.enums.AppointmentStatus;
import com.demo.dentalclinic.model.*;
import com.demo.dentalclinic.repository.AppointmentRepository;
import com.demo.dentalclinic.repository.DentistRepository;
import com.demo.dentalclinic.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DentistRepository dentistRepository;
    private final DentistScheduleService dentistScheduleService;
    private final PatientRepository patientRepository;
    private final AppointmentTypeService appointmentTypeService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, DentistRepository dentistRepository, DentistScheduleService dentistScheduleService, AppointmentTypeService appointmentTypeService, PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.dentistRepository = dentistRepository;
        this.dentistScheduleService = dentistScheduleService;
        this.appointmentTypeService = appointmentTypeService;
    }

    public Appointment createAppointment(AppointmentRequest request) {
        AppointmentType appointmentType = appointmentTypeService.getAppointmentTypeById(request.getAppointmentTypeId()).orElseThrow(() -> new IllegalArgumentException("Invalid appointment type ID"));

        LocalDateTime appointmentStart = request.getAppointmentTime();
        LocalDateTime appointmentEnd = appointmentStart.plusMinutes(appointmentType.getDurationMinutes());

        Dentist availableDentist = dentistRepository.findAll().stream().filter(dentist -> isDentistAvailableForAppointment(dentist.getId(), appointmentStart.toLocalDate(), appointmentStart.toLocalTime(), appointmentEnd.toLocalTime())).findFirst().orElseThrow(() -> new IllegalArgumentException("No available dentist"));

        Patient patient = patientRepository.findByIdentificationNumber(request.getIdentificationNumber()).orElseGet(() -> {
            Patient newPatient = new Patient();
            newPatient.setName(request.getName());
            newPatient.setIdentificationNumber(request.getIdentificationNumber());
            return patientRepository.save(newPatient);
        });

        Appointment appointment = new Appointment();
        appointment.setAppointmentType(appointmentType);
        appointment.setDentist(availableDentist);
        appointment.setPatient(patient);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setAppointmentStatus(AppointmentStatus.UPCOMING);

        return appointmentRepository.save(appointment);
    }

    private boolean isDentistAvailableForAppointment(Long dentistId, LocalDate date, LocalTime startTime, LocalTime endTime) {

        // Get all available periods for the dentist on the given date
        List<DentistSchedulePeriod> availablePeriods = dentistScheduleService.getAvailablePeriods(dentistId, date);

        // Check if the appointment time falls within any available period
        return availablePeriods.stream().anyMatch(period -> {
            // Check if the appointment time is within the available period
            boolean isWithinPeriod = !startTime.isBefore(period.getStartTime()) && !endTime.isAfter(period.getEndTime());

            if (!isWithinPeriod) {
                return false;
            }

            // Check for existing appointments that might overlap
            List<Appointment> existingAppointments = appointmentRepository.findByDentistIdAndAppointmentTimeBetween(dentistId, date.atTime(startTime), date.atTime(endTime));

            return existingAppointments.isEmpty();
        });
    }

    public List<Appointment> getDentistAppointments(Long dentistId) {
        return appointmentRepository.findByDentistId(dentistId);
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
} 