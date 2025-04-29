package edu.ilkiv.auto_company.service;


import edu.ilkiv.auto_company.model.Bus;
import edu.ilkiv.auto_company.model.PersonalData;
import edu.ilkiv.auto_company.model.Route;
import edu.ilkiv.auto_company.model.RouteSheet;
import edu.ilkiv.auto_company.repository.RouteSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RouteSheetService {

    private final RouteSheetRepository routeSheetRepository;

    @Autowired
    public RouteSheetService(RouteSheetRepository routeSheetRepository) {
        this.routeSheetRepository = routeSheetRepository;
    }

    public List<RouteSheet> getAllRouteSheets() {
        return routeSheetRepository.findAll();
    }

    public Optional<RouteSheet> getRouteSheetById(Long id) {
        return routeSheetRepository.findById(id);
    }

    public RouteSheet saveRouteSheet(RouteSheet routeSheet) {
        return routeSheetRepository.save(routeSheet);
    }

    public void deleteRouteSheet(Long id) {
        routeSheetRepository.deleteById(id);
    }

    public List<RouteSheet> findByRoute(Route route) {
        return routeSheetRepository.findByRoute(route);
    }

    public List<RouteSheet> findByBus(Bus bus) {
        return routeSheetRepository.findByBus(bus);
    }

    public List<RouteSheet> findByDriver(PersonalData driver) {
        return routeSheetRepository.findByDriver(driver);
    }

    public List<RouteSheet> findByConductor(PersonalData conductor) {
        return routeSheetRepository.findByConductor(conductor);
    }

    public List<RouteSheet> findByDateBetween(LocalDate start, LocalDate end) {
        return routeSheetRepository.findByDateBetween(start, end);
    }

    public List<RouteSheet> findByTotalPassengersGreaterThan(Integer passengers) {
        return routeSheetRepository.findByTotalPassengersGreaterThan(passengers);
    }

    public boolean existsById(Long id) {
        return routeSheetRepository.existsById(id);
    }

    public RouteSheet updateRouteSheet(Long id, RouteSheet routeSheetDetails) {
        RouteSheet routeSheet = routeSheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route sheet not found with id: " + id));

        routeSheet.setRoute(routeSheetDetails.getRoute());
        routeSheet.setBus(routeSheetDetails.getBus());
        routeSheet.setDate(routeSheetDetails.getDate());
        routeSheet.setTrips(routeSheetDetails.getTrips());
        routeSheet.setDriver(routeSheetDetails.getDriver());
        routeSheet.setConductor(routeSheetDetails.getConductor());
        routeSheet.setTotalPassengers(routeSheetDetails.getTotalPassengers());

        return routeSheetRepository.save(routeSheet);
    }
}