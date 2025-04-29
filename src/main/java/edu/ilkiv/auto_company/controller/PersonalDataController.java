package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.model.PersonalData;
import edu.ilkiv.auto_company.service.PersonalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/personnel")
public class PersonalDataController {

    private final PersonalDataService personalDataService;

    @Autowired
    public PersonalDataController(PersonalDataService personalDataService) {
        this.personalDataService = personalDataService;
    }

    // GET all personnel
    @GetMapping
    public ResponseEntity<List<PersonalData>> getAllPersonnel() {
        List<PersonalData> personnel = personalDataService.getAllPersonalData();
        return new ResponseEntity<>(personnel, HttpStatus.OK);
    }

    // GET personnel by tabel number
    @GetMapping("/{tabelNumber}")
    public ResponseEntity<PersonalData> getPersonnelById(@PathVariable String tabelNumber) {
        return personalDataService.getPersonalDataById(tabelNumber)
                .map(personnel -> new ResponseEntity<>(personnel, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new personnel
    @PostMapping
    public ResponseEntity<PersonalData> createPersonnel(@RequestBody PersonalData personalData) {
        if (personalDataService.existsById(personalData.getTabelNumber())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        PersonalData savedPersonalData = personalDataService.savePersonalData(personalData);
        return new ResponseEntity<>(savedPersonalData, HttpStatus.CREATED);
    }

    // PUT update personnel
    @PutMapping("/{tabelNumber}")
    public ResponseEntity<PersonalData> updatePersonnel(@PathVariable String tabelNumber, @RequestBody PersonalData personalData) {
        if (!personalDataService.existsById(tabelNumber)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        personalData.setTabelNumber(tabelNumber); // Ensure the ID matches
        PersonalData updatedPersonalData = personalDataService.updatePersonalData(tabelNumber, personalData);
        return new ResponseEntity<>(updatedPersonalData, HttpStatus.OK);
    }

    // DELETE personnel
    @DeleteMapping("/{tabelNumber}")
    public ResponseEntity<Void> deletePersonnel(@PathVariable String tabelNumber) {
        if (!personalDataService.existsById(tabelNumber)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        personalDataService.deletePersonalData(tabelNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET personnel by name
    @GetMapping("/name/{name}")
    public ResponseEntity<List<PersonalData>> getPersonnelByName(@PathVariable String name) {
        List<PersonalData> personnel = personalDataService.findByFullNameContaining(name);
        return new ResponseEntity<>(personnel, HttpStatus.OK);
    }

    // GET personnel by sex
    @GetMapping("/sex/{sex}")
    public ResponseEntity<List<PersonalData>> getPersonnelBySex(@PathVariable String sex) {
        List<PersonalData> personnel = personalDataService.findBySex(sex);
        return new ResponseEntity<>(personnel, HttpStatus.OK);
    }

    // GET personnel born before a date
    @GetMapping("/born-before")
    public ResponseEntity<List<PersonalData>> getPersonnelBornBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<PersonalData> personnel = personalDataService.findByDateOfBirthBefore(date);
        return new ResponseEntity<>(personnel, HttpStatus.OK);
    }

    // GET personnel by address
    @GetMapping("/address")
    public ResponseEntity<List<PersonalData>> getPersonnelByAddress(@RequestParam String address) {
        List<PersonalData> personnel = personalDataService.findByHomeAddressContaining(address);
        return new ResponseEntity<>(personnel, HttpStatus.OK);
    }
}