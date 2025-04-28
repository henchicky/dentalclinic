package com.demo.dentalclinic.service;

import com.demo.dentalclinic.model.DentistSchedule;
import com.demo.dentalclinic.repository.DentistScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DentistScheduleService {

    private final DentistScheduleRepository dentistScheduleRepository;

    @Autowired
    public DentistScheduleService(DentistScheduleRepository dentistScheduleRepository) {
        this.dentistScheduleRepository = dentistScheduleRepository;
    }

    public List<DentistSchedule> getDentistSchedule(Long dentistId, LocalDateTime startTime, LocalDateTime endTime) {
        return dentistScheduleRepository.findByDentistIdAndStartTimeBetween(dentistId, startTime, endTime);
    }

    public List<DentistSchedule> getAvailableSlots(Long dentistId, LocalDateTime startTime, LocalDateTime endTime) {
        return dentistScheduleRepository.findByDentistIdAndIsAvailableTrueAndStartTimeBetween(
                dentistId, startTime, endTime);
    }

    @Transactional
    public DentistSchedule createSchedule(DentistSchedule schedule) {
        // Validate that the time slot doesn't overlap with existing schedules
        validateTimeSlot(schedule);
        return dentistScheduleRepository.save(schedule);
    }

    @Transactional
    public void updateSchedule(Long id, DentistSchedule scheduleDetails) {
        DentistSchedule schedule = dentistScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + id));
        
        schedule.setStartTime(scheduleDetails.getStartTime());
        schedule.setEndTime(scheduleDetails.getEndTime());
        schedule.setAvailable(scheduleDetails.isAvailable());
        schedule.setMaxAppointments(scheduleDetails.getMaxAppointments());
        
        validateTimeSlot(schedule);
        dentistScheduleRepository.save(schedule);
    }

    private void validateTimeSlot(DentistSchedule newSchedule) {
        List<DentistSchedule> existingSchedules = dentistScheduleRepository
                .findByDentistIdAndStartTimeBetween(
                        newSchedule.getDentist().getId(),
                        newSchedule.getStartTime(),
                        newSchedule.getEndTime());
        
        if (!existingSchedules.isEmpty()) {
            throw new RuntimeException("Time slot overlaps with existing schedule");
        }
    }

    public boolean isDentistAvailable(Long dentistId, LocalDateTime appointmentTime) {
        List<DentistSchedule> availableSlots = dentistScheduleRepository
                .findAvailableSlotsAtTime(dentistId, appointmentTime);
        return !availableSlots.isEmpty();
    }
} 