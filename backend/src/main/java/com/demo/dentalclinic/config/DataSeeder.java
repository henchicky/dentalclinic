package com.demo.dentalclinic.config;

import com.demo.dentalclinic.model.Dentist;
import com.demo.dentalclinic.model.DentistSchedule;
import com.demo.dentalclinic.repository.DentistRepository;
import com.demo.dentalclinic.repository.DentistScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

@Configuration
@Profile("!test") // Don't run in test environment
public class DataSeeder {

    @Autowired
    private DentistScheduleRepository dentistScheduleRepository;

    @Bean
    public CommandLineRunner initData(DentistRepository dentistRepository) {
        return args -> {
            // Create sample dentists
            Dentist dentist1 = new Dentist();
            dentist1.setName("Dr. John Smith");
            dentist1.setPassword("password123");
            dentist1 = dentistRepository.save(dentist1);

            Dentist dentist2 = new Dentist();
            dentist2.setName("Dr. Sarah Johnson");
            dentist2.setPassword("password123");
            dentist2 = dentistRepository.save(dentist2);

            // Create sample schedules for the next 7 days
            LocalDateTime now = LocalDateTime.now();
            for (int i = 0; i < 7; i++) {
                LocalDateTime currentDate = now.plusDays(i);
                
                // Create a morning schedule (9 AM - 12 PM)
                createSchedule(dentist1, currentDate, LocalTime.of(9, 0), LocalTime.of(12, 0), true, 3);
                createSchedule(dentist2, currentDate, LocalTime.of(9, 0), LocalTime.of(12, 0), true, 3);
                
                // Create a lunch break (12 PM - 1 PM)
                createSchedule(dentist1, currentDate, LocalTime.of(12, 0), LocalTime.of(13, 0), false, 0);
                createSchedule(dentist2, currentDate, LocalTime.of(12, 0), LocalTime.of(13, 0), false, 0);
                
                // Create a afternoon schedule (1 PM - 5 PM)
                createSchedule(dentist1, currentDate, LocalTime.of(13, 0), LocalTime.of(17, 0), true, 4);
                createSchedule(dentist2, currentDate, LocalTime.of(13, 0), LocalTime.of(17, 0), true, 4);
            }
        };
    }

    private void createSchedule(Dentist dentist, LocalDateTime date, LocalTime startTime, LocalTime endTime, 
                              boolean isAvailable, int maxAppointments) {
        DentistSchedule schedule = new DentistSchedule();
        schedule.setDentist(dentist);
        schedule.setStartTime(LocalDateTime.of(date.toLocalDate(), startTime));
        schedule.setEndTime(LocalDateTime.of(date.toLocalDate(), endTime));
        schedule.setAvailable(isAvailable);
        schedule.setMaxAppointments(maxAppointments);
        schedule.setDescription(isAvailable ? "Available for appointments" : "Lunch break");
        
        dentistScheduleRepository.save(schedule);
    }
} 