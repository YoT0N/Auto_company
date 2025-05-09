package edu.ilkiv.auto_company.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.ilkiv.auto_company.dto.PersonalDataDTO;
import edu.ilkiv.auto_company.service.PersonalDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonalDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PersonalDataService personalDataService;

    @InjectMocks
    private PersonalDataController personalDataController;

    private ObjectMapper objectMapper;
    private PersonalDataDTO testPerson1;
    private PersonalDataDTO testPerson2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Ручне налаштування MockMvc з нашим контролером та мок-сервісом
        mockMvc = MockMvcBuilders.standaloneSetup(personalDataController).build();

        // Ініціалізація тестових об'єктів PersonalDataDTO
        testPerson1 = new PersonalDataDTO();
        testPerson1.setTabelNumber("T001");
        testPerson1.setFullName("Ivan Petrenko");
        testPerson1.setDateOfBirth(LocalDate.of(1985, 5, 15));
        testPerson1.setSex("Male");
        testPerson1.setHomeAddress("Lviv, Horodotska St, 10");
        testPerson1.setHomePhone("0322345678");
        testPerson1.setPhoneNumber("0681234567");

        testPerson2 = new PersonalDataDTO();
        testPerson2.setTabelNumber("T002");
        testPerson2.setFullName("Olena Kovalenko");
        testPerson2.setDateOfBirth(LocalDate.of(1990, 10, 25));
        testPerson2.setSex("Female");
        testPerson2.setHomeAddress("Lviv, Shevchenka St, 5");
        testPerson2.setHomePhone("0322987654");
        testPerson2.setPhoneNumber("0671234567");
    }

    @Test
    void getAllPersonnel_ShouldReturnAllPersonnel() throws Exception {
        // Given
        List<PersonalDataDTO> personnel = Arrays.asList(testPerson1, testPerson2);
        when(personalDataService.getAllPersonalData()).thenReturn(personnel);

        // When & Then
        mockMvc.perform(get("/api/personnel"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tabelNumber", is("T001")))
                .andExpect(jsonPath("$[0].fullName", is("Ivan Petrenko")))
                .andExpect(jsonPath("$[1].tabelNumber", is("T002")))
                .andExpect(jsonPath("$[1].fullName", is("Olena Kovalenko")));

        verify(personalDataService, times(1)).getAllPersonalData();
    }

    @Test
    void getPersonnelById_WhenExists_ShouldReturnPersonnel() throws Exception {
        // Given
        when(personalDataService.getPersonalDataById("T001")).thenReturn(Optional.of(testPerson1));

        // When & Then
        mockMvc.perform(get("/api/personnel/T001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tabelNumber", is("T001")))
                .andExpect(jsonPath("$.fullName", is("Ivan Petrenko")))
                .andExpect(jsonPath("$.sex", is("Male")));

        verify(personalDataService, times(1)).getPersonalDataById("T001");
    }

    @Test
    void getPersonnelById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        when(personalDataService.getPersonalDataById("NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/personnel/NONEXISTENT"))
                .andExpect(status().isNotFound());

        verify(personalDataService, times(1)).getPersonalDataById("NONEXISTENT");
    }



    @Test
    void updatePersonnel_WhenExists_ShouldUpdateAndReturnPersonnel() throws Exception {
        // Given
        String tabelNumber = "T001";
        PersonalDataDTO updatedPerson = new PersonalDataDTO();
        updatedPerson.setTabelNumber(tabelNumber);
        updatedPerson.setFullName("Ivan Petrenko-Updated");
        updatedPerson.setDateOfBirth(LocalDate.of(1985, 5, 15));
        updatedPerson.setSex("Male");
        updatedPerson.setHomeAddress("Lviv, Horodotska St, 15");
        updatedPerson.setHomePhone("0322345678");
        updatedPerson.setPhoneNumber("0689876543");

        when(personalDataService.existsById(tabelNumber)).thenReturn(true);
        when(personalDataService.updatePersonalData(eq(tabelNumber), any(PersonalDataDTO.class))).thenReturn(updatedPerson);

        // When & Then
        mockMvc.perform(put("/api/personnel/{tabelNumber}", tabelNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tabelNumber", is(tabelNumber)))
                .andExpect(jsonPath("$.fullName", is("Ivan Petrenko-Updated")))
                .andExpect(jsonPath("$.homeAddress", is("Lviv, Horodotska St, 15")))
                .andExpect(jsonPath("$.phoneNumber", is("0689876543")));

        verify(personalDataService, times(1)).existsById(tabelNumber);
        verify(personalDataService, times(1)).updatePersonalData(eq(tabelNumber), any(PersonalDataDTO.class));
    }

    @Test
    void updatePersonnel_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        String tabelNumber = "NONEXISTENT";
        when(personalDataService.existsById(tabelNumber)).thenReturn(false);

        // When & Then
        mockMvc.perform(put("/api/personnel/{tabelNumber}", tabelNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPerson1)))
                .andExpect(status().isNotFound());

        verify(personalDataService, times(1)).existsById(tabelNumber);
        verify(personalDataService, never()).updatePersonalData(anyString(), any(PersonalDataDTO.class));
    }

    @Test
    void deletePersonnel_WhenExists_ShouldReturnNoContent() throws Exception {
        // Given
        String tabelNumber = "T001";
        when(personalDataService.existsById(tabelNumber)).thenReturn(true);
        doNothing().when(personalDataService).deletePersonalData(tabelNumber);

        // When & Then
        mockMvc.perform(delete("/api/personnel/{tabelNumber}", tabelNumber))
                .andExpect(status().isNoContent());

        verify(personalDataService, times(1)).existsById(tabelNumber);
        verify(personalDataService, times(1)).deletePersonalData(tabelNumber);
    }

    @Test
    void deletePersonnel_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        String tabelNumber = "NONEXISTENT";
        when(personalDataService.existsById(tabelNumber)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/personnel/{tabelNumber}", tabelNumber))
                .andExpect(status().isNotFound());

        verify(personalDataService, times(1)).existsById(tabelNumber);
        verify(personalDataService, never()).deletePersonalData(tabelNumber);
    }

    @Test
    void getPersonnelByName_ShouldReturnPersonnelByName() throws Exception {
        // Given
        String name = "Ivan";
        List<PersonalDataDTO> personnel = List.of(testPerson1);
        when(personalDataService.findByFullNameContaining(name)).thenReturn(personnel);

        // When & Then
        mockMvc.perform(get("/api/personnel/name/{name}", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tabelNumber", is("T001")))
                .andExpect(jsonPath("$[0].fullName", is("Ivan Petrenko")));

        verify(personalDataService, times(1)).findByFullNameContaining(name);
    }

    @Test
    void getPersonnelBySex_ShouldReturnPersonnelBySex() throws Exception {
        // Given
        String sex = "Female";
        List<PersonalDataDTO> personnel = List.of(testPerson2);
        when(personalDataService.findBySex(sex)).thenReturn(personnel);

        // When & Then
        mockMvc.perform(get("/api/personnel/sex/{sex}", sex))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tabelNumber", is("T002")))
                .andExpect(jsonPath("$[0].sex", is("Female")));

        verify(personalDataService, times(1)).findBySex(sex);
    }


    @Test
    void getPersonnelByAddress_ShouldReturnPersonnelByAddress() throws Exception {
        // Given
        String address = "Lviv";
        List<PersonalDataDTO> personnel = Arrays.asList(testPerson1, testPerson2);
        when(personalDataService.findByHomeAddressContaining(address)).thenReturn(personnel);

        // When & Then
        mockMvc.perform(get("/api/personnel/address")
                        .param("address", address))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tabelNumber", is("T001")))
                .andExpect(jsonPath("$[1].tabelNumber", is("T002")))
                .andExpect(jsonPath("$[*].homeAddress", everyItem(containsString("Lviv"))));

        verify(personalDataService, times(1)).findByHomeAddressContaining(address);
    }
}