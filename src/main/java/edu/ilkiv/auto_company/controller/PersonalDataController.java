package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.dto.PersonalDataDTO;
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
    public ResponseEntity<List<PersonalDataDTO>> getAllPersonnel() {
        List<PersonalDataDTO> personnel = personalDataService.getAllPersonalData();
        return new ResponseEntity<>(personnel, HttpStatus.OK);
    }

    // GET personnel by tabel number
    @GetMapping("/{tabelNumber}")
    public ResponseEntity<PersonalDataDTO> getPersonnelById(@PathVariable String tabelNumber) {
        return personalDataService.getPersonalDataById(tabelNumber)
                .map(personnel -> new ResponseEntity<>(personnel, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new personnel
    @PostMapping
    public ResponseEntity<PersonalDataDTO> createPersonnel(@RequestBody PersonalDataDTO personalDataDTO) {
        if (personalDataService.existsById(personalDataDTO.getTabelNumber())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        PersonalDataDTO savedPersonalData = personalDataService.savePersonalData(personalDataDTO);
        return new ResponseEntity<>(savedPersonalData, HttpStatus.CREATED);
    }

    // PUT update personnel
    @PutMapping("/{tabelNumber}")
    public ResponseEntity<PersonalDataDTO> updatePersonnel(@PathVariable String tabelNumber, @RequestBody PersonalDataDTO personalDataDTO) {
        if (!personalDataService.existsById(tabelNumber)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        personalDataDTO.setTabelNumber(tabelNumber); // Ensure the ID matches
        PersonalDataDTO updatedPersonalData = personalDataService.updatePersonalData(tabelNumber, personalDataDTO);
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
    public ResponseEntity<List<PersonalDataDTO>> getPersonnelByName(@PathVariable String name) {
        List<PersonalDataDTO> personnel = personalDataService.findByFullNameContaining(name);
        return new ResponseEntity<>(personnel, HttpStatus.OK);
    }

    // GET personnel by sex
    @GetMapping("/sex/{sex}")
    public ResponseEntity<List<PersonalDataDTO>> getPersonnelBySex(@PathVariable String sex) {
        List<PersonalDataDTO> personnel = personalDataService.findBySex(sex);
        return new ResponseEntity<>(personnel, HttpStatus.OK);
    }

    // GET personnel born before a date
    @GetMapping("/born-before")
    public ResponseEntity<List<PersonalDataDTO>> getPersonnelBornBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<PersonalDataDTO> personnel = personalDataService.findByDateOfBirthBefore(date);
        return new ResponseEntity<>(personnel, HttpStatus.OK);
    }

    // GET personnel by address
    @GetMapping("/address")
    public ResponseEntity<List<PersonalDataDTO>> getPersonnelByAddress(@RequestParam String address) {
        List<PersonalDataDTO> personnel = personalDataService.findByHomeAddressContaining(address);
        return new ResponseEntity<>(personnel, HttpStatus.OK);
    }
}