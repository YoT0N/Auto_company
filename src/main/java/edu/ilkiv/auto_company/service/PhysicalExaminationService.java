package edu.ilkiv.auto_company.service;

import edu.ilkiv.auto_company.dto.PhysicalExaminationDTO;
import edu.ilkiv.auto_company.mappers.PhysicalExaminationMapper;
import edu.ilkiv.auto_company.model.PersonalData;
import edu.ilkiv.auto_company.model.PhysicalExamination;
import edu.ilkiv.auto_company.repository.PersonalDataRepository;
import edu.ilkiv.auto_company.repository.PhysicalExaminationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class PhysicalExaminationService {

    private final PhysicalExaminationRepository physicalExaminationRepository;
    private final PersonalDataRepository personalDataRepository;
    private final PhysicalExaminationMapper physicalExaminationMapper;

    @Autowired
    public PhysicalExaminationService(PhysicalExaminationRepository physicalExaminationRepository,
                                      PersonalDataRepository personalDataRepository,
                                      PhysicalExaminationMapper physicalExaminationMapper) {
        this.physicalExaminationRepository = physicalExaminationRepository;
        this.personalDataRepository = personalDataRepository;
        this.physicalExaminationMapper = physicalExaminationMapper;
    }

    public List<PhysicalExaminationDTO> getAllPhysicalExaminations() {
        return physicalExaminationRepository.findAll().stream()
                .map(physicalExaminationMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<PhysicalExaminationDTO> getPhysicalExaminationById(Long id) {
        return physicalExaminationRepository.findById(id)
                .map(physicalExaminationMapper::toDto);
    }

    public PhysicalExaminationDTO savePhysicalExamination(@Valid PhysicalExaminationDTO physicalExaminationDTO) {
        // Get the associated personnel entity
        PersonalData personnel = personalDataRepository.findById(physicalExaminationDTO.getPersonnelTabelNumber())
                .orElseThrow(() -> new RuntimeException("Personnel not found with tabelNumber: " +
                        physicalExaminationDTO.getPersonnelTabelNumber()));

        PhysicalExamination physicalExamination = physicalExaminationMapper.toEntity(physicalExaminationDTO);
        physicalExamination.setPersonnel(personnel);

        PhysicalExamination savedExamination = physicalExaminationRepository.save(physicalExamination);
        return physicalExaminationMapper.toDto(savedExamination);
    }

    public void deletePhysicalExamination(Long id) {
        physicalExaminationRepository.deleteById(id);
    }

    public List<PhysicalExaminationDTO> findByPersonnelTabelNumber(String tabelNumber) {
        PersonalData personnel = personalDataRepository.findById(tabelNumber)
                .orElseThrow(() -> new RuntimeException("Personnel not found with tabelNumber: " + tabelNumber));

        return physicalExaminationRepository.findByPersonnel(personnel).stream()
                .map(physicalExaminationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PhysicalExaminationDTO> findByDateBetween(LocalDate start, LocalDate end) {
        return physicalExaminationRepository.findByDateBetween(start, end).stream()
                .map(physicalExaminationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PhysicalExaminationDTO> findByExaminationResultContaining(String result) {
        return physicalExaminationRepository.findByExaminationResultContaining(result).stream()
                .map(physicalExaminationMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean existsById(Long id) {
        return physicalExaminationRepository.existsById(id);
    }

    public PhysicalExaminationDTO updatePhysicalExamination(Long id, @Valid PhysicalExaminationDTO physicalExaminationDTO) {
        if (!physicalExaminationRepository.existsById(id)) {
            throw new RuntimeException("Physical examination not found with id: " + id);
        }

        // Set the ID in the DTO
        physicalExaminationDTO.setIdPhysicalExamination(id);

        // Get the associated personnel entity
        PersonalData personnel = personalDataRepository.findById(physicalExaminationDTO.getPersonnelTabelNumber())
                .orElseThrow(() -> new RuntimeException("Personnel not found with tabelNumber: " +
                        physicalExaminationDTO.getPersonnelTabelNumber()));

        PhysicalExamination physicalExamination = physicalExaminationMapper.toEntity(physicalExaminationDTO);
        physicalExamination.setPersonnel(personnel);

        PhysicalExamination updatedExamination = physicalExaminationRepository.save(physicalExamination);
        return physicalExaminationMapper.toDto(updatedExamination);
    }
}