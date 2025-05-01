package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.dto.PersonnelCredentialsDTO;
import edu.ilkiv.auto_company.service.PersonnelCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/credentials")
public class PersonnelCredentialsController {

    private final PersonnelCredentialsService personnelCredentialsService;

    @Autowired
    public PersonnelCredentialsController(PersonnelCredentialsService personnelCredentialsService) {
        this.personnelCredentialsService = personnelCredentialsService;
    }

    // GET all credentials
    @GetMapping
    public ResponseEntity<List<PersonnelCredentialsDTO>> getAllCredentials() {
        List<PersonnelCredentialsDTO> credentials = personnelCredentialsService.getAllPersonnelCredentials();
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }

    // GET credentials by tabel number
    @GetMapping("/{tabelNumber}")
    public ResponseEntity<PersonnelCredentialsDTO> getCredentialsById(@PathVariable String tabelNumber) {
        return personnelCredentialsService.getPersonnelCredentialsById(tabelNumber)
                .map(credentials -> new ResponseEntity<>(credentials, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new credentials
    @PostMapping
    public ResponseEntity<PersonnelCredentialsDTO> createCredentials(@RequestBody PersonnelCredentialsDTO personnelCredentialsDTO) {
        if (personnelCredentialsService.existsById(personnelCredentialsDTO.getTabelNumber())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        PersonnelCredentialsDTO savedCredentials = personnelCredentialsService.savePersonnelCredentials(personnelCredentialsDTO);
        return new ResponseEntity<>(savedCredentials, HttpStatus.CREATED);
    }

    // PUT update credentials
    @PutMapping("/{tabelNumber}")
    public ResponseEntity<PersonnelCredentialsDTO> updateCredentials(@PathVariable String tabelNumber, @RequestBody PersonnelCredentialsDTO personnelCredentialsDTO) {
        if (!personnelCredentialsService.existsById(tabelNumber)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        personnelCredentialsDTO.setTabelNumber(tabelNumber); // Ensure the ID matches
        PersonnelCredentialsDTO updatedCredentials = personnelCredentialsService.updatePersonnelCredentials(tabelNumber, personnelCredentialsDTO);
        return new ResponseEntity<>(updatedCredentials, HttpStatus.OK);
    }

    // DELETE credentials
    @DeleteMapping("/{tabelNumber}")
    public ResponseEntity<Void> deleteCredentials(@PathVariable String tabelNumber) {
        if (!personnelCredentialsService.existsById(tabelNumber)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        personnelCredentialsService.deletePersonnelCredentials(tabelNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET credentials by position
    @GetMapping("/position/{position}")
    public ResponseEntity<List<PersonnelCredentialsDTO>> getCredentialsByPosition(@PathVariable String position) {
        List<PersonnelCredentialsDTO> credentials = personnelCredentialsService.findByPosition(position);
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }

    // GET credentials by employment date before
    @GetMapping("/employed-before")
    public ResponseEntity<List<PersonnelCredentialsDTO>> getCredentialsEmployedBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<PersonnelCredentialsDTO> credentials = personnelCredentialsService.findByDateOfEmploymentBefore(date);
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }

    // GET credentials by employment date range
    @GetMapping("/employed-between")
    public ResponseEntity<List<PersonnelCredentialsDTO>> getCredentialsEmployedBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PersonnelCredentialsDTO> credentials = personnelCredentialsService.findByDateOfEmploymentBetween(startDate, endDate);
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }
}