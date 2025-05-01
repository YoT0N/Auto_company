package edu.ilkiv.auto_company.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.ilkiv.auto_company.dto.PhysicalExaminationDTO;
import edu.ilkiv.auto_company.model.PersonalData;
import edu.ilkiv.auto_company.service.PersonalDataService;
import edu.ilkiv.auto_company.service.PhysicalExaminationService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PhysicalExaminationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PhysicalExaminationService physicalExaminationService;

    @Mock
    private PersonalDataService personalDataService;

    @InjectMocks
    private PhysicalExaminationController physicalExaminationController;

    private ObjectMapper objectMapper;
    private PhysicalExaminationDTO testExamination1;
    private PhysicalExaminationDTO testExamination2;
    private PersonalData testPersonnel;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Manual setup of MockMvc with our controller and mock services
        mockMvc = MockMvcBuilders.standaloneSetup(physicalExaminationController).build();

        // Initialize test personnel
        testPersonnel = new PersonalData();
        testPersonnel.setTabelNumber("T12345");
        testPersonnel.setFullName("John Doe");

        // Initialize test PhysicalExaminationDTO objects
        testExamination1 = new PhysicalExaminationDTO();
        testExamination1.setIdPhysicalExamination(1L);
        testExamination1.setPersonnelTabelNumber("T12345");
        testExamination1.setDate(LocalDate.of(2023, 5, 15));
        testExamination1.setExaminationResult("Healthy, fit for work");

        testExamination2 = new PhysicalExaminationDTO();
        testExamination2.setIdPhysicalExamination(2L);
        testExamination2.setPersonnelTabelNumber("T12345");
        testExamination2.setDate(LocalDate.of(2023, 8, 10));
        testExamination2.setExaminationResult("Minor issues, follow-up required");
    }

    @Test
    void getAllPhysicalExaminations_ShouldReturnAllExaminations() throws Exception {
        // Given
        List<PhysicalExaminationDTO> examinations = Arrays.asList(testExamination1, testExamination2);
        when(physicalExaminationService.getAllPhysicalExaminations()).thenReturn(examinations);

        // When & Then
        mockMvc.perform(get("/api/physical-examinations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idPhysicalExamination", is(1)))
                .andExpect(jsonPath("$[0].personnelTabelNumber", is("T12345")))
                .andExpect(jsonPath("$[1].idPhysicalExamination", is(2)))
                .andExpect(jsonPath("$[1].examinationResult", is("Minor issues, follow-up required")));

        verify(physicalExaminationService, times(1)).getAllPhysicalExaminations();
    }

    @Test
    void getPhysicalExaminationById_WhenExists_ShouldReturnExamination() throws Exception {
        // Given
        Long id = 1L;
        when(physicalExaminationService.getPhysicalExaminationById(id)).thenReturn(Optional.of(testExamination1));

        // When & Then
        mockMvc.perform(get("/api/physical-examinations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idPhysicalExamination", is(1)))
                .andExpect(jsonPath("$.personnelTabelNumber", is("T12345")))
                .andExpect(jsonPath("$.examinationResult", is("Healthy, fit for work")));

        verify(physicalExaminationService, times(1)).getPhysicalExaminationById(id);
    }

    @Test
    void getPhysicalExaminationById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long id = 999L;
        when(physicalExaminationService.getPhysicalExaminationById(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/physical-examinations/{id}", id))
                .andExpect(status().isNotFound());

        verify(physicalExaminationService, times(1)).getPhysicalExaminationById(id);
    }

    @Test
    void createPhysicalExamination_WithValidData_ShouldCreateAndReturnExamination() throws Exception {
        // Given
        when(physicalExaminationService.savePhysicalExamination(any(PhysicalExaminationDTO.class))).thenReturn(testExamination1);

        // When & Then
        mockMvc.perform(post("/api/physical-examinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testExamination1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPhysicalExamination", is(1)))
                .andExpect(jsonPath("$.personnelTabelNumber", is("T12345")))
                .andExpect(jsonPath("$.examinationResult", is("Healthy, fit for work")));

        verify(physicalExaminationService, times(1)).savePhysicalExamination(any(PhysicalExaminationDTO.class));
    }

    @Test
    void updatePhysicalExamination_WhenExists_ShouldUpdateAndReturnExamination() throws Exception {
        // Given
        Long id = 1L;
        PhysicalExaminationDTO updatedExamination = new PhysicalExaminationDTO();
        updatedExamination.setIdPhysicalExamination(id);
        updatedExamination.setPersonnelTabelNumber("T12345");
        updatedExamination.setDate(LocalDate.of(2023, 5, 15));
        updatedExamination.setExaminationResult("Updated result, fit for work with restrictions");

        when(physicalExaminationService.existsById(id)).thenReturn(true);
        when(physicalExaminationService.updatePhysicalExamination(eq(id), any(PhysicalExaminationDTO.class))).thenReturn(updatedExamination);

        // When & Then
        mockMvc.perform(put("/api/physical-examinations/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedExamination)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPhysicalExamination", is(1)))
                .andExpect(jsonPath("$.personnelTabelNumber", is("T12345")))
                .andExpect(jsonPath("$.examinationResult", is("Updated result, fit for work with restrictions")));

        verify(physicalExaminationService, times(1)).existsById(id);
        verify(physicalExaminationService, times(1)).updatePhysicalExamination(eq(id), any(PhysicalExaminationDTO.class));
    }

    @Test
    void updatePhysicalExamination_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long id = 999L;
        when(physicalExaminationService.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(put("/api/physical-examinations/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testExamination1)))
                .andExpect(status().isNotFound());

        verify(physicalExaminationService, times(1)).existsById(id);
        verify(physicalExaminationService, never()).updatePhysicalExamination(anyLong(), any(PhysicalExaminationDTO.class));
    }

    @Test
    void deletePhysicalExamination_WhenExists_ShouldReturnNoContent() throws Exception {
        // Given
        Long id = 1L;
        when(physicalExaminationService.existsById(id)).thenReturn(true);
        doNothing().when(physicalExaminationService).deletePhysicalExamination(id);

        // When & Then
        mockMvc.perform(delete("/api/physical-examinations/{id}", id))
                .andExpect(status().isNoContent());

        verify(physicalExaminationService, times(1)).existsById(id);
        verify(physicalExaminationService, times(1)).deletePhysicalExamination(id);
    }

    @Test
    void deletePhysicalExamination_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long id = 999L;
        when(physicalExaminationService.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/physical-examinations/{id}", id))
                .andExpect(status().isNotFound());

        verify(physicalExaminationService, times(1)).existsById(id);
        verify(physicalExaminationService, never()).deletePhysicalExamination(id);
    }


    @Test
    void getPhysicalExaminationsByDateRange_ShouldReturnExaminations() throws Exception {
        // Given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        List<PhysicalExaminationDTO> examinations = Arrays.asList(testExamination1, testExamination2);

        when(physicalExaminationService.findByDateBetween(startDate, endDate)).thenReturn(examinations);

        // When & Then
        mockMvc.perform(get("/api/physical-examinations/date-range")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idPhysicalExamination", is(1)))
                .andExpect(jsonPath("$[1].idPhysicalExamination", is(2)));

        verify(physicalExaminationService, times(1)).findByDateBetween(startDate, endDate);
    }

    @Test
    void getPhysicalExaminationsByResult_ShouldReturnExaminations() throws Exception {
        // Given
        String keyword = "Healthy";
        List<PhysicalExaminationDTO> examinations = List.of(testExamination1);

        when(physicalExaminationService.findByExaminationResultContaining(keyword)).thenReturn(examinations);

        // When & Then
        mockMvc.perform(get("/api/physical-examinations/result")
                        .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idPhysicalExamination", is(1)))
                .andExpect(jsonPath("$[0].examinationResult", is("Healthy, fit for work")));

        verify(physicalExaminationService, times(1)).findByExaminationResultContaining(keyword);
    }
}