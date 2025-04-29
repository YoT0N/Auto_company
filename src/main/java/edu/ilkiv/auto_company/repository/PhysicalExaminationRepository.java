package edu.ilkiv.auto_company.repository;

import edu.ilkiv.auto_company.model.PersonalData;
import edu.ilkiv.auto_company.model.PhysicalExamination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PhysicalExaminationRepository extends JpaRepository<PhysicalExamination, Long> {
    List<PhysicalExamination> findByPersonnel(PersonalData personnel);
    List<PhysicalExamination> findByDateBetween(LocalDate start, LocalDate end);
    List<PhysicalExamination> findByExaminationResultContaining(String result);
}