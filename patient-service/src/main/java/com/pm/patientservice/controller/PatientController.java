package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import com.pm.patientservice.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    // dependency injection
    private PatientService patientService;
    public PatientController(PatientService patientService) {this.patientService = patientService;}

    @GetMapping("/")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients(){
        List<PatientResponseDTO> patientResponseDTOS = patientService.getPatients();
        return ResponseEntity.ok().body(patientResponseDTOS);
    }
}
