package edu.ilkiv.auto_company.service;

import edu.ilkiv.auto_company.dto.TechnicalExaminationDTO;
import edu.ilkiv.auto_company.mappers.TechnicalExaminationMapper;
import edu.ilkiv.auto_company.model.Bus;
import edu.ilkiv.auto_company.model.TechnicalExamination;
import edu.ilkiv.auto_company.repository.BusRepository;
import edu.ilkiv.auto_company.repository.TechnicalExaminationRepository;
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
public class TechnicalExaminationService {

    private final TechnicalExaminationRepository technicalExaminationRepository;
    private final BusRepository busRepository;
    private final TechnicalExaminationMapper technicalExaminationMapper;

    @Autowired
    public TechnicalExaminationService(TechnicalExaminationRepository technicalExaminationRepository,
                                       BusRepository busRepository,
                                       TechnicalExaminationMapper technicalExaminationMapper) {
        this.technicalExaminationRepository = technicalExaminationRepository;
        this.busRepository = busRepository;
        this.technicalExaminationMapper = technicalExaminationMapper;
    }

    public List<TechnicalExaminationDTO> getAllTechnicalExaminations() {
        return technicalExaminationRepository.findAll().stream()
                .map(technicalExaminationMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<TechnicalExaminationDTO> getTechnicalExaminationById(Long id) {
        return technicalExaminationRepository.findById(id)
                .map(technicalExaminationMapper::toDto);
    }

    public TechnicalExaminationDTO saveTechnicalExamination(@Valid TechnicalExaminationDTO technicalExaminationDTO) {
        // Get the associated bus entity
        Bus bus = busRepository.findById(technicalExaminationDTO.getBusCountryNumber())
                .orElseThrow(() -> new RuntimeException("Bus not found with country number: " +
                        technicalExaminationDTO.getBusCountryNumber()));

        TechnicalExamination technicalExamination = technicalExaminationMapper.toEntity(technicalExaminationDTO);
        technicalExamination.setBus(bus);

        TechnicalExamination savedExamination = technicalExaminationRepository.save(technicalExamination);
        return technicalExaminationMapper.toDto(savedExamination);
    }

    public void deleteTechnicalExamination(Long id) {
        technicalExaminationRepository.deleteById(id);
    }

    public List<TechnicalExaminationDTO> findByBusCountryNumber(String countryNumber) {
        Bus bus = busRepository.findById(countryNumber)
                .orElseThrow(() -> new RuntimeException("Bus not found with country number: " + countryNumber));

        return technicalExaminationRepository.findByBus(bus).stream()
                .map(technicalExaminationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TechnicalExaminationDTO> findByDateBetween(LocalDate start, LocalDate end) {
        return technicalExaminationRepository.findByDateBetween(start, end).stream()
                .map(technicalExaminationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TechnicalExaminationDTO> findBySentForRepair(Boolean sentForRepair) {
        return technicalExaminationRepository.findBySentForRepair(sentForRepair).stream()
                .map(technicalExaminationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TechnicalExaminationDTO> findByRepairPriceGreaterThan(Double price) {
        return technicalExaminationRepository.findByRepairPriceGreaterThan(price).stream()
                .map(technicalExaminationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TechnicalExaminationDTO> findByExaminationResultContaining(String result) {
        return technicalExaminationRepository.findByExaminationResultContaining(result).stream()
                .map(technicalExaminationMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean existsById(Long id) {
        return technicalExaminationRepository.existsById(id);
    }

    public TechnicalExaminationDTO updateTechnicalExamination(Long id, @Valid TechnicalExaminationDTO technicalExaminationDTO) {
        if (!technicalExaminationRepository.existsById(id)) {
            throw new RuntimeException("Technical examination not found with id: " + id);
        }

        // Set the ID in the DTO
        technicalExaminationDTO.setIdTechnicalExamination(id);

        // Get the associated bus entity
        Bus bus = busRepository.findById(technicalExaminationDTO.getBusCountryNumber())
                .orElseThrow(() -> new RuntimeException("Bus not found with country number: " +
                        technicalExaminationDTO.getBusCountryNumber()));

        TechnicalExamination technicalExamination = technicalExaminationMapper.toEntity(technicalExaminationDTO);
        technicalExamination.setBus(bus);

        TechnicalExamination updatedExamination = technicalExaminationRepository.save(technicalExamination);
        return technicalExaminationMapper.toDto(updatedExamination);
    }
}