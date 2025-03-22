package ro.medCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.medCare.model.MedicalService;

@Repository
public interface MedicalServiceRepository extends JpaRepository<MedicalService, Long> {
}