package com.example.service;

import com.example.entity.Incident;
import com.example.entity.Technician;
import com.example.repository.IncidentRepository;
import com.example.repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    public void saveIncident(Incident incident) {
        incidentRepository.save(incident);
    }
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }
    public Long getIncidentsCount() {
        return incidentRepository.count();
    }
    @Autowired
    private TechnicianRepository technicianRepository;

    public void assignTechnician(Long incidentId, Long technicianId) {
        Incident incident = incidentRepository.findById(incidentId).orElseThrow(() -> new RuntimeException("Incident not found"));
        Technician technician = technicianRepository.findById(technicianId).orElseThrow(() -> new RuntimeException("Technician not found"));

        incident.setTechnician(technician); // Assuming Incident entity has a `Technician` field
        incidentRepository.save(incident);
    }

    public List<Incident> searchIncidents(String query) {
        return incidentRepository.findByDescriptionContainingIgnoreCase(query);
    }
    public List<Incident> searchByStatus(String status) {
        return incidentRepository.findByStatus(status);
    }

    public List<Incident> searchByType(String type) {
        return incidentRepository.findByType(type);
    }
}
