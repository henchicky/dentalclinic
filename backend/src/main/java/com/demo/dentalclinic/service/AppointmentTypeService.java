package com.demo.dentalclinic.service;

import com.demo.dentalclinic.model.AppointmentType;
import com.demo.dentalclinic.repository.AppointmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentTypeService {

    private final AppointmentTypeRepository appointmentTypeRepository;

    @Autowired
    public AppointmentTypeService(AppointmentTypeRepository appointmentTypeRepository) {
        this.appointmentTypeRepository = appointmentTypeRepository;
    }

    @Cacheable("appointmentTypes")
    public List<AppointmentType> getAllAppointmentTypes() {
        return appointmentTypeRepository.findAll();
    }

    public Optional<AppointmentType> getAppointmentTypeById(Long id) {
        return appointmentTypeRepository.findById(id);
    }

    @Cacheable(value = "appointmentTypes", key = "#name")
    public Optional<AppointmentType> getAppointmentTypeByName(String name) {
        return appointmentTypeRepository.findByName(name);
    }

    @CacheEvict(value = "appointmentTypes", allEntries = true)
    public AppointmentType createAppointmentType(AppointmentType appointmentType) {
        if (appointmentTypeRepository.findByName(appointmentType.getName()).isPresent()) {
            throw new IllegalArgumentException("Appointment type with name " + appointmentType.getName() + " already exists");
        }
        return appointmentTypeRepository.save(appointmentType);
    }

//    public AppointmentType updateAppointmentType(Long id, AppointmentType appointmentTypeDetails) {
//        return appointmentTypeRepository.findById(id)
//            .map(existingAppointmentType -> {
//                // Check if the new name conflicts with any other appointment type
//                if (!existingAppointmentType.getName().equals(appointmentTypeDetails.getName()) &&
//                    appointmentTypeRepository.findByName(appointmentTypeDetails.getName()).isPresent()) {
//                    throw new IllegalArgumentException("Appointment type with name " + appointmentTypeDetails.getName() + " already exists");
//                }
//
//                existingAppointmentType.setName(appointmentTypeDetails.getName());
//                existingAppointmentType.setSlotCount(appointmentTypeDetails.getSlotCount());
//                existingAppointmentType.setDurationMinutes(appointmentTypeDetails.getDurationMinutes());
//                existingAppointmentType.setDescription(appointmentTypeDetails.getDescription());
//
//                return appointmentTypeRepository.save(existingAppointmentType);
//            })
//            .orElseThrow(() -> new IllegalArgumentException("Appointment type not found with id " + id));
//    }
//
//    public void deleteAppointmentType(Long id) {
//        if (!appointmentTypeRepository.existsById(id)) {
//            throw new IllegalArgumentException("Appointment type not found with id " + id);
//        }
//        appointmentTypeRepository.deleteById(id);
//    }
} 