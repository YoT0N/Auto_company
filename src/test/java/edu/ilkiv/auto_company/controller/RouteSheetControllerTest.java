package edu.ilkiv.auto_company.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.ilkiv.auto_company.model.Bus;
import edu.ilkiv.auto_company.model.PersonalData;
import edu.ilkiv.auto_company.model.Route;
import edu.ilkiv.auto_company.model.RouteSheet;
import edu.ilkiv.auto_company.service.BusService;
import edu.ilkiv.auto_company.service.PersonalDataService;
import edu.ilkiv.auto_company.service.RouteService;
import edu.ilkiv.auto_company.service.RouteSheetService;
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
public class RouteSheetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RouteSheetService routeSheetService;

    @Mock
    private RouteService routeService;

    @Mock
    private BusService busService;

    @Mock
    private PersonalDataService personalDataService;

    @InjectMocks
    private RouteSheetController routeSheetController;

    private ObjectMapper objectMapper;
    private RouteSheet testRouteSheet1;
    private RouteSheet testRouteSheet2;
    private Route testRoute;
    private Bus testBus;
    private PersonalData testDriver;
    private PersonalData testConductor;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Ручне налаштування MockMvc з нашим контролером та мок-сервісами
        mockMvc = MockMvcBuilders.standaloneSetup(routeSheetController).build();

        // Ініціалізація тестових об'єктів
        testRoute = new Route();
        testRoute.setRouteNumber("R123");
        testRoute.setRouteName("City Center - Airport");
        testRoute.setRouteLength(15.5);
        testRoute.setAverageTime(45);
        testRoute.setPlannedTripsPerShift(12);

        testBus = new Bus();
        testBus.setCountryNumber("AA1234BB");
        testBus.setBoardingNumber("B001");
        testBus.setBrand("Mercedes");
        testBus.setPassengerCapacity(45);
        testBus.setYearOfManufacture(2020);
        testBus.setMileage(25000.0);
        testBus.setDateOfReceipt(LocalDate.of(2020, 3, 15));
        testBus.setWriteoffDate(null);

        testDriver = new PersonalData();
        testDriver.setTabelNumber("D123");
        testDriver.setFullName("John Doe");
        testDriver.setDateOfBirth(LocalDate.of(1980, 5, 15));
        testDriver.setSex("Male");
        testDriver.setHomeAddress("123 Main St");
        testDriver.setHomePhone("555-1234");
        testDriver.setPhoneNumber("555-5678");

        testConductor = new PersonalData();
        testConductor.setTabelNumber("C456");
        testConductor.setFullName("Jane Smith");
        testConductor.setDateOfBirth(LocalDate.of(1985, 8, 20));
        testConductor.setSex("Female");
        testConductor.setHomeAddress("456 Oak St");
        testConductor.setHomePhone("555-4321");
        testConductor.setPhoneNumber("555-8765");

        // Ініціалізація тестових об'єктів RouteSheet
        testRouteSheet1 = new RouteSheet();
        testRouteSheet1.setIdRouteSheet(1L);
        testRouteSheet1.setRoute(testRoute);
        testRouteSheet1.setBus(testBus);
        testRouteSheet1.setDate(LocalDate.of(2023, 5, 1));
        testRouteSheet1.setTrips(10);
        testRouteSheet1.setDriver(testDriver);
        testRouteSheet1.setConductor(testConductor);
        testRouteSheet1.setTotalPassengers(250);

        testRouteSheet2 = new RouteSheet();
        testRouteSheet2.setIdRouteSheet(2L);
        testRouteSheet2.setRoute(testRoute);
        testRouteSheet2.setBus(testBus);
        testRouteSheet2.setDate(LocalDate.of(2023, 5, 2));
        testRouteSheet2.setTrips(12);
        testRouteSheet2.setDriver(testDriver);
        testRouteSheet2.setConductor(testConductor);
        testRouteSheet2.setTotalPassengers(300);
    }

    @Test
    void getRouteSheetById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        when(routeSheetService.getRouteSheetById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/route-sheets/999"))
                .andExpect(status().isNotFound());

        verify(routeSheetService, times(1)).getRouteSheetById(999L);
    }

    @Test
    void deleteRouteSheet_WhenExists_ShouldReturnNoContent() throws Exception {
        // Given
        Long id = 1L;
        when(routeSheetService.existsById(id)).thenReturn(true);
        doNothing().when(routeSheetService).deleteRouteSheet(id);

        // When & Then
        mockMvc.perform(delete("/api/route-sheets/{id}", id))
                .andExpect(status().isNoContent());

        verify(routeSheetService, times(1)).existsById(id);
        verify(routeSheetService, times(1)).deleteRouteSheet(id);
    }

    @Test
    void deleteRouteSheet_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long id = 999L;
        when(routeSheetService.existsById(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/route-sheets/{id}", id))
                .andExpect(status().isNotFound());

        verify(routeSheetService, times(1)).existsById(id);
        verify(routeSheetService, never()).deleteRouteSheet(id);
    }

}