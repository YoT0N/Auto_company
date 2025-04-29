package edu.ilkiv.auto_company.repository;

import edu.ilkiv.auto_company.model.Bus;
import edu.ilkiv.auto_company.model.TechnicalExamination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TechnicalExaminationRepository extends JpaRepository<TechnicalExamination, Long> {
    List<TechnicalExamination> findByBus(Bus bus);
    List<TechnicalExamination> findByDateBetween(LocalDate start, LocalDate end);
    List<TechnicalExamination> findBySentForRepair(Boolean sentForRepair);
    List<TechnicalExamination> findByRepairPriceGreaterThan(Double price);
    List<TechnicalExamination> findByExaminationResultContaining(String result);
}