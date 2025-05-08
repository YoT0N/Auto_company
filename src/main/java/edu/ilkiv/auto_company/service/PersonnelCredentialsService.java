package edu.ilkiv.auto_company.service;

import edu.ilkiv.auto_company.dto.PersonnelCredentialsDTO;
import edu.ilkiv.auto_company.mappers.PersonnelCredentialsMapper;
import edu.ilkiv.auto_company.model.PersonnelCredentials;
import edu.ilkiv.auto_company.repository.PersonnelCredentialsRepository;
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
public class PersonnelCredentialsService {

    private final PersonnelCredentialsRepository personnelCredentialsRepository;
    private final PersonnelCredentialsMapper personnelCredentialsMapper;

    @Autowired
    public PersonnelCredentialsService(PersonnelCredentialsRepository personnelCredentialsRepository,
                                       PersonnelCredentialsMapper personnelCredentialsMapper) {
        this.personnelCredentialsRepository = personnelCredentialsRepository;
        this.personnelCredentialsMapper = personnelCredentialsMapper;
    }

    public List<PersonnelCredentialsDTO> getAllPersonnelCredentials() {
        return personnelCredentialsRepository.findAll().stream()
                .map(personnelCredentialsMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<PersonnelCredentialsDTO> getPersonnelCredentialsById(String tabelNumber) {
        return personnelCredentialsRepository.findById(tabelNumber)
                .map(personnelCredentialsMapper::toDto);
    }

    public PersonnelCredentialsDTO savePersonnelCredentials(@Valid PersonnelCredentialsDTO personnelCredentialsDTO) {
        PersonnelCredentials personnelCredentials = personnelCredentialsMapper.toEntity(personnelCredentialsDTO);
        PersonnelCredentials savedPersonnelCredentials = personnelCredentialsRepository.save(personnelCredentials);
        return personnelCredentialsMapper.toDto(savedPersonnelCredentials);
    }

    public void deletePersonnelCredentials(String tabelNumber) {
        personnelCredentialsRepository.deleteById(tabelNumber);
    }

    public List<PersonnelCredentialsDTO> findByPosition(String position) {
        return personnelCredentialsRepository.findByPosition(position).stream()
                .map(personnelCredentialsMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PersonnelCredentialsDTO> findByDateOfEmploymentBefore(LocalDate date) {
        return personnelCredentialsRepository.findByDateOfEmploymentBefore(date).stream()
                .map(personnelCredentialsMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PersonnelCredentialsDTO> findByDateOfEmploymentBetween(LocalDate start, LocalDate end) {
        return personnelCredentialsRepository.findByDateOfEmploymentBetween(start, end).stream()
                .map(personnelCredentialsMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean existsById(String tabelNumber) {
        return personnelCredentialsRepository.existsById(tabelNumber);
    }

    public PersonnelCredentialsDTO updatePersonnelCredentials(String tabelNumber, PersonnelCredentialsDTO personnelCredentialsDTO) {
        if (!personnelCredentialsRepository.existsById(tabelNumber)) {
            throw new RuntimeException("Personnel credentials not found with id: " + tabelNumber);
        }

        // Ensure the DTO has the correct ID before mapping
        if (!tabelNumber.equals(personnelCredentialsDTO.getTabelNumber())) {
            throw new ValidationException("Tabel number in path and request body must match");
        }

        PersonnelCredentials personnelCredentials = personnelCredentialsMapper.toEntity(personnelCredentialsDTO);
        PersonnelCredentials updatedPersonnelCredentials = personnelCredentialsRepository.save(personnelCredentials);
        return personnelCredentialsMapper.toDto(updatedPersonnelCredentials);
    }
}