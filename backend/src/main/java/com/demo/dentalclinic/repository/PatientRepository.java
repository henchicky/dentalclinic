package com.demo.dentalclinic.repository;

import com.demo.dentalclinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByIdentificationNumber(String identificationNumber);
}