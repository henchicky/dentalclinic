
package com.demo.dentalclinic.config;

import com.demo.dentalclinic.model.AppointmentType;
import com.demo.dentalclinic.model.Patient;
import com.demo.dentalclinic.repository.AppointmentTypeRepository;
import com.demo.dentalclinic.repository.DentistRepository;
import com.demo.dentalclinic.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("!test")
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(
            DentistRepository dentistRepository,
            AppointmentTypeRepository appointmentTypeRepository,
            PatientRepository patientRepository
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
}