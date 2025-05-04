package com.demo.dentalclinic.service;

import com.demo.dentalclinic.dto.AppointmentRequest;
import com.demo.dentalclinic.dto.AvailableTimeSlotDTO;
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
import java.util.*;

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
        AppointmentType appointmentType = appointmentTypeService.getAppointmentTypeById(request.getAppointmentType())
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment type: " + request.getAppointmentType()));

        LocalDateTime appointmentStart = request.getAppointmentTime();
        LocalDateTime appointmentEnd = appointmentStart.plusMinutes(appointmentType.getDurationMinutes());

        Dentist availableDentist = dentistRepository
                .findAll()
                .stream()
                .filter(dentist -> isDentistAvailableForAppointment(dentist.getId(), appointmentStart.toLocalDate(), appointmentStart.toLocalTime(), appointmentEnd.toLocalTime()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No available dentist"));

        Patient patient = patientRepository.findByName(request.getName()).orElseGet(() -> {
            Patient newPatient = new Patient();
            newPatient.setName(request.getName());
            return patientRepository.save(newPatient);
        });

        Appointment appointment = new Appointment();
        appointment.setAppointmentType(appointmentType);
        appointment.setDentist(availableDentist);
        appointment.setPatient(patient);
        appointment.setDescription(request.getDescription());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setAppointmentEndTime(request.getAppointmentTime().plusMinutes(appointmentType.getDurationMinutes()));
        appointment.setAppointmentStatus(AppointmentStatus.UPCOMING);

        return appointmentRepository.save(appointment);
    }

    private boolean isDentistAvailableForAppointment(Long dentistId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<DentistSchedulePeriod> availablePeriods = dentistScheduleService.getAvailablePeriods(dentistId, date);

        return availablePeriods.stream().anyMatch(period -> {

            boolean isWithinPeriod = !startTime.isBefore(period.getStartTime()) && !endTime.isAfter(period.getEndTime());

            if (!isWithinPeriod) {
                return false;
            }
            
            List<Appointment> overlappingAppointments = appointmentRepository.findExistingAppointments(
                    dentistId, date.atTime(startTime), date.atTime(endTime));
    
            return overlappingAppointments.isEmpty();
        });
    }
    
    public List<AvailableTimeSlotDTO> findAllAvailableTimeSlots() {
        // Map to store available timings by date
        Map<LocalDate, List<LocalTime>> availableTimesByDate = new TreeMap<>();
        
        // Get the current date and next 30 days
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);
        
        List<Dentist> dentists = dentistRepository.findAll();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            for (Dentist dentist : dentists) {
                // Get all available periods for the dentist on the current date
                List<DentistSchedulePeriod> availablePeriods = dentistScheduleService.getAvailablePeriods(
                        dentist.getId(), date);

                if (availablePeriods.isEmpty()) {
                    continue;
                }
                
                LocalDateTime dayStart = date.atStartOfDay();
                LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
                List<Appointment> existingAppointments = appointmentRepository
                        .findExistingAppointments(dentist.getId(), dayStart, dayEnd);
                
                // For each available period, collect available times
                for (DentistSchedulePeriod period : availablePeriods) {
                    collectAvailableTimesForPeriod(
                            date,
                            period,
                            existingAppointments,
                            availableTimesByDate
                    );
                }
            }
        }
        
        List<AvailableTimeSlotDTO> result = new ArrayList<>();
        for (Map.Entry<LocalDate, List<LocalTime>> entry : availableTimesByDate.entrySet()) {
            Collections.sort(entry.getValue());
            if(!entry.getValue().isEmpty()){
                result.add(new AvailableTimeSlotDTO(
                        entry.getKey(),
                        entry.getValue()
                ));
            }
        }

        result.sort(Comparator.comparing(AvailableTimeSlotDTO::getDate));
        
        return result;
    }
    
    private void collectAvailableTimesForPeriod(
            LocalDate date,
            DentistSchedulePeriod period,
            List<Appointment> existingAppointments,
            Map<LocalDate, List<LocalTime>> availableTimesByDate) {

        LocalTime currentTime = period.getStartTime();
        LocalTime endTime = period.getEndTime();

        if (!availableTimesByDate.containsKey(date)) {
            availableTimesByDate.put(date, new ArrayList<>());
        }
        
        // Generate 30-minute slots until we reach the end time
        while (currentTime.plusMinutes(30).isBefore(endTime) ||
               currentTime.plusMinutes(30).equals(endTime)) {
            
            LocalTime slotStart = currentTime;
            LocalTime slotEnd = currentTime.plusMinutes(30);
            
            boolean isDentistHavingAppointment = existingAppointments.stream()
                    .anyMatch(appointment -> {
                        LocalTime appointmentStart = appointment.getAppointmentTime().toLocalTime();
                        LocalTime appointmentEnd = appointment.getAppointmentEndTime().toLocalTime();
                        
                        // Check for overlap
                        return !(slotEnd.isBefore(appointmentStart) || slotStart.isAfter(appointmentEnd) ||
                                slotEnd.equals(appointmentStart) || slotStart.equals(appointmentEnd));
                    });
            
            if (!isDentistHavingAppointment && !availableTimesByDate.get(date).contains(slotStart)) {
                availableTimesByDate.get(date).add(slotStart);
            }
            
            // Move to next 30-minute slot
            currentTime = slotEnd;
        }
    }

    public List<Appointment> getDentistAppointmentsByDate(Long dentistId, LocalDate date) {
        return appointmentRepository.findExistingAppointments(dentistId, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }
} 