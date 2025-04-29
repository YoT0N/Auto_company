package edu.ilkiv.auto_company.service;


import edu.ilkiv.auto_company.model.PersonalData;
import edu.ilkiv.auto_company.model.PhysicalExamination;
import edu.ilkiv.auto_company.repository.PhysicalExaminationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PhysicalExaminationService {

    private final PhysicalExaminationRepository physicalExaminationRepository;

    @Autowired
    public PhysicalExaminationService(PhysicalExaminationRepository physicalExaminationRepository) {
        this.physicalExaminationRepository = physicalExaminationRepository;
    }

    public List<PhysicalExamination> getAllPhysicalExaminations() {
        return physicalExaminationRepository.findAll();
    }

    public Optional<PhysicalExamination> getPhysicalExaminationById(Long id) {
        return physicalExaminationRepository.findById(id);
    }

    public PhysicalExamination savePhysicalExamination(PhysicalExamination physicalExamination) {
        return physicalExaminationRepository.save(physicalExamination);
    }

    public void deletePhysicalExamination(Long id) {
        physicalExaminationRepository.deleteById(id);
    }

    public List<PhysicalExamination> findByPersonnel(PersonalData personnel) {
        return physicalExaminationRepository.findByPersonnel(personnel);
    }

    public List<PhysicalExamination> findByDateBetween(LocalDate start, LocalDate end) {
        return physicalExaminationRepository.findByDateBetween(start, end);
    }

    public List<PhysicalExamination> findByExaminationResultContaining(String result) {
        return physicalExaminationRepository.findByExaminationResultContaining(result);
    }

    public boolean existsById(Long id) {
        return physicalExaminationRepository.existsById(id);
    }

    public PhysicalExamination updatePhysicalExamination(Long id, PhysicalExamination physicalExaminationDetails) {
        PhysicalExamination physicalExamination = physicalExaminationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Physical examination not found with id: " + id));

        physicalExamination.setPersonnel(physicalExaminationDetails.getPersonnel());
        physicalExamination.setDate(physicalExaminationDetails.getDate());
        physicalExamination.setExaminationResult(physicalExaminationDetails.getExaminationResult());

        return physicalExaminationRepository.save(physicalExamination);
    }
}