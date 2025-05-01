package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.dto.PhysicalExaminationDTO;
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
    public ResponseEntity<List<PhysicalExaminationDTO>> getAllPhysicalExaminations() {
        List<PhysicalExaminationDTO> examinations = physicalExaminationService.getAllPhysicalExaminations();
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }

    // GET physical examination by id
    @GetMapping("/{id}")
    public ResponseEntity<PhysicalExaminationDTO> getPhysicalExaminationById(@PathVariable Long id) {
        return physicalExaminationService.getPhysicalExaminationById(id)
                .map(examination -> new ResponseEntity<>(examination, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new physical examination
    @PostMapping
    public ResponseEntity<PhysicalExaminationDTO> createPhysicalExamination(@RequestBody PhysicalExaminationDTO physicalExaminationDTO) {
        PhysicalExaminationDTO savedExamination = physicalExaminationService.savePhysicalExamination(physicalExaminationDTO);
        return new ResponseEntity<>(savedExamination, HttpStatus.CREATED);
    }

    // PUT update physical examination
    @PutMapping("/{id}")
    public ResponseEntity<PhysicalExaminationDTO> updatePhysicalExamination(@PathVariable Long id, @RequestBody PhysicalExaminationDTO physicalExaminationDTO) {
        if (!physicalExaminationService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        physicalExaminationDTO.setIdPhysicalExamination(id); // Ensure the ID matches
        PhysicalExaminationDTO updatedExamination = physicalExaminationService.updatePhysicalExamination(id, physicalExaminationDTO);
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
    public ResponseEntity<List<PhysicalExaminationDTO>> getPhysicalExaminationsByPersonnel(@PathVariable String tabelNumber) {
        List<PhysicalExaminationDTO> examinations = physicalExaminationService.findByPersonnelTabelNumber(tabelNumber);
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }

    // GET physical examinations by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<PhysicalExaminationDTO>> getPhysicalExaminationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PhysicalExaminationDTO> examinations = physicalExaminationService.findByDateBetween(startDate, endDate);
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }

    // GET physical examinations by result containing
    @GetMapping("/result")
    public ResponseEntity<List<PhysicalExaminationDTO>> getPhysicalExaminationsByResult(@RequestParam String keyword) {
        List<PhysicalExaminationDTO> examinations = physicalExaminationService.findByExaminationResultContaining(keyword);
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }
}