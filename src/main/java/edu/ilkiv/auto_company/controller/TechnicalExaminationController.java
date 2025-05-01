package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.dto.TechnicalExaminationDTO;
import edu.ilkiv.auto_company.service.BusService;
import edu.ilkiv.auto_company.service.TechnicalExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/technical-examinations")
public class TechnicalExaminationController {

    private final TechnicalExaminationService technicalExaminationService;
    private final BusService busService;

    @Autowired
    public TechnicalExaminationController(TechnicalExaminationService technicalExaminationService, BusService busService) {
        this.technicalExaminationService = technicalExaminationService;
        this.busService = busService;
    }

    // GET all technical examinations
    @GetMapping
    public ResponseEntity<List<TechnicalExaminationDTO>> getAllTechnicalExaminations() {
        List<TechnicalExaminationDTO> examinations = technicalExaminationService.getAllTechnicalExaminations();
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }

    // GET technical examination by id
    @GetMapping("/{id}")
    public ResponseEntity<TechnicalExaminationDTO> getTechnicalExaminationById(@PathVariable Long id) {
        return technicalExaminationService.getTechnicalExaminationById(id)
                .map(examination -> new ResponseEntity<>(examination, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new technical examination
    @PostMapping
    public ResponseEntity<TechnicalExaminationDTO> createTechnicalExamination(@RequestBody TechnicalExaminationDTO technicalExaminationDTO) {
        TechnicalExaminationDTO savedExamination = technicalExaminationService.saveTechnicalExamination(technicalExaminationDTO);
        return new ResponseEntity<>(savedExamination, HttpStatus.CREATED);
    }

    // PUT update technical examination
    @PutMapping("/{id}")
    public ResponseEntity<TechnicalExaminationDTO> updateTechnicalExamination(@PathVariable Long id, @RequestBody TechnicalExaminationDTO technicalExaminationDTO) {
        if (!technicalExaminationService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        technicalExaminationDTO.setIdTechnicalExamination(id); // Ensure the ID matches
        TechnicalExaminationDTO updatedExamination = technicalExaminationService.updateTechnicalExamination(id, technicalExaminationDTO);
        return new ResponseEntity<>(updatedExamination, HttpStatus.OK);
    }

    // DELETE technical examination
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnicalExamination(@PathVariable Long id) {
        if (!technicalExaminationService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        technicalExaminationService.deleteTechnicalExamination(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET technical examinations by bus
    @GetMapping("/bus/{countryNumber}")
    public ResponseEntity<List<TechnicalExaminationDTO>> getTechnicalExaminationsByBus(@PathVariable String countryNumber) {
        List<TechnicalExaminationDTO> examinations = technicalExaminationService.findByBusCountryNumber(countryNumber);
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }

    // GET technical examinations by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<TechnicalExaminationDTO>> getTechnicalExaminationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TechnicalExaminationDTO> examinations = technicalExaminationService.findByDateBetween(startDate, endDate);
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }

    // GET technical examinations by sent for repair status
    @GetMapping("/sent-for-repair")
    public ResponseEntity<List<TechnicalExaminationDTO>> getTechnicalExaminationsByRepairStatus(@RequestParam Boolean sentForRepair) {
        List<TechnicalExaminationDTO> examinations = technicalExaminationService.findBySentForRepair(sentForRepair);
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }

    // GET technical examinations by repair price greater than
    @GetMapping("/repair-price")
    public ResponseEntity<List<TechnicalExaminationDTO>> getTechnicalExaminationsByRepairPriceGreaterThan(@RequestParam Double price) {
        List<TechnicalExaminationDTO> examinations = technicalExaminationService.findByRepairPriceGreaterThan(price);
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }

    // GET technical examinations by result containing
    @GetMapping("/result")
    public ResponseEntity<List<TechnicalExaminationDTO>> getTechnicalExaminationsByResultContaining(@RequestParam String keyword) {
        List<TechnicalExaminationDTO> examinations = technicalExaminationService.findByExaminationResultContaining(keyword);
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }
}