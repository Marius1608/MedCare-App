package ro.medCare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.medCare.exception.ResourceNotFoundException;
import ro.medCare.model.MedicalService;
import ro.medCare.repository.MedicalServiceRepository;

import java.util.List;

@Service
public class MedicalServiceService {

    private final MedicalServiceRepository medicalServiceRepository;

    @Autowired
    public MedicalServiceService(MedicalServiceRepository medicalServiceRepository) {
        this.medicalServiceRepository = medicalServiceRepository;
    }

    public MedicalService createMedicalService(MedicalService medicalService) {
        return medicalServiceRepository.save(medicalService);
    }

    public MedicalService updateMedicalService(MedicalService medicalService) {
        if (!medicalServiceRepository.existsById(medicalService.getId())) {
            throw new ResourceNotFoundException("Serviciul medical nu a fost găsit!");
        }
        return medicalServiceRepository.save(medicalService);
    }

    public void deleteMedicalService(Long id) {
        if (!medicalServiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Serviciul medical nu a fost găsit!");
        }
        medicalServiceRepository.deleteById(id);
    }

    public MedicalService getMedicalServiceById(Long id) {
        return medicalServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviciul medical nu a fost găsit!"));
    }

    public List<MedicalService> getAllMedicalServices() {
        return medicalServiceRepository.findAll();
    }
}