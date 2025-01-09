package com.example.service;

import com.example.entity.MaintenanceRequest;
import com.example.entity.RequestStatus;
import com.example.repository.MaintenanceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceRequestService {

    @Autowired
    private MaintenanceRequestRepository maintenanceRequestRepository;

    public List<MaintenanceRequest> getAllRequests() {
        return maintenanceRequestRepository.findAll();
    }

    public Optional<MaintenanceRequest> getRequestById(Long id) {
        return maintenanceRequestRepository.findById(id);
    }

    public MaintenanceRequest createRequest(MaintenanceRequest request) {
        return maintenanceRequestRepository.save(request);
    }

    public MaintenanceRequest updateRequest(Long id, MaintenanceRequest updatedRequest) {
        return maintenanceRequestRepository.findById(id).map(request -> {
            request.setTitle(updatedRequest.getTitle());
            request.setDescription(updatedRequest.getDescription());
            request.setStatus(updatedRequest.getStatus());
            request.setTechnician(updatedRequest.getTechnician());
            request.setResolvedDate(updatedRequest.getResolvedDate());
            return maintenanceRequestRepository.save(request);
        }).orElseThrow(() -> new RuntimeException("Request not found"));
    }

    public MaintenanceRequest resolveRequest(Long id) {
        return maintenanceRequestRepository.findById(id).map(request -> {
            request.setStatus(RequestStatus.RESOLVED);
            request.setResolvedDate(LocalDate.now());
            return maintenanceRequestRepository.save(request);
        }).orElseThrow(() -> new RuntimeException("Request not found"));
    }

    public void deleteRequest(Long id) {
        maintenanceRequestRepository.deleteById(id);
    }

    public long getPendingRequestsCount() {
        return maintenanceRequestRepository.countByStatus("PENDING");
    }
}
