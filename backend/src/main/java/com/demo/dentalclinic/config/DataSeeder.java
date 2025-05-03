package com.demo.dentalclinic.config;

import com.demo.dentalclinic.dto.AppointmentRequest;
import com.demo.dentalclinic.dto.AppointmentRequest;
import com.demo.dentalclinic.enums.AppointmentStatus;
import com.demo.dentalclinic.enums.AvailabilityType;
import com.demo.dentalclinic.model.Appointment;
import com.demo.dentalclinic.model.AppointmentType;
import com.demo.dentalclinic.model.Dentist;
import com.demo.dentalclinic.model.DentistSchedulePeriod;
import com.demo.dentalclinic.model.Patient;
import com.demo.dentalclinic.repository.AppointmentRepository;
import com.demo.dentalclinic.repository.AppointmentTypeRepository;
import com.demo.dentalclinic.repository.DentistRepository;
import com.demo.dentalclinic.repository.DentistSchedulePeriodRepository;
import com.demo.dentalclinic.repository.PatientRepository;
import com.demo.dentalclinic.service.AppointmentService;
import com.demo.dentalclinic.service.AppointmentService;
import com.demo.dentalclinic.service.AppointmentTypeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("!test")
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(
            DentistRepository dentistRepository,
            AppointmentTypeRepository appointmentTypeRepository,
            PatientRepository patientRepository,
            AppointmentTypeService appointmentTypeService,
            DentistSchedulePeriodRepository dentistSchedulePeriodRepository,
            AppointmentService appointmentService
    ) {
        return _ -> {
            seedAppointmentTypes(appointmentTypeRepository);

            seedPatients(patientRepository);

            seedDentists(dentistRepository);

            seedDentistSchedulePeriods(dentistRepository, dentistSchedulePeriodRepository);

            seedAppointments(appointmentTypeService, appointmentService);
        };
    }

    private void seedAppointments(AppointmentTypeService appointmentTypeService, AppointmentService appointmentService) {
        // Inject required services
        PatientRepository patientRepository = appointmentService.getPatientRepository();
        
        // Get all appointment types and patients
        List<AppointmentType> appointmentTypes = appointmentTypeService.getAllAppointmentTypes();
        List<Patient> patients = patientRepository.findAll();
        
        if (appointmentTypes.isEmpty() || patients.isEmpty()) {
            System.out.println("Cannot seed appointments: No appointment types or patients found");
            return;
        }
        
        // Create some upcoming appointments
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0);
        
        // Track the current appointment time, starting from tomorrow at 9 AM
        LocalDateTime currentDateTime = startDateTime;
        int dayCounter = 0;
        
        // Create 15 sample appointments
        for (int i = 0; i < 15; i++) {
            Patient patient = patients.get(i % patients.size());
            AppointmentType appointmentType = appointmentTypes.get(i % appointmentTypes.size());
            
            // If we've added appointments pushing past 5 PM, move to the next day
            if (currentDateTime.getHour() >= 19) {
                dayCounter++;
                currentDateTime = startDateTime.plusDays(dayCounter).withHour(9).withMinute(0);
            }
            
            // Create appointment request
            AppointmentRequest request = new AppointmentRequest();
            request.setName(patient.getName());
            request.setDescription("Seeded appointment #" + (i + 1));
            request.setAppointmentType(appointmentType.getId());
            request.setAppointmentTime(currentDateTime);

            try {
                // Create the appointment using the service
                Appointment appointment = appointmentService.createAppointment(request);
                
                // Move to the next time slot based on appointment duration
                currentDateTime = currentDateTime.plusMinutes(appointmentType.getDurationMinutes());
            } catch (Exception e) {
                // If there's an error (like no dentist available), move the time forward and try again
                System.out.println("Failed to create appointment at " + currentDateTime + ": " + e.getMessage());
                currentDateTime = currentDateTime.plusHours(1);
            }
        }
    }

    private void seedDentists(DentistRepository dentistRepository) {
        List<Dentist> dentists = Arrays.asList(
                createDentist("Jennifer", "Jennifer")
//                createDentist("Michael", "Michael"),
//                createDentist("Sarah", "Sarah"),
//                createDentist("David", "David"),
//                createDentist("Emily", "Emily")
        );
        dentistRepository.saveAll(dentists);
    }

    private void seedPatients(PatientRepository patientRepository) {
        List<Patient> patients = Arrays.asList(
                createPatient("John Doe"),
                createPatient("Jane Smith")
        );
        patientRepository.saveAll(patients);
    }

    private void seedAppointmentTypes(AppointmentTypeRepository appointmentTypeRepository) {
        List<AppointmentType> appointmentTypes = Arrays.asList(
                createAppointmentType("Cleaning", 30),
                createAppointmentType("Extraction", 45),
                createAppointmentType("Consultation", 30),
                createAppointmentType("Filling", 30),
                createAppointmentType("Root Canal", 90),
                createAppointmentType("Crown", 60),
                createAppointmentType("Bridge", 90),
                createAppointmentType("Denture", 60),
                createAppointmentType("Braces", 60),
                createAppointmentType("Others", 30)
        );
        appointmentTypeRepository.saveAll(appointmentTypes);
    }

    private AppointmentType createAppointmentType(String name, int duration) {
        AppointmentType appointmentType = new AppointmentType();
        appointmentType.setName(name);
        appointmentType.setDurationMinutes(duration);
        return appointmentType;
    }

    private Patient createPatient(String name) {
        Patient patient = new Patient();
        patient.setName(name);
        return patient;
    }

    private Dentist createDentist(String name, String password) {
        Dentist dentist = new Dentist();
        dentist.setName(name);
        dentist.setPassword(password);
        return dentist;
    }

    // createAppointment method removed as we now use AppointmentService.createAppointment

    private void seedDentistSchedulePeriods(DentistRepository dentistRepository, DentistSchedulePeriodRepository dentistSchedulePeriodRepository) {
        List<Dentist> dentists = dentistRepository.findAll();
        List<DentistSchedulePeriod> schedulePeriods = new ArrayList<>();

        // Create schedule periods for 14 days (2 weeks) starting from tomorrow
        LocalDate startDate = LocalDate.now().plusDays(1);

        for (Dentist dentist : dentists) {
            for (int day = 0; day < 14; day++) {
                LocalDate currentDate = startDate.plusDays(day);

                // Skip weekends (Saturday and Sunday)
//                if (currentDate.getDayOfWeek().getValue() >= 6) {
//                    continue;
//                }

                // Morning availability (9:00 AM - 12:00 PM)
                schedulePeriods.add(createSchedulePeriod(
                        dentist,
                        currentDate,
                        LocalTime.of(9, 0),
                        LocalTime.of(12, 0),
                        AvailabilityType.AVAILABLE,
                        "Morning shift"
                ));

                // Lunch break (12:00 PM - 1:00 PM)
                schedulePeriods.add(createSchedulePeriod(
                        dentist,
                        currentDate,
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0),
                        AvailabilityType.UNAVAILABLE,
                        "Lunch break"
                ));

                // Afternoon availability (1:00 PM - 5:00 PM)
                schedulePeriods.add(createSchedulePeriod(
                        dentist,
                        currentDate,
                        LocalTime.of(13, 0),
                        LocalTime.of(19, 0),
                        AvailabilityType.AVAILABLE,
                        "Afternoon shift"
                ));

                // Add some random unavailable periods for meetings, etc.
                if (day % 3 == 0) {
                    schedulePeriods.add(createSchedulePeriod(
                            dentist,
                            currentDate,
                            LocalTime.of(14, 0),
                            LocalTime.of(15, 0),
                            AvailabilityType.UNAVAILABLE,
                            "Staff meeting"
                    ));
                }

                // Dr. Jennifer Smith has special hours on Wednesdays
                if (dentist.getName().equals("Dr. Jennifer Smith") &&
                        currentDate.getDayOfWeek().getValue() == 3) {
                    schedulePeriods.add(createSchedulePeriod(
                            dentist,
                            currentDate,
                            LocalTime.of(17, 0),
                            LocalTime.of(19, 0),
                            AvailabilityType.AVAILABLE,
                            "Evening appointments"
                    ));
                }
            }
        }

        dentistSchedulePeriodRepository.saveAll(schedulePeriods);
    }

    private DentistSchedulePeriod createSchedulePeriod(Dentist dentist, LocalDate date,
                                                       LocalTime startTime, LocalTime endTime,
                                                       AvailabilityType type, String description) {
        DentistSchedulePeriod period = new DentistSchedulePeriod();
        period.setDentist(dentist);
        period.setDate(date);
        period.setStartTime(startTime);
        period.setEndTime(endTime);
        period.setType(type);
        period.setDescription(description);
        return period;
    }
}