package edu.ilkiv.auto_company.service;


import edu.ilkiv.auto_company.model.PersonalData;
import edu.ilkiv.auto_company.repository.PersonalDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PersonalDataService {

    private final PersonalDataRepository personalDataRepository;

    @Autowired
    public PersonalDataService(PersonalDataRepository personalDataRepository) {
        this.personalDataRepository = personalDataRepository;
    }

    public List<PersonalData> getAllPersonalData() {
        return personalDataRepository.findAll();
    }

    public Optional<PersonalData> getPersonalDataById(String tabelNumber) {
        return personalDataRepository.findById(tabelNumber);
    }

    public PersonalData savePersonalData(PersonalData personalData) {
        return personalDataRepository.save(personalData);
    }

    public void deletePersonalData(String tabelNumber) {
        personalDataRepository.deleteById(tabelNumber);
    }

    public List<PersonalData> findByFullNameContaining(String name) {
        return personalDataRepository.findByFullNameContaining(name);
    }

    public List<PersonalData> findBySex(String sex) {
        return personalDataRepository.findBySex(sex);
    }

    public List<PersonalData> findByDateOfBirthBefore(LocalDate date) {
        return personalDataRepository.findByDateOfBirthBefore(date);
    }

    public List<PersonalData> findByHomeAddressContaining(String address) {
        return personalDataRepository.findByHomeAddressContaining(address);
    }

    public boolean existsById(String tabelNumber) {
        return personalDataRepository.existsById(tabelNumber);
    }

    public PersonalData updatePersonalData(String tabelNumber, PersonalData personalDataDetails) {
        PersonalData personalData = personalDataRepository.findById(tabelNumber)
                .orElseThrow(() -> new RuntimeException("Personal data not found with id: " + tabelNumber));

        personalData.setFullName(personalDataDetails.getFullName());
        personalData.setDateOfBirth(personalDataDetails.getDateOfBirth());
        personalData.setSex(personalDataDetails.getSex());
        personalData.setHomeAddress(personalDataDetails.getHomeAddress());
        personalData.setHomePhone(personalDataDetails.getHomePhone());
        personalData.setPhoneNumber(personalDataDetails.getPhoneNumber());

        return personalDataRepository.save(personalData);
    }
}