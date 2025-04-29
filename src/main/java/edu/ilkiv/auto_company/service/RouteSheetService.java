package edu.ilkiv.auto_company.service;

import edu.ilkiv.auto_company.dto.RouteSheetDTO;
import edu.ilkiv.auto_company.mappers.RouteSheetMapper;
import edu.ilkiv.auto_company.model.Bus;
import edu.ilkiv.auto_company.model.PersonalData;
import edu.ilkiv.auto_company.model.Route;
import edu.ilkiv.auto_company.model.RouteSheet;
import edu.ilkiv.auto_company.repository.BusRepository;
import edu.ilkiv.auto_company.repository.PersonalDataRepository;
import edu.ilkiv.auto_company.repository.RouteRepository;
import edu.ilkiv.auto_company.repository.RouteSheetRepository;
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
public class RouteSheetService {

    private final RouteSheetRepository routeSheetRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;
    private final PersonalDataRepository personalDataRepository;
    private final RouteSheetMapper routeSheetMapper;

    @Autowired
    public RouteSheetService(RouteSheetRepository routeSheetRepository,
                             RouteRepository routeRepository,
                             BusRepository busRepository,
                             PersonalDataRepository personalDataRepository,
                             RouteSheetMapper routeSheetMapper) {
        this.routeSheetRepository = routeSheetRepository;
        this.routeRepository = routeRepository;
        this.busRepository = busRepository;
        this.personalDataRepository = personalDataRepository;
        this.routeSheetMapper = routeSheetMapper;
    }

    public List<RouteSheetDTO> getAllRouteSheets() {
        return routeSheetRepository.findAll().stream()
                .map(routeSheetMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<RouteSheetDTO> getRouteSheetById(Long id) {
        return routeSheetRepository.findById(id)
                .map(routeSheetMapper::toDto);
    }

    public RouteSheetDTO saveRouteSheet(@Valid RouteSheetDTO routeSheetDTO) {
        RouteSheet routeSheet = routeSheetMapper.toEntity(routeSheetDTO);

        // Set relationships from DTO IDs
        setRelationshipsFromDTO(routeSheet, routeSheetDTO);

        RouteSheet savedRouteSheet = routeSheetRepository.save(routeSheet);
        return routeSheetMapper.toDto(savedRouteSheet);
    }

    public void deleteRouteSheet(Long id) {
        routeSheetRepository.deleteById(id);
    }

    public List<RouteSheetDTO> findByRouteNumber(String routeNumber) {
        Route route = routeRepository.findById(routeNumber)
                .orElseThrow(() -> new RuntimeException("Route not found with number: " + routeNumber));

        return routeSheetRepository.findByRoute(route).stream()
                .map(routeSheetMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RouteSheetDTO> findByBusCountryNumber(String countryNumber) {
        Bus bus = busRepository.findById(countryNumber)
                .orElseThrow(() -> new RuntimeException("Bus not found with country number: " + countryNumber));

        return routeSheetRepository.findByBus(bus).stream()
                .map(routeSheetMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RouteSheetDTO> findByDriverTabelNumber(String tabelNumber) {
        PersonalData driver = personalDataRepository.findById(tabelNumber)
                .orElseThrow(() -> new RuntimeException("Driver not found with tabel number: " + tabelNumber));

        return routeSheetRepository.findByDriver(driver).stream()
                .map(routeSheetMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RouteSheetDTO> findByConductorTabelNumber(String tabelNumber) {
        PersonalData conductor = personalDataRepository.findById(tabelNumber)
                .orElseThrow(() -> new RuntimeException("Conductor not found with tabel number: " + tabelNumber));

        return routeSheetRepository.findByConductor(conductor).stream()
                .map(routeSheetMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RouteSheetDTO> findByDateBetween(LocalDate start, LocalDate end) {
        return routeSheetRepository.findByDateBetween(start, end).stream()
                .map(routeSheetMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RouteSheetDTO> findByTotalPassengersGreaterThan(Integer passengers) {
        return routeSheetRepository.findByTotalPassengersGreaterThan(passengers).stream()
                .map(routeSheetMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean existsById(Long id) {
        return routeSheetRepository.existsById(id);
    }

    public RouteSheetDTO updateRouteSheet(Long id, @Valid RouteSheetDTO routeSheetDTO) {
        if (!routeSheetRepository.existsById(id)) {
            throw new RuntimeException("Route sheet not found with id: " + id);
        }

        // Set the ID to ensure we're updating the correct entity
        routeSheetDTO.setIdRouteSheet(id);

        RouteSheet routeSheet = routeSheetMapper.toEntity(routeSheetDTO);

        // Set relationships from DTO IDs
        setRelationshipsFromDTO(routeSheet, routeSheetDTO);

        RouteSheet updatedRouteSheet = routeSheetRepository.save(routeSheet);
        return routeSheetMapper.toDto(updatedRouteSheet);
    }

    private void setRelationshipsFromDTO(RouteSheet routeSheet, RouteSheetDTO dto) {
        // Set the route if routeNumber is provided
        if (dto.getRouteNumber() != null) {
            Route route = routeRepository.findById(dto.getRouteNumber())
                    .orElseThrow(() -> new RuntimeException("Route not found with number: " + dto.getRouteNumber()));
            routeSheet.setRoute(route);
        }

        // Set the bus if busCountryNumber is provided
        if (dto.getBusCountryNumber() != null) {
            Bus bus = busRepository.findById(dto.getBusCountryNumber())
                    .orElseThrow(() -> new RuntimeException("Bus not found with country number: " + dto.getBusCountryNumber()));
            routeSheet.setBus(bus);
        }

        // Set the driver if driverTabelNumber is provided
        if (dto.getDriverTabelNumber() != null) {
            PersonalData driver = personalDataRepository.findById(dto.getDriverTabelNumber())
                    .orElseThrow(() -> new RuntimeException("Driver not found with tabel number: " + dto.getDriverTabelNumber()));
            routeSheet.setDriver(driver);
        }

        // Set the conductor if conductorTabelNumber is provided
        if (dto.getConductorTabelNumber() != null) {
            PersonalData conductor = personalDataRepository.findById(dto.getConductorTabelNumber())
                    .orElseThrow(() -> new RuntimeException("Conductor not found with tabel number: " + dto.getConductorTabelNumber()));
            routeSheet.setConductor(conductor);
        }
    }
}