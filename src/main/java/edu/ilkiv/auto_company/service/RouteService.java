package edu.ilkiv.auto_company.service;

import edu.ilkiv.auto_company.dto.RouteDTO;
import edu.ilkiv.auto_company.mappers.RouteMapper;
import edu.ilkiv.auto_company.model.Route;
import edu.ilkiv.auto_company.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    @Autowired
    public RouteService(RouteRepository routeRepository, RouteMapper routeMapper) {
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
    }

    public List<RouteDTO> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(routeMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<RouteDTO> getRouteById(String routeNumber) {
        return routeRepository.findById(routeNumber)
                .map(routeMapper::toDto);
    }

    public RouteDTO saveRoute(@Valid RouteDTO routeDTO) {
        Route route = routeMapper.toEntity(routeDTO);
        Route savedRoute = routeRepository.save(route);
        return routeMapper.toDto(savedRoute);
    }

    public void deleteRoute(String routeNumber) {
        routeRepository.deleteById(routeNumber);
    }

    public List<RouteDTO> findByRouteNameContaining(String keyword) {
        return routeRepository.findByRouteNameContaining(keyword).stream()
                .map(routeMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RouteDTO> findByRouteLengthGreaterThan(Double length) {
        return routeRepository.findByRouteLengthGreaterThan(length).stream()
                .map(routeMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RouteDTO> findByAverageTimeLessThan(Integer time) {
        return routeRepository.findByAverageTimeLessThan(time).stream()
                .map(routeMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean existsById(String routeNumber) {
        return routeRepository.existsById(routeNumber);
    }

    public RouteDTO updateRoute(String routeNumber, @Valid RouteDTO routeDTO) {
        if (!routeRepository.existsById(routeNumber)) {
            throw new RuntimeException("Route not found with id: " + routeNumber);
        }

        // Ensure the DTO has the correct ID before mapping
        if (!routeNumber.equals(routeDTO.getRouteNumber())) {
            throw new ValidationException("Route number in path and request body must match");
        }

        Route route = routeMapper.toEntity(routeDTO);
        Route updatedRoute = routeRepository.save(route);
        return routeMapper.toDto(updatedRoute);
    }
}