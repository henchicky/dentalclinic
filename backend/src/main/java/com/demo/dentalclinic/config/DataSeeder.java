package com.demo.dentalclinic.config;

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
            AppointmentRepository appointmentRepository,
            DentistSchedulePeriodRepository dentistSchedulePeriodRepository
    ) {
        return _ -> {
            seedAppointmentTypes(appointmentTypeRepository);

            seedPatients(patientRepository);

            seedDentists(dentistRepository);

            seedAppointments(dentistRepository, appointmentTypeRepository, patientRepository, appointmentRepository);

            seedDentistSchedulePeriods(dentistRepository, dentistSchedulePeriodRepository);
        };
    }

    private void seedAppointments(DentistRepository dentistRepository, AppointmentTypeRepository appointmentTypeRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        // Create some appointments for the next 7 days
        List<Appointment> appointments = new ArrayList<>();

        // Get saved entities from repositories
        List<Dentist> savedDentists = dentistRepository.findAll();
        List<Patient> savedPatients = patientRepository.findAll();
        List<AppointmentType> savedAppointmentTypes = appointmentTypeRepository.findAll();

        // Create some upcoming appointments
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0);

        // Create 20 sample appointments
        for (int i = 0; i < 20; i++) {
            Dentist dentist = savedDentists.get(i % savedDentists.size());
            Patient patient = savedPatients.get(i % savedPatients.size());
            AppointmentType appointmentType = savedAppointmentTypes.get(i % savedAppointmentTypes.size());

            // Create appointment at different times
            LocalDateTime appointmentTime = startDateTime.plusHours(i % 8).plusDays(i / 8);

            appointments.add(createAppointment(dentist, patient, appointmentType, appointmentTime, AppointmentStatus.UPCOMING));
        }

        // Create a few past appointments (some completed, some cancelled)
        LocalDateTime pastStartDateTime = LocalDateTime.now().minusDays(10).withHour(10).withMinute(0).withSecond(0).withNano(0);

        for (int i = 0; i < 10; i++) {
            Dentist dentist = savedDentists.get(i % savedDentists.size());
            Patient patient = savedPatients.get((i + 3) % savedPatients.size());
            AppointmentType appointmentType = savedAppointmentTypes.get((i + 2) % savedAppointmentTypes.size());

            // Create appointment at different past times
            LocalDateTime appointmentTime = pastStartDateTime.plusHours(i % 7).plusDays(i / 7);

            // Alternate between COMPLETED and CANCELLED status
            AppointmentStatus status = (i % 2 == 0) ? AppointmentStatus.COMPLETED : AppointmentStatus.CANCELLED;

            appointments.add(createAppointment(dentist, patient, appointmentType, appointmentTime, status));
        }

        appointmentRepository.saveAll(appointments);
    }

    private void seedDentists(DentistRepository dentistRepository) {
        List<Dentist> dentists = Arrays.asList(
                createDentist("Jennifer", "Jennifer"),
                createDentist("Michael", "Michael"),
                createDentist("Sarah", "Sarah"),
                createDentist("David", "David"),
                createDentist("Emily", "Emily")
        );
        dentistRepository.saveAll(dentists);
    }

    private void seedPatients(PatientRepository patientRepository) {
        List<Patient> patients = Arrays.asList(
                createPatient("John Doe"),
                createPatient("Jane Smith"),
                createPatient("Robert Johnson"),
                createPatient("Maria Garcia"),
                createPatient("David Lee"),
                createPatient("Sarah Wilson"),
                createPatient("Michael Brown"),
                createPatient("Emily Davis"),
                createPatient("James Miller"),
                createPatient("Lisa Anderson")
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

    private Appointment createAppointment(Dentist dentist, Patient patient, AppointmentType appointmentType,
                                          LocalDateTime appointmentTime, AppointmentStatus status) {
        Appointment appointment = new Appointment();
        appointment.setDentist(dentist);
        appointment.setPatient(patient);
        appointment.setAppointmentType(appointmentType);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setAppointmentStatus(status);
        return appointment;
    }

    private void seedDentistSchedulePeriods(DentistRepository dentistRepository, DentistSchedulePeriodRepository dentistSchedulePeriodRepository) {
        List<Dentist> dentists = dentistRepository.findAll();
        List<DentistSchedulePeriod> schedulePeriods = new ArrayList<>();

        // Create schedule periods for 14 days (2 weeks) starting from tomorrow
        LocalDate startDate = LocalDate.now().plusDays(1);

        for (Dentist dentist : dentists) {
            for (int day = 0; day < 14; day++) {
                LocalDate currentDate = startDate.plusDays(day);

                // Skip weekends (Saturday and Sunday)
                if (currentDate.getDayOfWeek().getValue() >= 6) {
                    continue;
                }

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
                        LocalTime.of(17, 0),
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