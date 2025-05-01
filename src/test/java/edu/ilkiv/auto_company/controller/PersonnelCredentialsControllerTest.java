package edu.ilkiv.auto_company.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.ilkiv.auto_company.dto.PersonnelCredentialsDTO;
import edu.ilkiv.auto_company.model.PersonalData;
import edu.ilkiv.auto_company.model.PersonnelCredentials;
import edu.ilkiv.auto_company.service.PersonnelCredentialsService;
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
public class PersonnelCredentialsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PersonnelCredentialsService personnelCredentialsService;

    @InjectMocks
    private PersonnelCredentialsController personnelCredentialsController;

    private ObjectMapper objectMapper;
    private PersonnelCredentialsDTO testCredentials1;
    private PersonnelCredentialsDTO testCredentials2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Ручне налаштування MockMvc з нашим контролером та мок-сервісом
        mockMvc = MockMvcBuilders.standaloneSetup(personnelCredentialsController).build();

        // Ініціалізація тестових об'єктів PersonnelCredentialsDTO
        testCredentials1 = new PersonnelCredentialsDTO();
        testCredentials1.setTabelNumber("T12345");
        testCredentials1.setPosition("Driver");
        testCredentials1.setDateOfEmployment(LocalDate.of(2020, 5, 15));
        // Note: We don't need to set personalData since it's handled by the mapper in actual service

        testCredentials2 = new PersonnelCredentialsDTO();
        testCredentials2.setTabelNumber("T67890");
        testCredentials2.setPosition("Mechanic");
        testCredentials2.setDateOfEmployment(LocalDate.of(2018, 3, 10));
    }

    @Test
    void getAllCredentials_ShouldReturnAllCredentials() throws Exception {
        // Given
        List<PersonnelCredentialsDTO> credentials = Arrays.asList(testCredentials1, testCredentials2);
        when(personnelCredentialsService.getAllPersonnelCredentials()).thenReturn(credentials);

        // When & Then
        mockMvc.perform(get("/api/credentials"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tabelNumber", is("T12345")))
                .andExpect(jsonPath("$[0].position", is("Driver")))
                .andExpect(jsonPath("$[1].tabelNumber", is("T67890")))
                .andExpect(jsonPath("$[1].position", is("Mechanic")));

        verify(personnelCredentialsService, times(1)).getAllPersonnelCredentials();
    }


    @Test
    void getCredentialsById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        when(personnelCredentialsService.getPersonnelCredentialsById("NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/credentials/NONEXISTENT"))
                .andExpect(status().isNotFound());

        verify(personnelCredentialsService, times(1)).getPersonnelCredentialsById("NONEXISTENT");
    }

    @Test
    void createCredentials_WithValidData_ShouldCreateAndReturnCredentials() throws Exception {
        // Given
        when(personnelCredentialsService.existsById(anyString())).thenReturn(false);
        when(personnelCredentialsService.savePersonnelCredentials(any(PersonnelCredentialsDTO.class))).thenReturn(testCredentials1);

        // When & Then
        mockMvc.perform(post("/api/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCredentials1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tabelNumber", is("T12345")))
                .andExpect(jsonPath("$.position", is("Driver")));

        verify(personnelCredentialsService, times(1)).existsById(testCredentials1.getTabelNumber());
        verify(personnelCredentialsService, times(1)).savePersonnelCredentials(any(PersonnelCredentialsDTO.class));
    }

    @Test
    void createCredentials_WithExistingId_ShouldReturnConflict() throws Exception {
        // Given
        when(personnelCredentialsService.existsById(testCredentials1.getTabelNumber())).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCredentials1)))
                .andExpect(status().isConflict());

        verify(personnelCredentialsService, times(1)).existsById(testCredentials1.getTabelNumber());
        verify(personnelCredentialsService, never()).savePersonnelCredentials(any(PersonnelCredentialsDTO.class));
    }

    @Test
    void updateCredentials_WhenExists_ShouldUpdateAndReturnCredentials() throws Exception {
        // Given
        String tabelNumber = "T12345";
        PersonnelCredentialsDTO updatedCredentials = new PersonnelCredentialsDTO();
        updatedCredentials.setTabelNumber(tabelNumber);
        updatedCredentials.setPosition("Senior Driver");
        updatedCredentials.setDateOfEmployment(LocalDate.of(2020, 5, 15));

        when(personnelCredentialsService.existsById(tabelNumber)).thenReturn(true);
        when(personnelCredentialsService.updatePersonnelCredentials(eq(tabelNumber), any(PersonnelCredentialsDTO.class))).thenReturn(updatedCredentials);

        // When & Then
        mockMvc.perform(put("/api/credentials/{tabelNumber}", tabelNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCredentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tabelNumber", is(tabelNumber)))
                .andExpect(jsonPath("$.position", is("Senior Driver")));

        verify(personnelCredentialsService, times(1)).existsById(tabelNumber);
        verify(personnelCredentialsService, times(1)).updatePersonnelCredentials(eq(tabelNumber), any(PersonnelCredentialsDTO.class));
    }

    @Test
    void updateCredentials_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        String tabelNumber = "NONEXISTENT";
        when(personnelCredentialsService.existsById(tabelNumber)).thenReturn(false);

        // When & Then
        mockMvc.perform(put("/api/credentials/{tabelNumber}", tabelNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCredentials1)))
                .andExpect(status().isNotFound());

        verify(personnelCredentialsService, times(1)).existsById(tabelNumber);
        verify(personnelCredentialsService, never()).updatePersonnelCredentials(anyString(), any(PersonnelCredentialsDTO.class));
    }

    @Test
    void deleteCredentials_WhenExists_ShouldReturnNoContent() throws Exception {
        // Given
        String tabelNumber = "T12345";
        when(personnelCredentialsService.existsById(tabelNumber)).thenReturn(true);
        doNothing().when(personnelCredentialsService).deletePersonnelCredentials(tabelNumber);

        // When & Then
        mockMvc.perform(delete("/api/credentials/{tabelNumber}", tabelNumber))
                .andExpect(status().isNoContent());

        verify(personnelCredentialsService, times(1)).existsById(tabelNumber);
        verify(personnelCredentialsService, times(1)).deletePersonnelCredentials(tabelNumber);
    }

    @Test
    void deleteCredentials_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        String tabelNumber = "NONEXISTENT";
        when(personnelCredentialsService.existsById(tabelNumber)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/credentials/{tabelNumber}", tabelNumber))
                .andExpect(status().isNotFound());

        verify(personnelCredentialsService, times(1)).existsById(tabelNumber);
        verify(personnelCredentialsService, never()).deletePersonnelCredentials(tabelNumber);
    }

    @Test
    void getCredentialsByPosition_ShouldReturnCredentialsByPosition() throws Exception {
        // Given
        String position = "Driver";
        List<PersonnelCredentialsDTO> credentials = List.of(testCredentials1);
        when(personnelCredentialsService.findByPosition(position)).thenReturn(credentials);

        // When & Then
        mockMvc.perform(get("/api/credentials/position/{position}", position))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tabelNumber", is("T12345")))
                .andExpect(jsonPath("$[0].position", is("Driver")));

        verify(personnelCredentialsService, times(1)).findByPosition(position);
    }

}