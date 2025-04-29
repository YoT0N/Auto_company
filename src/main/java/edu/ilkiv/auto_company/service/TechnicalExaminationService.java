package edu.ilkiv.auto_company.service;


import edu.ilkiv.auto_company.model.Bus;
import edu.ilkiv.auto_company.model.TechnicalExamination;
import edu.ilkiv.auto_company.repository.TechnicalExaminationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TechnicalExaminationService {

    private final TechnicalExaminationRepository technicalExaminationRepository;

    @Autowired
    public TechnicalExaminationService(TechnicalExaminationRepository technicalExaminationRepository) {
        this.technicalExaminationRepository = technicalExaminationRepository;
    }

    public List<TechnicalExamination> getAllTechnicalExaminations() {
        return technicalExaminationRepository.findAll();
    }

    public Optional<TechnicalExamination> getTechnicalExaminationById(Long id) {
        return technicalExaminationRepository.findById(id);
    }

    public TechnicalExamination saveTechnicalExamination(TechnicalExamination technicalExamination) {
        return technicalExaminationRepository.save(technicalExamination);
    }

    public void deleteTechnicalExamination(Long id) {
        technicalExaminationRepository.deleteById(id);
    }

    public List<TechnicalExamination> findByBus(Bus bus) {
        return technicalExaminationRepository.findByBus(bus);
    }

    public List<TechnicalExamination> findByDateBetween(LocalDate start, LocalDate end) {
        return technicalExaminationRepository.findByDateBetween(start, end);
    }

    public List<TechnicalExamination> findBySentForRepair(Boolean sentForRepair) {
        return technicalExaminationRepository.findBySentForRepair(sentForRepair);
    }

    public List<TechnicalExamination> findByRepairPriceGreaterThan(Double price) {
        return technicalExaminationRepository.findByRepairPriceGreaterThan(price);
    }

    public List<TechnicalExamination> findByExaminationResultContaining(String result) {
        return technicalExaminationRepository.findByExaminationResultContaining(result);
    }

    public boolean existsById(Long id) {
        return technicalExaminationRepository.existsById(id);
    }

    public TechnicalExamination updateTechnicalExamination(Long id, TechnicalExamination technicalExaminationDetails) {
        TechnicalExamination technicalExamination = technicalExaminationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technical examination not found with id: " + id));

        technicalExamination.setBus(technicalExaminationDetails.getBus());
        technicalExamination.setDate(technicalExaminationDetails.getDate());
        technicalExamination.setExaminationResult(technicalExaminationDetails.getExaminationResult());
        technicalExamination.setSentForRepair(technicalExaminationDetails.getSentForRepair());
        technicalExamination.setRepairPrice(technicalExaminationDetails.getRepairPrice());

        return technicalExaminationRepository.save(technicalExamination);
    }
}