package com.demo.dentalclinic.repository;

import com.demo.dentalclinic.model.AppointmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, Long> {
    Optional<AppointmentType> findByName(String name);
} 