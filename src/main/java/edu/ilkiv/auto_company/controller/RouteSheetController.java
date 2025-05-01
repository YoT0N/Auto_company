package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.dto.RouteSheetDTO;
import edu.ilkiv.auto_company.service.BusService;
import edu.ilkiv.auto_company.service.PersonalDataService;
import edu.ilkiv.auto_company.service.RouteService;
import edu.ilkiv.auto_company.service.RouteSheetService;
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
    public ResponseEntity<List<RouteSheetDTO>> getAllRouteSheets() {
        List<RouteSheetDTO> routeSheets = routeSheetService.getAllRouteSheets();
        return new ResponseEntity<>(routeSheets, HttpStatus.OK);
    }

    // GET route sheet by id
    @GetMapping("/{id}")
    public ResponseEntity<RouteSheetDTO> getRouteSheetById(@PathVariable Long id) {
        return routeSheetService.getRouteSheetById(id)
                .map(routeSheet -> new ResponseEntity<>(routeSheet, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new route sheet
    @PostMapping
    public ResponseEntity<RouteSheetDTO> createRouteSheet(@RequestBody RouteSheetDTO routeSheetDTO) {
        RouteSheetDTO savedRouteSheet = routeSheetService.saveRouteSheet(routeSheetDTO);
        return new ResponseEntity<>(savedRouteSheet, HttpStatus.CREATED);
    }

    // PUT update route sheet
    @PutMapping("/{id}")
    public ResponseEntity<RouteSheetDTO> updateRouteSheet(@PathVariable Long id, @RequestBody RouteSheetDTO routeSheetDTO) {
        if (!routeSheetService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        routeSheetDTO.setIdRouteSheet(id); // Ensure the ID matches
        RouteSheetDTO updatedRouteSheet = routeSheetService.updateRouteSheet(id, routeSheetDTO);
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
    public ResponseEntity<List<RouteSheetDTO>> getRouteSheetsByRoute(@PathVariable String routeNumber) {
        List<RouteSheetDTO> routeSheets = routeSheetService.findByRouteNumber(routeNumber);
        return new ResponseEntity<>(routeSheets, HttpStatus.OK);
    }

    // GET route sheets by bus
    @GetMapping("/bus/{countryNumber}")
    public ResponseEntity<List<RouteSheetDTO>> getRouteSheetsByBus(@PathVariable String countryNumber) {
        List<RouteSheetDTO> routeSheets = routeSheetService.findByBusCountryNumber(countryNumber);
        return new ResponseEntity<>(routeSheets, HttpStatus.OK);
    }

    // GET route sheets by driver
    @GetMapping("/driver/{tabelNumber}")
    public ResponseEntity<List<RouteSheetDTO>> getRouteSheetsByDriver(@PathVariable String tabelNumber) {
        List<RouteSheetDTO> routeSheets = routeSheetService.findByDriverTabelNumber(tabelNumber);
        return new ResponseEntity<>(routeSheets, HttpStatus.OK);
    }

    // GET route sheets by conductor
    @GetMapping("/conductor/{tabelNumber}")
    public ResponseEntity<List<RouteSheetDTO>> getRouteSheetsByConductor(@PathVariable String tabelNumber) {
        List<RouteSheetDTO> routeSheets = routeSheetService.findByConductorTabelNumber(tabelNumber);
        return new ResponseEntity<>(routeSheets, HttpStatus.OK);
    }

    // GET route sheets by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<RouteSheetDTO>> getRouteSheetsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<RouteSheetDTO> routeSheets = routeSheetService.findByDateBetween(startDate, endDate);
        return new ResponseEntity<>(routeSheets, HttpStatus.OK);
    }

    // GET route sheets by total passengers greater than
    @GetMapping("/passengers")
    public ResponseEntity<List<RouteSheetDTO>> getRouteSheetsByPassengersGreaterThan(@RequestParam Integer passengers) {
        List<RouteSheetDTO> routeSheets = routeSheetService.findByTotalPassengersGreaterThan(passengers);
        return new ResponseEntity<>(routeSheets, HttpStatus.OK);
    }
}