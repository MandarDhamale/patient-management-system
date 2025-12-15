package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {
    public static PatientResponseDTO toDTO(Patient patient){
        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(String.valueOf(patient.getId()));
        patientResponseDTO.setName(String.valueOf(patient.getName()));
        patientResponseDTO.setEmail(String.valueOf(patient.getEmail()));
        patientResponseDTO.setAddress(String.valueOf(patient.getAddress()));
        patientResponseDTO.setDateOfBirth(String.valueOf(patient.getDateOfBirth()));
        return patientResponseDTO;
    }

    public static Patient toPatient(PatientRequestDTO patientRequestDTO){
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));
        return patient;
    }

}
