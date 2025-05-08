package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.dto.BusDTO;
import edu.ilkiv.auto_company.service.BusService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<BusDTO>> getAllBuses() {
        List<BusDTO> buses = busService.getAllBuses();
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }

    // GET bus by country number
    @GetMapping("/{countryNumber}")
    public ResponseEntity<BusDTO> getBusById(@PathVariable String countryNumber) {
        return busService.getBusById(countryNumber)
                .map(bus -> new ResponseEntity<>(bus, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new bus
    @PostMapping
    public ResponseEntity<BusDTO> createBus(@Valid @RequestBody BusDTO busDTO) {
        if (busService.existsById(busDTO.getCountryNumber())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        BusDTO savedBus = busService.saveBus(busDTO);
        return new ResponseEntity<>(savedBus, HttpStatus.CREATED);
    }

    // PUT update bus
    @PutMapping("/{countryNumber}")
    public ResponseEntity<BusDTO> updateBus(@PathVariable String countryNumber,@Valid @RequestBody BusDTO busDTO) {
        if (!busService.existsById(countryNumber)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        busDTO.setCountryNumber(countryNumber); // Ensure the ID matches
        BusDTO updatedBus = busService.updateBus(countryNumber, busDTO);
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
    public ResponseEntity<List<BusDTO>> getBusesByBrand(@PathVariable String brand) {
        List<BusDTO> buses = busService.findByBrand(brand);
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }

    // GET buses by year of manufacture
    @GetMapping("/year/{year}")
    public ResponseEntity<List<BusDTO>> getBusesByYear(@PathVariable Integer year) {
        List<BusDTO> buses = busService.findByYearOfManufacture(year);
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }

    // GET buses by date of receipt range
    @GetMapping("/receipt-date")
    public ResponseEntity<List<BusDTO>> getBusesByReceiptDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<BusDTO> buses = busService.findByDateOfReceiptBetween(startDate, endDate);
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }

    // GET active buses (not written off)
    @GetMapping("/active")
    public ResponseEntity<List<BusDTO>> getActiveBuses() {
        List<BusDTO> buses = busService.findActiveBuses();
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }
}