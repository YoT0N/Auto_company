package edu.ilkiv.auto_company.service;

import edu.ilkiv.auto_company.dto.PersonalDataDTO;
import edu.ilkiv.auto_company.mappers.PersonalDataMapper;
import edu.ilkiv.auto_company.model.PersonalData;
import edu.ilkiv.auto_company.repository.PersonalDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class PersonalDataService {

    private final PersonalDataRepository personalDataRepository;
    private final PersonalDataMapper personalDataMapper;

    @Autowired
    public PersonalDataService(PersonalDataRepository personalDataRepository, PersonalDataMapper personalDataMapper) {
        this.personalDataRepository = personalDataRepository;
        this.personalDataMapper = personalDataMapper;
    }

    public List<PersonalDataDTO> getAllPersonalData() {
        return personalDataRepository.findAll().stream()
                .map(personalDataMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<PersonalDataDTO> getPersonalDataById(String tabelNumber) {
        return personalDataRepository.findById(tabelNumber)
                .map(personalDataMapper::toDto);
    }

    public PersonalDataDTO savePersonalData(PersonalDataDTO personalDataDTO) {
        PersonalData personalData = personalDataMapper.toEntity(personalDataDTO);
        PersonalData savedPersonalData = personalDataRepository.save(personalData);
        return personalDataMapper.toDto(savedPersonalData);
    }

    public void deletePersonalData(String tabelNumber) {
        personalDataRepository.deleteById(tabelNumber);
    }

    public List<PersonalDataDTO> findByFullNameContaining(String name) {
        return personalDataRepository.findByFullNameContaining(name).stream()
                .map(personalDataMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PersonalDataDTO> findBySex(String sex) {
        return personalDataRepository.findBySex(sex).stream()
                .map(personalDataMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PersonalDataDTO> findByDateOfBirthBefore(LocalDate date) {
        return personalDataRepository.findByDateOfBirthBefore(date).stream()
                .map(personalDataMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PersonalDataDTO> findByHomeAddressContaining(String address) {
        return personalDataRepository.findByHomeAddressContaining(address).stream()
                .map(personalDataMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean existsById(String tabelNumber) {
        return personalDataRepository.existsById(tabelNumber);
    }

    public PersonalDataDTO updatePersonalData(String tabelNumber, PersonalDataDTO personalDataDTO) {
        if (!personalDataRepository.existsById(tabelNumber)) {
            throw new RuntimeException("Personal data not found with id: " + tabelNumber);
        }

        // Ensure the DTO has the correct ID before mapping
        if (!tabelNumber.equals(personalDataDTO.getTabelNumber())) {
            throw new ValidationException("Tabel number in path and request body must match");
        }

        PersonalData personalData = personalDataMapper.toEntity(personalDataDTO);
        PersonalData updatedPersonalData = personalDataRepository.save(personalData);
        return personalDataMapper.toDto(updatedPersonalData);
    }
}