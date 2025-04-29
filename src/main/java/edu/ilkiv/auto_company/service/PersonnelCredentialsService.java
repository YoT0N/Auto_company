package edu.ilkiv.auto_company.service;


import edu.ilkiv.auto_company.model.PersonnelCredentials;
import edu.ilkiv.auto_company.repository.PersonnelCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PersonnelCredentialsService {

    private final PersonnelCredentialsRepository personnelCredentialsRepository;

    @Autowired
    public PersonnelCredentialsService(PersonnelCredentialsRepository personnelCredentialsRepository) {
        this.personnelCredentialsRepository = personnelCredentialsRepository;
    }

    public List<PersonnelCredentials> getAllPersonnelCredentials() {
        return personnelCredentialsRepository.findAll();
    }

    public Optional<PersonnelCredentials> getPersonnelCredentialsById(String tabelNumber) {
        return personnelCredentialsRepository.findById(tabelNumber);
    }

    public PersonnelCredentials savePersonnelCredentials(PersonnelCredentials personnelCredentials) {
        return personnelCredentialsRepository.save(personnelCredentials);
    }

    public void deletePersonnelCredentials(String tabelNumber) {
        personnelCredentialsRepository.deleteById(tabelNumber);
    }

    public List<PersonnelCredentials> findByPosition(String position) {
        return personnelCredentialsRepository.findByPosition(position);
    }

    public List<PersonnelCredentials> findByDateOfEmploymentBefore(LocalDate date) {
        return personnelCredentialsRepository.findByDateOfEmploymentBefore(date);
    }

    public List<PersonnelCredentials> findByDateOfEmploymentBetween(LocalDate start, LocalDate end) {
        return personnelCredentialsRepository.findByDateOfEmploymentBetween(start, end);
    }

    public boolean existsById(String tabelNumber) {
        return personnelCredentialsRepository.existsById(tabelNumber);
    }

    public PersonnelCredentials updatePersonnelCredentials(String tabelNumber, PersonnelCredentials personnelCredentialsDetails) {
        PersonnelCredentials personnelCredentials = personnelCredentialsRepository.findById(tabelNumber)
                .orElseThrow(() -> new RuntimeException("Personnel credentials not found with id: " + tabelNumber));

        personnelCredentials.setPosition(personnelCredentialsDetails.getPosition());
        personnelCredentials.setDateOfEmployment(personnelCredentialsDetails.getDateOfEmployment());

        return personnelCredentialsRepository.save(personnelCredentials);
    }
}
