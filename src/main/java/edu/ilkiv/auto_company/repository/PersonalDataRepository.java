package edu.ilkiv.auto_company.repository;

import edu.ilkiv.auto_company.model.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalData, String> {
    List<PersonalData> findByFullNameContaining(String name);
    List<PersonalData> findBySex(String sex);
    List<PersonalData> findByDateOfBirthBefore(LocalDate date);
    List<PersonalData> findByHomeAddressContaining(String address);
}