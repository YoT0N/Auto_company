package edu.ilkiv.auto_company.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.ilkiv.auto_company.dto.TechnicalExaminationDTO;
import edu.ilkiv.auto_company.model.Bus;
import edu.ilkiv.auto_company.service.BusService;
import edu.ilkiv.auto_company.service.TechnicalExaminationService;
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
public class TechnicalExaminationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TechnicalExaminationService technicalExaminationService;

    @Mock
    private BusService busService;

    @InjectMocks
    private TechnicalExaminationController technicalExaminationController;

    private ObjectMapper objectMapper;
    private TechnicalExaminationDTO testExam1;
    private TechnicalExaminationDTO testExam2;
    private Bus testBus;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Ручне налаштування MockMvc з нашим контролером та мок-сервісами
        mockMvc = MockMvcBuilders.standaloneSetup(technicalExaminationController).build();

        // Створюємо тестовий автобус для зв'язування з технічними оглядами
        testBus = new Bus();
        testBus.setCountryNumber("AA1234BB");
        testBus.setBoardingNumber("B001");
        testBus.setBrand("Mercedes");
        testBus.setPassengerCapacity(45);
        testBus.setYearOfManufacture(2020);
        testBus.setMileage(25000.0);
        testBus.setDateOfReceipt(LocalDate.of(2020, 3, 15));
        testBus.setWriteoffDate(null);

        // Ініціалізація тестових об'єктів TechnicalExaminationDTO
        testExam1 = new TechnicalExaminationDTO();
        testExam1.setIdTechnicalExamination(1L);
        testExam1.setBusCountryNumber("AA1234BB");
        testExam1.setDate(LocalDate.of(2023, 5, 15));
        testExam1.setExaminationResult("Good condition, minor issues with brakes");
        testExam1.setSentForRepair(true);
        testExam1.setRepairPrice(1500.0);

        testExam2 = new TechnicalExaminationDTO();
        testExam2.setIdTechnicalExamination(2L);
        testExam2.setBusCountryNumber("AA1234BB");
        testExam2.setDate(LocalDate.of(2023, 8, 20));
        testExam2.setExaminationResult("All systems operational");
        testExam2.setSentForRepair(false);
        testExam2.setRepairPrice(0.0);
    }

    @Test
    void getAllTechnicalExaminations_ShouldReturnAllExaminations() throws Exception {
        // Given
        List<TechnicalExaminationDTO> examinations = Arrays.asList(testExam1, testExam2);
        when(technicalExaminationService.getAllTechnicalExaminations()).thenReturn(examinations);

        // When & Then
        mockMvc.perform(get("/api/technical-examinations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idTechnicalExamination", is(1)))
                .andExpect(jsonPath("$[0].busCountryNumber", is("AA1234BB")))
                .andExpect(jsonPath("$[1].idTechnicalExamination", is(2)))
                .andExpect(jsonPath("$[1].busCountryNumber", is("AA1234BB")));

        verify(technicalExaminationService, times(1)).getAllTechnicalExaminations();
    }

    @Test
    void getTechnicalExaminationById_WhenExists_ShouldReturnExamination() throws Exception {
        // Given
        Long id = 1L;
        when(technicalExaminationService.getTechnicalExaminationById(id)).thenReturn(Optional.of(testExam1));

        // When & Then
        mockMvc.perform(get("/api/technical-examinations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idTechnicalExamination", is(1)))
                .andExpect(jsonPath("$.busCountryNumber", is("AA1234BB")))
                .andExpect(jsonPath("$.examinationResult", is("Good condition, minor issues with brakes")));

        verify(technicalExaminationService, times(1)).getTechnicalExaminationById(id);
    }

    @Test
    void getTechnicalExaminationById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long id = 99L;
        when(technicalExaminationService.getTechnicalExaminationById(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/technical-examinations/{id}", id))
                .andExpect(status().isNotFound());

        verify(technicalExaminationService, times(1)).getTechnicalExaminationById(id);
    }

    @Test
    void createTechnicalExamination_ShouldCreateAndReturnExamination() throws Exception {
        // Given
        when(technicalExaminationService.saveTechnicalExamination(any(TechnicalExaminationDTO.class))).thenReturn(testExam1);

        // When & Then
        mockMvc.perform(post("/api/technical-examinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testExam1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTechnicalExamination", is(1)))
                .andExpect(jsonPath("$.busCountryNumber", is("AA1234BB")))
                .andExpect(jsonPath("$.sentForRepair", is(true)));

        verify(technicalExaminationService, times(1)).saveTechnicalExamination(any(TechnicalExaminationDTO.class));
    }

    @Test
    void updateTechnicalExamination_WhenExists_ShouldUpdateAndReturnExamination() throws Exception {
        // Given
        Long id = 1L;
        TechnicalExaminationDTO updatedExam = new TechnicalExaminationDTO();
        updatedExam.setIdTechnicalExamination(id);
        updatedExam.setBusCountryNumber("AA1234BB");
        updatedExam.setDate(LocalDate.of(2023, 5, 15));
        updatedExam.setExaminationResult("Updated result after inspection");
        updatedExam.setSentForRepair(true);
        updatedExam.setRepairPrice(2000.0);

        when(technicalExaminationService.existsById(id)).thenReturn(true);
        when(technicalExaminationService.updateTechnicalExamination(eq(id), any(TechnicalExaminationDTO.class))).thenReturn(updatedExam);

        // When & Then
        mockMvc.perform(put("/api/technical-examinations/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedExam)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTechnicalExamination", is(1)))
                .andExpect(jsonPath("$.examinationResult", is("Updated result after inspection")))
                .andExpect(jsonPath("$.repairPrice", is(2000.0)));

        verify(technicalExaminationService, times(1)).existsById(id);
        verify(technicalExaminationService, times(1)).updateTechnicalExamination(eq(id), any(TechnicalExaminationDTO.class));
    }

    @Test
    void updateTechnicalExamination_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long id = 99L;
        when(technicalExaminationService.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(put("/api/technical-examinations/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testExam1)))
                .andExpect(status().isNotFound());

        verify(technicalExaminationService, times(1)).existsById(id);
        verify(technicalExaminationService, never()).updateTechnicalExamination(anyLong(), any(TechnicalExaminationDTO.class));
    }

    @Test
    void deleteTechnicalExamination_WhenExists_ShouldReturnNoContent() throws Exception {
        // Given
        Long id = 1L;
        when(technicalExaminationService.existsById(id)).thenReturn(true);
        doNothing().when(technicalExaminationService).deleteTechnicalExamination(id);

        // When & Then
        mockMvc.perform(delete("/api/technical-examinations/{id}", id))
                .andExpect(status().isNoContent());

        verify(technicalExaminationService, times(1)).existsById(id);
        verify(technicalExaminationService, times(1)).deleteTechnicalExamination(id);
    }

    @Test
    void deleteTechnicalExamination_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long id = 99L;
        when(technicalExaminationService.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/technical-examinations/{id}", id))
                .andExpect(status().isNotFound());

        verify(technicalExaminationService, times(1)).existsById(id);
        verify(technicalExaminationService, never()).deleteTechnicalExamination(id);
    }

    @Test
    void getTechnicalExaminationsByRepairStatus_ShouldReturnExaminations() throws Exception {
        // Given
        Boolean sentForRepair = true;
        List<TechnicalExaminationDTO> examinations = List.of(testExam1);
        when(technicalExaminationService.findBySentForRepair(sentForRepair)).thenReturn(examinations);

        // When & Then
        mockMvc.perform(get("/api/technical-examinations/sent-for-repair")
                        .param("sentForRepair", sentForRepair.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idTechnicalExamination", is(1)))
                .andExpect(jsonPath("$[0].sentForRepair", is(true)));

        verify(technicalExaminationService, times(1)).findBySentForRepair(sentForRepair);
    }

    @Test
    void getTechnicalExaminationsByRepairPriceGreaterThan_ShouldReturnExaminations() throws Exception {
        // Given
        Double price = 1000.0;
        List<TechnicalExaminationDTO> examinations = List.of(testExam1);
        when(technicalExaminationService.findByRepairPriceGreaterThan(price)).thenReturn(examinations);

        // When & Then
        mockMvc.perform(get("/api/technical-examinations/repair-price")
                        .param("price", price.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idTechnicalExamination", is(1)))
                .andExpect(jsonPath("$[0].repairPrice", is(1500.0)));

        verify(technicalExaminationService, times(1)).findByRepairPriceGreaterThan(price);
    }

    @Test
    void getTechnicalExaminationsByResultContaining_ShouldReturnExaminations() throws Exception {
        // Given
        String keyword = "brakes";
        List<TechnicalExaminationDTO> examinations = List.of(testExam1);
        when(technicalExaminationService.findByExaminationResultContaining(keyword)).thenReturn(examinations);

        // When & Then
        mockMvc.perform(get("/api/technical-examinations/result")
                        .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idTechnicalExamination", is(1)))
                .andExpect(jsonPath("$[0].examinationResult", containsString(keyword)));

        verify(technicalExaminationService, times(1)).findByExaminationResultContaining(keyword);
    }
}