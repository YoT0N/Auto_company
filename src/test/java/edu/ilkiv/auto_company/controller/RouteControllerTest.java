package edu.ilkiv.auto_company.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.ilkiv.auto_company.dto.RouteDTO;
import edu.ilkiv.auto_company.service.RouteService;
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
public class RouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RouteService routeService;

    @InjectMocks
    private RouteController routeController;

    private ObjectMapper objectMapper;
    private RouteDTO testRoute1;
    private RouteDTO testRoute2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Ручне налаштування MockMvc з нашим контролером та мок-сервісом
        mockMvc = MockMvcBuilders.standaloneSetup(routeController).build();

        // Ініціалізація тестових об'єктів RouteDTO
        testRoute1 = new RouteDTO();
        testRoute1.setRouteNumber("R001");
        testRoute1.setRouteName("City Center - Airport");
        testRoute1.setRouteLength(15.5);
        testRoute1.setAverageTime(45);
        testRoute1.setPlannedTripsPerShift(8);

        testRoute2 = new RouteDTO();
        testRoute2.setRouteNumber("R002");
        testRoute2.setRouteName("University - Shopping Mall");
        testRoute2.setRouteLength(10.2);
        testRoute2.setAverageTime(30);
        testRoute2.setPlannedTripsPerShift(12);
    }

    @Test
    void getAllRoutes_ShouldReturnAllRoutes() throws Exception {
        // Given
        List<RouteDTO> routes = Arrays.asList(testRoute1, testRoute2);
        when(routeService.getAllRoutes()).thenReturn(routes);

        // When & Then
        mockMvc.perform(get("/api/routes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].routeNumber", is("R001")))
                .andExpect(jsonPath("$[0].routeName", is("City Center - Airport")))
                .andExpect(jsonPath("$[1].routeNumber", is("R002")))
                .andExpect(jsonPath("$[1].routeName", is("University - Shopping Mall")));

        verify(routeService, times(1)).getAllRoutes();
    }

    @Test
    void getRouteById_WhenExists_ShouldReturnRoute() throws Exception {
        // Given
        when(routeService.getRouteById("R001")).thenReturn(Optional.of(testRoute1));

        // When & Then
        mockMvc.perform(get("/api/routes/R001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.routeNumber", is("R001")))
                .andExpect(jsonPath("$.routeName", is("City Center - Airport")))
                .andExpect(jsonPath("$.routeLength", is(15.5)))
                .andExpect(jsonPath("$.averageTime", is(45)));

        verify(routeService, times(1)).getRouteById("R001");
    }

    @Test
    void getRouteById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        when(routeService.getRouteById("NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/routes/NONEXISTENT"))
                .andExpect(status().isNotFound());

        verify(routeService, times(1)).getRouteById("NONEXISTENT");
    }

    @Test
    void createRoute_WithValidData_ShouldCreateAndReturnRoute() throws Exception {
        // Given
        when(routeService.existsById(anyString())).thenReturn(false);
        when(routeService.saveRoute(any(RouteDTO.class))).thenReturn(testRoute1);

        // When & Then
        mockMvc.perform(post("/api/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRoute1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.routeNumber", is("R001")))
                .andExpect(jsonPath("$.routeName", is("City Center - Airport")));

        verify(routeService, times(1)).existsById(testRoute1.getRouteNumber());
        verify(routeService, times(1)).saveRoute(any(RouteDTO.class));
    }

    @Test
    void createRoute_WithExistingId_ShouldReturnConflict() throws Exception {
        // Given
        when(routeService.existsById(testRoute1.getRouteNumber())).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRoute1)))
                .andExpect(status().isConflict());

        verify(routeService, times(1)).existsById(testRoute1.getRouteNumber());
        verify(routeService, never()).saveRoute(any(RouteDTO.class));
    }

    @Test
    void updateRoute_WhenExists_ShouldUpdateAndReturnRoute() throws Exception {
        // Given
        String routeNumber = "R001";
        RouteDTO updatedRoute = new RouteDTO();
        updatedRoute.setRouteNumber(routeNumber);
        updatedRoute.setRouteName("City Center - Airport Express");
        updatedRoute.setRouteLength(16.0);
        updatedRoute.setAverageTime(40);
        updatedRoute.setPlannedTripsPerShift(10);

        when(routeService.existsById(routeNumber)).thenReturn(true);
        when(routeService.updateRoute(eq(routeNumber), any(RouteDTO.class))).thenReturn(updatedRoute);

        // When & Then
        mockMvc.perform(put("/api/routes/{routeNumber}", routeNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRoute)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routeNumber", is(routeNumber)))
                .andExpect(jsonPath("$.routeName", is("City Center - Airport Express")))
                .andExpect(jsonPath("$.routeLength", is(16.0)))
                .andExpect(jsonPath("$.averageTime", is(40)))
                .andExpect(jsonPath("$.plannedTripsPerShift", is(10)));

        verify(routeService, times(1)).existsById(routeNumber);
        verify(routeService, times(1)).updateRoute(eq(routeNumber), any(RouteDTO.class));
    }

    @Test
    void updateRoute_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        String routeNumber = "NONEXISTENT";
        when(routeService.existsById(routeNumber)).thenReturn(false);

        // When & Then
        mockMvc.perform(put("/api/routes/{routeNumber}", routeNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRoute1)))
                .andExpect(status().isNotFound());

        verify(routeService, times(1)).existsById(routeNumber);
        verify(routeService, never()).updateRoute(anyString(), any(RouteDTO.class));
    }

    @Test
    void deleteRoute_WhenExists_ShouldReturnNoContent() throws Exception {
        // Given
        String routeNumber = "R001";
        when(routeService.existsById(routeNumber)).thenReturn(true);
        doNothing().when(routeService).deleteRoute(routeNumber);

        // When & Then
        mockMvc.perform(delete("/api/routes/{routeNumber}", routeNumber))
                .andExpect(status().isNoContent());

        verify(routeService, times(1)).existsById(routeNumber);
        verify(routeService, times(1)).deleteRoute(routeNumber);
    }

    @Test
    void deleteRoute_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        String routeNumber = "NONEXISTENT";
        when(routeService.existsById(routeNumber)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/routes/{routeNumber}", routeNumber))
                .andExpect(status().isNotFound());

        verify(routeService, times(1)).existsById(routeNumber);
        verify(routeService, never()).deleteRoute(routeNumber);
    }

    @Test
    void getRoutesByName_ShouldReturnRoutesByNameContainingKeyword() throws Exception {
        // Given
        String keyword = "Center";
        List<RouteDTO> routes = List.of(testRoute1);
        when(routeService.findByRouteNameContaining(keyword)).thenReturn(routes);

        // When & Then
        mockMvc.perform(get("/api/routes/search")
                        .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].routeNumber", is("R001")))
                .andExpect(jsonPath("$[0].routeName", is("City Center - Airport")));

        verify(routeService, times(1)).findByRouteNameContaining(keyword);
    }

    @Test
    void getRoutesByLengthGreaterThan_ShouldReturnRoutesByLength() throws Exception {
        // Given
        Double length = 12.0;
        List<RouteDTO> routes = List.of(testRoute1);
        when(routeService.findByRouteLengthGreaterThan(length)).thenReturn(routes);

        // When & Then
        mockMvc.perform(get("/api/routes/length")
                        .param("length", length.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].routeNumber", is("R001")))
                .andExpect(jsonPath("$[0].routeLength", is(15.5)));

        verify(routeService, times(1)).findByRouteLengthGreaterThan(length);
    }

    @Test
    void getRoutesByTimeLessThan_ShouldReturnRoutesByAverageTime() throws Exception {
        // Given
        Integer time = 40;
        List<RouteDTO> routes = List.of(testRoute2);
        when(routeService.findByAverageTimeLessThan(time)).thenReturn(routes);

        // When & Then
        mockMvc.perform(get("/api/routes/time")
                        .param("time", time.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].routeNumber", is("R002")))
                .andExpect(jsonPath("$[0].averageTime", is(30)));

        verify(routeService, times(1)).findByAverageTimeLessThan(time);
    }
}