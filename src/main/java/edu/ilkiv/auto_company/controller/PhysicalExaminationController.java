package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.model.PersonalData;
import edu.ilkiv.auto_company.model.PhysicalExamination;
import edu.ilkiv.auto_company.service.PersonalDataService;
import edu.ilkiv.auto_company.service.PhysicalExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/physical-examinations")
public class PhysicalExaminationController {

    private final PhysicalExaminationService physicalExaminationService;
    private final PersonalDataService personalDataService;

    @Autowired
    public PhysicalExaminationController(PhysicalExaminationService physicalExaminationService, PersonalDataService personalDataService) {
        this.physicalExaminationService = physicalExaminationService;
        this.personalDataService = personalDataService;
    }

    // GET all physical examinations
    @GetMapping
    public ResponseEntity<List<PhysicalExamination>> getAllPhysicalExaminations() {
        List<PhysicalExamination> examinations = physicalExaminationService.getAllPhysicalExaminations();
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }

    // GET physical examination by id
    @GetMapping("/{id}")
    public ResponseEntity<PhysicalExamination> getPhysicalExaminationById(@PathVariable Long id) {
        return physicalExaminationService.getPhysicalExaminationById(id)
                .map(examination -> new ResponseEntity<>(examination, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new physical examination
    @PostMapping
    public ResponseEntity<PhysicalExamination> createPhysicalExamination(@RequestBody PhysicalExamination physicalExamination) {
        PhysicalExamination savedExamination = physicalExaminationService.savePhysicalExamination(physicalExamination);
        return new ResponseEntity<>(savedExamination, HttpStatus.CREATED);
    }

    // PUT update physical examination
    @PutMapping("/{id}")
    public ResponseEntity<PhysicalExamination> updatePhysicalExamination(@PathVariable Long id, @RequestBody PhysicalExamination physicalExamination) {
        if (!physicalExaminationService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        physicalExamination.setIdPhysicalExamination(id); // Ensure the ID matches
        PhysicalExamination updatedExamination = physicalExaminationService.updatePhysicalExamination(id, physicalExamination);
        return new ResponseEntity<>(updatedExamination, HttpStatus.OK);
    }

    // DELETE physical examination
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhysicalExamination(@PathVariable Long id) {
        if (!physicalExaminationService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        physicalExaminationService.deletePhysicalExamination(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET physical examinations by personnel
    @GetMapping("/personnel/{tabelNumber}")
    public ResponseEntity<List<PhysicalExamination>> getPhysicalExaminationsByPersonnel(@PathVariable String tabelNumber) {
        return personalDataService.getPersonalDataById(tabelNumber)
                .map(personnel -> {
                    List<PhysicalExamination> examinations = physicalExaminationService.findByPersonnel(personnel);
                    return new ResponseEntity<>(examinations, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // GET physical examinations by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<PhysicalExamination>> getPhysicalExaminationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PhysicalExamination> examinations = physicalExaminationService.findByDateBetween(startDate, endDate);
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }

    // GET physical examinations by result containing
    @GetMapping("/result")
    public ResponseEntity<List<PhysicalExamination>> getPhysicalExaminationsByResult(@RequestParam String keyword) {
        List<PhysicalExamination> examinations = physicalExaminationService.findByExaminationResultContaining(keyword);
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }
}