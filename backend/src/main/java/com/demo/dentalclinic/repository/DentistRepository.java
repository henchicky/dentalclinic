package com.demo.dentalclinic.repository;

import com.demo.dentalclinic.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DentistRepository extends JpaRepository<Dentist, Long> {
    Dentist getDentistsById(Long id);
    Optional<Dentist> findByName(String name);
}