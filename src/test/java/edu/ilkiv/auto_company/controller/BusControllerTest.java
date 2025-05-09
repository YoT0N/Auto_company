package edu.ilkiv.auto_company.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.ilkiv.auto_company.dto.BusDTO;
import edu.ilkiv.auto_company.service.BusService;
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
public class BusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BusService busService;

    @InjectMocks
    private BusController busController;

    private ObjectMapper objectMapper;
    private BusDTO testBus1;
    private BusDTO testBus2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Ручне налаштування MockMvc з нашим контролером та мок-сервісом
        mockMvc = MockMvcBuilders.standaloneSetup(busController).build();

        // Ініціалізація тестових об'єктів BusDTO
        testBus1 = new BusDTO();
        testBus1.setCountryNumber("AA1234BB");
        testBus1.setBoardingNumber("B001");
        testBus1.setBrand("Mercedes");
        testBus1.setPassengerCapacity(45);
        testBus1.setYearOfManufacture(2020);
        testBus1.setMileage(25000.0);
        testBus1.setDateOfReceipt(LocalDate.of(2020, 3, 15));
        testBus1.setWriteoffDate(null);

        testBus2 = new BusDTO();
        testBus2.setCountryNumber("CC5678DD");
        testBus2.setBoardingNumber("B002");
        testBus2.setBrand("Volvo");
        testBus2.setPassengerCapacity(50);
        testBus2.setYearOfManufacture(2018);
        testBus2.setMileage(75000.0);
        testBus2.setDateOfReceipt(LocalDate.of(2018, 5, 10));
        testBus2.setWriteoffDate(null);
    }

    @Test
    void getAllBuses_ShouldReturnAllBuses() throws Exception {
        // Given
        List<BusDTO> buses = Arrays.asList(testBus1, testBus2);
        when(busService.getAllBuses()).thenReturn(buses);

        // When & Then
        mockMvc.perform(get("/api/buses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].countryNumber", is("AA1234BB")))
                .andExpect(jsonPath("$[0].brand", is("Mercedes")))
                .andExpect(jsonPath("$[1].countryNumber", is("CC5678DD")))
                .andExpect(jsonPath("$[1].brand", is("Volvo")));

        verify(busService, times(1)).getAllBuses();
    }

    @Test
    void getBusById_WhenExists_ShouldReturnBus() throws Exception {
        // Given
        when(busService.getBusById("AA1234BB")).thenReturn(Optional.of(testBus1));

        // When & Then
        mockMvc.perform(get("/api/buses/AA1234BB"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.countryNumber", is("AA1234BB")))
                .andExpect(jsonPath("$.brand", is("Mercedes")))
                .andExpect(jsonPath("$.passengerCapacity", is(45)));

        verify(busService, times(1)).getBusById("AA1234BB");
    }


    @Test
    void deleteBus_WhenExists_ShouldReturnNoContent() throws Exception {
        // Given
        String countryNumber = "AA1234BB";
        when(busService.existsById(countryNumber)).thenReturn(true);
        doNothing().when(busService).deleteBus(countryNumber);

        // When & Then
        mockMvc.perform(delete("/api/buses/{countryNumber}", countryNumber))
                .andExpect(status().isNoContent());

        verify(busService, times(1)).existsById(countryNumber);
        verify(busService, times(1)).deleteBus(countryNumber);
    }

    @Test
    void deleteBus_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        String countryNumber = "NONEXISTENT";
        when(busService.existsById(countryNumber)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/buses/{countryNumber}", countryNumber))
                .andExpect(status().isNotFound());

        verify(busService, times(1)).existsById(countryNumber);
        verify(busService, never()).deleteBus(countryNumber);
    }

    @Test
    void getBusesByBrand_ShouldReturnBusesByBrand() throws Exception {
        // Given
        String brand = "Mercedes";
        List<BusDTO> buses = List.of(testBus1);
        when(busService.findByBrand(brand)).thenReturn(buses);

        // When & Then
        mockMvc.perform(get("/api/buses/brand/{brand}", brand))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].countryNumber", is("AA1234BB")))
                .andExpect(jsonPath("$[0].brand", is("Mercedes")));

        verify(busService, times(1)).findByBrand(brand);
    }

    @Test
    void getBusesByYear_ShouldReturnBusesByYear() throws Exception {
        // Given
        Integer year = 2020;
        List<BusDTO> buses = List.of(testBus1);
        when(busService.findByYearOfManufacture(year)).thenReturn(buses);

        // When & Then
        mockMvc.perform(get("/api/buses/year/{year}", year))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].countryNumber", is("AA1234BB")))
                .andExpect(jsonPath("$[0].yearOfManufacture", is(2020)));

        verify(busService, times(1)).findByYearOfManufacture(year);
    }


    @Test
    void getActiveBuses_ShouldReturnActiveBuses() throws Exception {
        // Given
        List<BusDTO> activeBuses = Arrays.asList(testBus1, testBus2);
        when(busService.findActiveBuses()).thenReturn(activeBuses);

        // When & Then
        mockMvc.perform(get("/api/buses/active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].countryNumber", is("AA1234BB")))
                .andExpect(jsonPath("$[1].countryNumber", is("CC5678DD")))
                .andExpect(jsonPath("$[*].writeoffDate").value(everyItem(nullValue())));

        verify(busService, times(1)).findActiveBuses();
    }
}