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
import java.util.ArrayList;
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
        AppointmentType appointmentType = appointmentTypeService.getAppointmentTypeById(request.getAppointmentType())
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment type"));

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
            List<Appointment> existingAppointments = appointmentRepository.findByDentistIdAndAppointmentTimeGreaterThanEqualAndAppointmentEndTimeLessThanEqual(dentistId, date.atTime(startTime), date.atTime(endTime));

            return existingAppointments.isEmpty();
        });
    }

    public List<Appointment> getDentistAppointments(Long dentistId) {
        return appointmentRepository.findByDentistId(dentistId);
    }
    
    public List<AvailableTimeSlotDTO> findAllAvailableTimeSlots() {
        // Map to store available timings by date
        java.util.Map<LocalDate, List<LocalTime>> availableTimesByDate = new java.util.HashMap<>();
        
        // Get current date and next 30 days
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);
        
        // Get all dentists
        List<Dentist> dentists = dentistRepository.findAll();
        
        // For each date in the range
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            // For use in lambda

            // For each dentist
            for (Dentist dentist : dentists) {
                // Get all available periods for the dentist on the current date
                List<DentistSchedulePeriod> availablePeriods = dentistScheduleService.getAvailablePeriods(
                        dentist.getId(), date);
                
                // Skip if no available periods
                if (availablePeriods.isEmpty()) {
                    continue;
                }
                
                // Get existing appointments for this dentist on this date
                LocalDateTime dayStart = date.atStartOfDay();
                LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
                List<Appointment> existingAppointments = appointmentRepository
                        .findByDentistIdAndAppointmentTimeGreaterThanEqualAndAppointmentEndTimeLessThanEqual(
                                dentist.getId(), dayStart, dayEnd);
                
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
        
        // Convert the map to a list of DTOs
        List<AvailableTimeSlotDTO> result = new ArrayList<>();
        for (java.util.Map.Entry<LocalDate, List<LocalTime>> entry : availableTimesByDate.entrySet()) {
            // Sort the times for consistency
            java.util.Collections.sort(entry.getValue());
            
            // Create a DTO with the date and its available times
            if(!entry.getValue().isEmpty()){
                result.add(new AvailableTimeSlotDTO(
                        entry.getKey(),
                        entry.getValue()
                ));
            }
        }
        
        // Sort the result by date
        result.sort(java.util.Comparator.comparing(AvailableTimeSlotDTO::getDate));
        
        return result;
    }
    
    private void collectAvailableTimesForPeriod(
            LocalDate date,
            DentistSchedulePeriod period,
            List<Appointment> existingAppointments,
            java.util.Map<LocalDate, List<LocalTime>> availableTimesByDate) {
        
        // Start time of the period
        LocalTime currentTime = period.getStartTime();
        
        // End time of the period
        LocalTime endTime = period.getEndTime();
        
        // Ensure we have a list for this date
        if (!availableTimesByDate.containsKey(date)) {
            availableTimesByDate.put(date, new ArrayList<>());
        }
        
        // Generate 30-minute slots until we reach the end time
        while (currentTime.plusMinutes(30).isBefore(endTime) ||
               currentTime.plusMinutes(30).equals(endTime)) {
            
            LocalTime slotStart = currentTime;
            LocalTime slotEnd = currentTime.plusMinutes(30);
            
            // Check if this slot overlaps with any existing appointment
            boolean overlapsWithExistingAppointment = existingAppointments.stream()
                    .anyMatch(appointment -> {
                        LocalTime appointmentStart = appointment.getAppointmentTime().toLocalTime();
                        LocalTime appointmentEnd = appointment.getAppointmentEndTime().toLocalTime();
                        
                        // Check for overlap
                        return !(slotEnd.isBefore(appointmentStart) || slotStart.isAfter(appointmentEnd) ||
                                slotEnd.equals(appointmentStart) || slotStart.equals(appointmentEnd));
                    });
            
            // If no overlap and we haven't already added this time, add it to the list
            if (!overlapsWithExistingAppointment && !availableTimesByDate.get(date).contains(slotStart)) {
                availableTimesByDate.get(date).add(slotStart);
            }
            
            // Move to next 30-minute slot
            currentTime = slotEnd;
        }
    }
} 