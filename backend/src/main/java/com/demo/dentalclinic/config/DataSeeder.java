
package com.demo.dentalclinic.config;

import com.demo.dentalclinic.enums.AppointmentStatus;
import com.demo.dentalclinic.model.Appointment;
import com.demo.dentalclinic.model.AppointmentType;
import com.demo.dentalclinic.model.Dentist;
import com.demo.dentalclinic.model.Patient;
import com.demo.dentalclinic.repository.AppointmentRepository;
import com.demo.dentalclinic.repository.AppointmentTypeRepository;
import com.demo.dentalclinic.repository.DentistRepository;
import com.demo.dentalclinic.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
            AppointmentRepository appointmentRepository
    ) {
        return args -> {
            // Seed AppointmentTypes
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

            // Seed Patients
            List<Patient> patients = Arrays.asList(
                    createPatient("John Doe", "P001"),
                    createPatient("Jane Smith", "P002"),
                    createPatient("Robert Johnson", "P003"),
                    createPatient("Maria Garcia", "P004"),
                    createPatient("David Lee", "P005"),
                    createPatient("Sarah Wilson", "P006"),
                    createPatient("Michael Brown", "P007"),
                    createPatient("Emily Davis", "P008"),
                    createPatient("James Miller", "P009"),
                    createPatient("Lisa Anderson", "P010")
            );
            patientRepository.saveAll(patients);
            
            // Seed Dentists
            List<Dentist> dentists = Arrays.asList(
                    createDentist("Dr. Jennifer Smith", "password123"),
                    createDentist("Dr. Michael Chen", "password123"),
                    createDentist("Dr. Sarah Johnson", "password123"),
                    createDentist("Dr. David Williams", "password123"),
                    createDentist("Dr. Emily Rodriguez", "password123")
            );
            dentistRepository.saveAll(dentists);
            
            // Seed Appointments
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
        };
    }

    private AppointmentType createAppointmentType(String name, int duration) {
        AppointmentType appointmentType = new AppointmentType();
        appointmentType.setName(name);
        appointmentType.setDurationMinutes(duration);
        return appointmentType;
    }

    private Patient createPatient(String name, String identificationNumber) {
        Patient patient = new Patient();
        patient.setName(name);
        patient.setIdentificationNumber(identificationNumber);
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
}