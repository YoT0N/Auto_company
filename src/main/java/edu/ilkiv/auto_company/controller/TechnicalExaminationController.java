package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.model.Bus;
import edu.ilkiv.auto_company.model.TechnicalExamination;
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
    public ResponseEntity<List<TechnicalExamination>> getAllTechnicalExaminations() {
        List<TechnicalExamination> examinations = technicalExaminationService.getAllTechnicalExaminations();
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }

    // GET technical examination by id
    @GetMapping("/{id}")
    public ResponseEntity<TechnicalExamination> getTechnicalExaminationById(@PathVariable Long id) {
        return technicalExaminationService.getTechnicalExaminationById(id)
                .map(examination -> new ResponseEntity<>(examination, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new technical examination
    @PostMapping
    public ResponseEntity<TechnicalExamination> createTechnicalExamination(@RequestBody TechnicalExamination technicalExamination) {
        TechnicalExamination savedExamination = technicalExaminationService.saveTechnicalExamination(technicalExamination);
        return new ResponseEntity<>(savedExamination, HttpStatus.CREATED);
    }

    // PUT update technical examination
    @PutMapping("/{id}")
    public ResponseEntity<TechnicalExamination> updateTechnicalExamination(@PathVariable Long id, @RequestBody TechnicalExamination technicalExamination) {
        if (!technicalExaminationService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        technicalExamination.setIdTechnicalExamination(id); // Ensure the ID matches
        TechnicalExamination updatedExamination = technicalExaminationService.updateTechnicalExamination(id, technicalExamination);
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
    public ResponseEntity<List<TechnicalExamination>> getTechnicalExaminationsByBus(@PathVariable String countryNumber) {
        return busService.getBusById(countryNumber)
                .map(bus -> {
                    List<TechnicalExamination> examinations = technicalExaminationService.findByBus(bus);
                    return new ResponseEntity<>(examinations, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // GET technical examinations by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<TechnicalExamination>> getTechnicalExaminationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TechnicalExamination> examinations = technicalExaminationService.findByDateBetween(startDate, endDate);
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }

    // GET technical examinations by sent for repair status
    @GetMapping("/sent-for-repair")
    public ResponseEntity