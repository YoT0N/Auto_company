package edu.ilkiv.auto_company.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ilkiv.auto_company.dto.KeysDTO;
import edu.ilkiv.auto_company.service.KeysService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class KeysControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private KeysService keysService;

    @InjectMocks
    private KeysController keysController;

    private ObjectMapper objectMapper;
    private KeysDTO testKeys1;
    private KeysDTO testKeys2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Ручне налаштування MockMvc з нашим контролером та мок-сервісом
        mockMvc = MockMvcBuilders.standaloneSetup(keysController).build();

        // Ініціалізація тестових об'єктів KeysDTO
        testKeys1 = new KeysDTO();
        testKeys1.setIdKeys(1L);
        testKeys1.setLogin("admin");
        testKeys1.setPassword("password123");
        testKeys1.setRole("ADMIN");
        testKeys1.setTablename("all_tables");

        testKeys2 = new KeysDTO();
        testKeys2.setIdKeys(2L);
        testKeys2.setLogin("user");
        testKeys2.setPassword("userpass");
        testKeys2.setRole("USER");
        testKeys2.setTablename("users_table");
    }

    @Test
    void getAllKeys_ShouldReturnAllKeys() throws Exception {
        // Given
        List<KeysDTO> keys = Arrays.asList(testKeys1, testKeys2);
        when(keysService.getAllKeys()).thenReturn(keys);

        // When & Then
        mockMvc.perform(get("/api/keys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].login", is("admin")))
                .andExpect(jsonPath("$[0].role", is("ADMIN")))
                .andExpect(jsonPath("$[1].login", is("user")))
                .andExpect(jsonPath("$[1].role", is("USER")));

        verify(keysService, times(1)).getAllKeys();
    }

    @Test
    void getKeysById_WhenExists_ShouldReturnKeys() throws Exception {
        // Given
        when(keysService.getKeysById(1L)).thenReturn(Optional.of(testKeys1));

        // When & Then
        mockMvc.perform(get("/api/keys/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idKeys", is(1)))
                .andExpect(jsonPath("$.login", is("admin")))
                .andExpect(jsonPath("$.role", is("ADMIN")));

        verify(keysService, times(1)).getKeysById(1L);
    }

    @Test
    void getKeysById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        when(keysService.getKeysById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/keys/999"))
                .andExpect(status().isNotFound());

        verify(keysService, times(1)).getKeysById(999L);
    }

    @Test
    void getKeysByLogin_WhenExists_ShouldReturnKeys() throws Exception {
        // Given
        when(keysService.findByLogin("admin")).thenReturn(Optional.of(testKeys1));

        // When & Then
        mockMvc.perform(get("/api/keys/login/admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idKeys", is(1)))
                .andExpect(jsonPath("$.login", is("admin")))
                .andExpect(jsonPath("$.role", is("ADMIN")));

        verify(keysService, times(1)).findByLogin("admin");
    }

    @Test
    void getKeysByLogin_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        when(keysService.findByLogin("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/keys/login/nonexistent"))
                .andExpect(status().isNotFound());

        verify(keysService, times(1)).findByLogin("nonexistent");
    }

    @Test
    void createKeys_WithValidData_ShouldCreateAndReturnKeys() throws Exception {
        // Given
        when(keysService.saveKeys(any(KeysDTO.class))).thenReturn(testKeys1);

        // When & Then
        mockMvc.perform(post("/api/keys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testKeys1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idKeys", is(1)))
                .andExpect(jsonPath("$.login", is("admin")))
                .andExpect(jsonPath("$.role", is("ADMIN")));

        verify(keysService, times(1)).saveKeys(any(KeysDTO.class));
    }

    @Test
    void updateKeys_WhenExists_ShouldUpdateAndReturnKeys() throws Exception {
        // Given
        Long keysId = 1L;
        KeysDTO updatedKeys = new KeysDTO();
        updatedKeys.setIdKeys(keysId);
        updatedKeys.setLogin("admin-updated");
        updatedKeys.setPassword("newpassword");
        updatedKeys.setRole("SUPER_ADMIN");
        updatedKeys.setTablename("all_tables");

        when(keysService.existsById(keysId)).thenReturn(true);
        when(keysService.updateKeys(eq(keysId), any(KeysDTO.class))).thenReturn(updatedKeys);

        // When & Then
        mockMvc.perform(put("/api/keys/{id}", keysId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedKeys)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idKeys", is(1)))
                .andExpect(jsonPath("$.login", is("admin-updated")))
                .andExpect(jsonPath("$.role", is("SUPER_ADMIN")));

        verify(keysService, times(1)).existsById(keysId);
        verify(keysService, times(1)).updateKeys(eq(keysId), any(KeysDTO.class));
    }

    @Test
    void updateKeys_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long keysId = 999L;
        when(keysService.existsById(keysId)).thenReturn(false);

        // When & Then
        mockMvc.perform(put("/api/keys/{id}", keysId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testKeys1)))
                .andExpect(status().isNotFound());

        verify(keysService, times(1)).existsById(keysId);
        verify(keysService, never()).updateKeys(anyLong(), any(KeysDTO.class));
    }

    @Test
    void deleteKeys_WhenExists_ShouldReturnNoContent() throws Exception {
        // Given
        Long keysId = 1L;
        when(keysService.existsById(keysId)).thenReturn(true);
        doNothing().when(keysService).deleteKeys(keysId);

        // When & Then
        mockMvc.perform(delete("/api/keys/{id}", keysId))
                .andExpect(status().isNoContent());

        verify(keysService, times(1)).existsById(keysId);
        verify(keysService, times(1)).deleteKeys(keysId);
    }

    @Test
    void deleteKeys_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long keysId = 999L;
        when(keysService.existsById(keysId)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/keys/{id}", keysId))
                .andExpect(status().isNotFound());

        verify(keysService, times(1)).existsById(keysId);
        verify(keysService, never()).deleteKeys(keysId);
    }

    @Test
    void getKeysByRole_ShouldReturnKeysByRole() throws Exception {
        // Given
        String role = "ADMIN";
        List<KeysDTO> keys = List.of(testKeys1);
        when(keysService.findByRole(role)).thenReturn(keys);

        // When & Then
        mockMvc.perform(get("/api/keys/role/{role}", role))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idKeys", is(1)))
                .andExpect(jsonPath("$[0].login", is("admin")))
                .andExpect(jsonPath("$[0].role", is("ADMIN")));

        verify(keysService, times(1)).findByRole(role);
    }

    @Test
    void getKeysByTablename_ShouldReturnKeysByTablename() throws Exception {
        // Given
        String tablename = "all_tables";
        List<KeysDTO> keys = List.of(testKeys1);
        when(keysService.findByTablenameEquals(tablename)).thenReturn(keys);

        // When & Then
        mockMvc.perform(get("/api/keys/tablename/{tablename}", tablename))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idKeys", is(1)))
                .andExpect(jsonPath("$[0].login", is("admin")))
                .andExpect(jsonPath("$[0].tablename", is("all_tables")));

        verify(keysService, times(1)).findByTablenameEquals(tablename);
    }
}