package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.model.Bus;
import edu.ilkiv.auto_company.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/buses")
public class BusController {

    private final BusService busService;

    @Autowired
    public BusController(BusService busService) {
        this.busService = busService;
    }

    // GET all buses
    @GetMapping
    public ResponseEntity<List<Bus>> getAllBuses() {
        List<Bus> buses = busService.getAllBuses();
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }

    // GET bus by country number
    @GetMapping("/{countryNumber}")
    public ResponseEntity<Bus> getBusById(@PathVariable String countryNumber) {
        return busService.getBusById(countryNumber)
                .map(bus -> new ResponseEntity<>(bus, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new bus
    @PostMapping
    public ResponseEntity<Bus> createBus(@RequestBody Bus bus) {
        if (busService.existsById(bus.getCountryNumber())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Bus savedBus = busService.saveBus(bus);
        return new ResponseEntity<>(savedBus, HttpStatus.CREATED);
    }

    // PUT update bus
    @PutMapping("/{countryNumber}")
    public ResponseEntity<Bus> updateBus(@PathVariable String countryNumber, @RequestBody Bus bus) {
        if (!busService.existsById(countryNumber)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bus.setCountryNumber(countryNumber); // Ensure the ID matches
        Bus updatedBus = busService.updateBus(countryNumber, bus);
        return new ResponseEntity<>(updatedBus, HttpStatus.OK);
    }

    // DELETE bus
    @DeleteMapping("/{countryNumber}")
    public ResponseEntity<Void> deleteBus(@PathVariable String countryNumber) {
        if (!busService.existsById(countryNumber)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        busService.deleteBus(countryNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET buses by brand
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Bus>> getBusesByBrand(@PathVariable String brand) {
        List<Bus> buses = busService.findByBrand(brand);
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }

    // GET buses by year of manufacture
    @GetMapping("/year/{year}")
    public ResponseEntity<List<Bus>> getBusesByYear(@PathVariable Integer year) {
        List<Bus> buses = busService.findByYearOfManufacture(year);
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }

    // GET buses by date of receipt range
    @GetMapping("/receipt-date")
    public ResponseEntity<List<Bus>> getBusesByReceiptDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Bus> buses = busService.findByDateOfReceiptBetween(startDate, endDate);
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }

    // GET active buses (not written off)
    @GetMapping("/active")
    public ResponseEntity<List<Bus>> getActiveBuses() {
        List<Bus> buses = busService.findActiveBuses();
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }
}