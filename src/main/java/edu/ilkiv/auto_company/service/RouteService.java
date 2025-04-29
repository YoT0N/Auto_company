package edu.ilkiv.auto_company.service;

import edu.ilkiv.auto_company.model.Route;
import edu.ilkiv.auto_company.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Optional<Route> getRouteById(String routeNumber) {
        return routeRepository.findById(routeNumber);
    }

    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }

    public void deleteRoute(String routeNumber) {
        routeRepository.deleteById(routeNumber);
    }

    public List<Route> findByRouteNameContaining(String keyword) {
        return routeRepository.findByRouteNameContaining(keyword);
    }

    public List<Route> findByRouteLengthGreaterThan(Double length) {
        return routeRepository.findByRouteLengthGreaterThan(length);
    }

    public List<Route> findByAverageTimeLessThan(Integer time) {
        return routeRepository.findByAverageTimeLessThan(time);
    }

    public boolean existsById(String routeNumber) {
        return routeRepository.existsById(routeNumber);
    }

    public Route updateRoute(String routeNumber, Route routeDetails) {
        Route route = routeRepository.findById(routeNumber)
                .orElseThrow(() -> new RuntimeException("Route not found with id: " + routeNumber));

        route.setRouteName(routeDetails.getRouteName());
        route.setRouteLength(routeDetails.getRouteLength());
        route.setAverageTime(routeDetails.getAverageTime());
        route.setPlannedTripsPerShift(routeDetails.getPlannedTripsPerShift());

        return routeRepository.save(route);
    }
}