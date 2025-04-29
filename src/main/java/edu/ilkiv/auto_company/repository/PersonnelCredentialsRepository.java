package edu.ilkiv.auto_company.repository;

import edu.ilkiv.auto_company.model.PersonnelCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonnelCredentialsRepository extends JpaRepository<PersonnelCredentials, String> {
    List<PersonnelCredentials> findByPosition(String position);
    List<PersonnelCredentials> findByDateOfEmploymentBefore(LocalDate date);
    List<PersonnelCredentials> findByDateOfEmploymentBetween(LocalDate start, LocalDate end);
}