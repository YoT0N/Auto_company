package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.model.PersonnelCredentials;
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
    public ResponseEntity<List<PersonnelCredentials>> getAllCredentials() {
        List<PersonnelCredentials> credentials = personnelCredentialsService.getAllPersonnelCredentials();
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }

    // GET credentials by tabel number
    @GetMapping("/{tabelNumber}")
    public ResponseEntity<PersonnelCredentials> getCredentialsById(@PathVariable String tabelNumber) {
        return personnelCredentialsService.getPersonnelCredentialsById(tabelNumber)
                .map(credentials -> new ResponseEntity<>(credentials, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new credentials
    @PostMapping
    public ResponseEntity<PersonnelCredentials> createCredentials(@RequestBody PersonnelCredentials personnelCredentials) {
        if (personnelCredentialsService.existsById(personnelCredentials.getTabelNumber())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        PersonnelCredentials savedCredentials = personnelCredentialsService.savePersonnelCredentials(personnelCredentials);
        return new ResponseEntity<>(savedCredentials, HttpStatus.CREATED);
    }

    // PUT update credentials
    @PutMapping("/{tabelNumber}")
    public ResponseEntity<PersonnelCredentials> updateCredentials(@PathVariable String tabelNumber, @RequestBody PersonnelCredentials personnelCredentials) {
        if (!personnelCredentialsService.existsById(tabelNumber)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        personnelCredentials.setTabelNumber(tabelNumber); // Ensure the ID matches
        PersonnelCredentials updatedCredentials = personnelCredentialsService.updatePersonnelCredentials(tabelNumber, personnelCredentials);
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
    public ResponseEntity<List<PersonnelCredentials>> getCredentialsByPosition(@PathVariable String position) {
        List<PersonnelCredentials> credentials = personnelCredentialsService.findByPosition(position);
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }

    // GET credentials by employment date before
    @GetMapping("/employed-before")
    public ResponseEntity<List<PersonnelCredentials>> getCredentialsEmployedBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<PersonnelCredentials> credentials = personnelCredentialsService.findByDateOfEmploymentBefore(date);
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }

    // GET credentials by employment date range
    @GetMapping("/employed-between")
    public ResponseEntity<List<PersonnelCredentials>> getCredentialsEmployedBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PersonnelCredentials> credentials = personnelCredentialsService.findByDateOfEmploymentBetween(startDate, endDate);
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }
}