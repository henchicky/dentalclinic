package com.demo.dentalclinic.service;

import com.demo.dentalclinic.model.Appointment;
import com.demo.dentalclinic.model.Schedule;
import com.demo.dentalclinic.repository.AppointmentRepository;
import com.demo.dentalclinic.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long id, Schedule scheduleDetail) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + id));
        
        schedule.setPatientName(scheduleDetail.getPatientName());
        schedule.setAppointmentTime(scheduleDetail.getAppointmentTime());
//        schedule.setDescription(scheduleDetail.getDescription());

        return scheduleRepository.save(schedule);
    }

    public void deleteAppointment(Long id) {
        scheduleRepository.deleteById(id);
    }
} 