package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.model.Bus;
import edu.ilkiv.auto_company.model.PersonalData;
import edu.ilkiv.auto_company.model.Route;
import edu.ilkiv.auto_company.model.RouteSheet;
import edu.ilkiv.auto_company.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/route-sheets")
public class RouteSheetController {

    private final RouteSheetService routeSheetService;
    private final RouteService routeService;
    private final BusService busService;
    private final PersonalDataService personalDataService;

    @Autowired
    public RouteSheetController(
            RouteSheetService routeSheetService,
            RouteService routeService,
            BusService busService,
            PersonalDataService personalDataService) {
        this.routeSheetService = routeSheetService;
        this.routeService = routeService;
        this.busService = busService;
        this.personalDataService = personalDataService;
    }

    // GET all route sheets
    @GetMapping
    public ResponseEntity<List<RouteSheet>> getAllRouteSheets() {
        List<RouteSheet> routeSheets = routeSheetService.getAllRouteSheets();
        return new ResponseEntity<>(routeSheets, HttpStatus.OK);
    }

    // GET route sheet by id
    @GetMapping("/{id}")
    public ResponseEntity<RouteSheet> getRouteSheetById(@PathVariable Long id) {
        return routeSheetService.getRouteSheetById(id)
                .map(routeSheet -> new ResponseEntity<>(routeSheet, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new route sheet
    @PostMapping
    public ResponseEntity<RouteSheet> createRouteSheet(@RequestBody RouteSheet routeSheet) {
        RouteSheet savedRouteSheet = routeSheetService.saveRouteSheet(routeSheet);
        return new ResponseEntity<>(savedRouteSheet, HttpStatus.CREATED);
    }

    // PUT update route sheet
    @PutMapping("/{id}")
    public ResponseEntity<RouteSheet> updateRouteSheet(@PathVariable Long id, @RequestBody RouteSheet routeSheet) {
        if (!routeSheetService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        routeSheet.setIdRouteSheet(id); // Ensure the ID matches
        RouteSheet updatedRouteSheet = routeSheetService.updateRouteSheet(id, routeSheet);
        return new ResponseEntity<>(updatedRouteSheet, HttpStatus.OK);
    }

    // DELETE route sheet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRouteSheet(@PathVariable Long id) {
        if (!routeSheetService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        routeSheetService.deleteRouteSheet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET route sheets by route
    @GetMapping("/route/{routeNumber}")
    public ResponseEntity<List<RouteSheet>> getRouteSheetsByRoute(@PathVariable String routeNumber) {
        return routeService.getRouteById(routeNumber)
                .map(route -> {
                    List<RouteSheet> routeSheets = routeSheetService.findByRoute(route);
                    return new ResponseEntity<>(routeSheets, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // GET route sheets by bus
    @GetMapping("/bus/{countryNumber}")
    public ResponseEntity<List<RouteSheet>> getRouteSheetsByBus(@PathVariable String countryNumber) {
        return busService.getBusById(countryNumber)
                .map(bus -> {
                    List<RouteSheet> routeSheets = routeSheetService.findByBus(bus);
                    return new ResponseEntity<>(routeSheets, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // GET route sheets by driver
    @GetMapping("/driver/{tabelNumber}")
    public ResponseEntity<List<RouteSheet>> getRouteSheetsByDriver(@PathVariable String tabelNumber) {
        return personalDataService.getPersonalDataById(tabelNumber)
                .map(driver -> {
                    List<RouteSheet> routeSheets = routeSheetService.findByDriver(driver);
                    return new ResponseEntity<>(routeSheets, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // GET route sheets by conductor
    @GetMapping("/conductor/{tabelNumber}")
    public ResponseEntity<List<RouteSheet>> getRouteSheetsByConductor(@PathVariable String tabelNumber) {
        return personalDataService.getPersonalDataById(tabelNumber)
                .map(conductor -> {
                    List<RouteSheet> routeSheets = routeSheetService.findByConductor(conductor);
                    return new ResponseEntity<>(routeSheets, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // GET route sheets by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<RouteSheet>> getRouteSheetsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<RouteSheet> routeSheets = routeSheetService.findByDateBetween(startDate, endDate);
        return new ResponseEntity<>(routeSheets, HttpStatus.OK);
    }

    // GET route sheets by total passengers greater than
    @GetMapping("/passengers")
    public ResponseEntity<List<RouteSheet>> getRouteSheetsByPassengersGreaterThan(@RequestParam Integer passengers) {
        List<RouteSheet> routeSheets = routeSheetService.findByTotalPassengersGreaterThan(passengers);
        return new ResponseEntity<>(routeSheets, HttpStatus.OK);
    }
}