package com.demo.dentalclinic.service;

import com.demo.dentalclinic.dto.LoginRequest;
import com.demo.dentalclinic.exception.AuthenticationException;
import com.demo.dentalclinic.model.Dentist;
import com.demo.dentalclinic.repository.DentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DentistService {

    private final DentistRepository dentistRepository;

    @Autowired
    public DentistService(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }

    public Long authenticate(LoginRequest loginRequest) {
        Dentist dentist = dentistRepository.findByName(loginRequest.getName())
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        // In a real application, you would use a password encoder for secure password comparison
        if (!dentist.getPassword().equals(loginRequest.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }

        return dentist.getId();
    }
}
