package com.demo.dentalclinic.repository;

import com.demo.dentalclinic.model.Appointment;
import com.demo.dentalclinic.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
} 