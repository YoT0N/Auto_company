package edu.ilkiv.auto_company.service;

import edu.ilkiv.auto_company.dto.BusDTO;
import edu.ilkiv.auto_company.exeptions.ResourceNotFoundException;
import edu.ilkiv.auto_company.mappers.BusMapper;
import edu.ilkiv.auto_company.model.Bus;
import edu.ilkiv.auto_company.repository.BusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.ilkiv.auto_company.exeptions.ValidationException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class BusService {


    private static final Logger logger = LoggerFactory.getLogger(BusService.class);

    private final BusRepository busRepository;
    private final BusMapper busMapper;

    @Autowired
    public BusService(BusRepository busRepository, BusMapper busMapper) {
        this.busRepository = busRepository;
        this.busMapper = busMapper;
    }

    public List<BusDTO> getAllBuses() {
        return busRepository.findAll().stream()
                .map(busMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<BusDTO> getBusById(String countryNumber) {
        return busRepository.findById(countryNumber)
                .map(busMapper::toDto);
    }

    public BusDTO saveBus( BusDTO busDTO) {
        logger.info("Saving new bus with name: {}", busDTO.getBrand());

        Bus bus = busMapper.toEntity(busDTO);
        Bus savedBus = busRepository.save(bus);
        return busMapper.toDto(savedBus);
    }

    public void deleteBus(String countryNumber) {
        busRepository.deleteById(countryNumber);
    }

    public List<BusDTO> findByBrand(String brand) {
        return busRepository.findByBrand(brand).stream()
                .map(busMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BusDTO> findByYearOfManufacture(Integer year) {
        return busRepository.findByYearOfManufacture(year).stream()
                .map(busMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BusDTO> findByDateOfReceiptBetween(LocalDate start, LocalDate end) {
        return busRepository.findByDateOfReceiptBetween(start, end).stream()
                .map(busMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BusDTO> findActiveBuses() {
        return busRepository.findByWriteoffDateIsNull().stream()
                .map(busMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean existsById(String countryNumber) {
        return busRepository.existsById(countryNumber);
    }

    public BusDTO updateBus(String countryNumber, BusDTO busDTO) {
        if (!busRepository.existsById(countryNumber)) {
           throw new ResourceNotFoundException("Bus not found with id", "countryNumber", countryNumber);
           // throw new RuntimeException("Bus not found with id: " + countryNumber);
        }

        // Ensure the DTO has the correct ID before mapping
        if (!countryNumber.equals(busDTO.getCountryNumber())) {
            throw new ValidationException("Country number in path and request body must match");
        }

        Bus bus = busMapper.toEntity(busDTO);
        Bus updatedBus = busRepository.save(bus);
        return busMapper.toDto(updatedBus);
    }
}