package com.app.pomodoroapp.me.controller;

import com.app.pomodoroapp.config.JwtUtil;
import com.app.pomodoroapp.controller.me.TacheMeController;
import com.app.pomodoroapp.dto.me.taches.*;
import com.app.pomodoroapp.repository.UtilisateurRepository;
import com.app.pomodoroapp.security.CurrentUserService;
import com.app.pomodoroapp.service.AppUserDetailsService;
import com.app.pomodoroapp.service.me.TacheMeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TacheMeController.class)
class TacheMeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    TacheMeService service;

    @MockitoBean
    CurrentUserService currentUserService;

    @MockitoBean
    JwtUtil jwtUtil;

    @MockitoBean
    AppUserDetailsService appUserDetailsService;

    @MockitoBean
    UtilisateurRepository utilisateurRepository;

    /* -------------------- GET ALL -------------------- */
    @Test
    @WithMockUser
    void getMyTaches_shouldReturnList() throws Exception {
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(service.getAll(1L)).thenReturn(List.of(new TacheMeDto()));

        mockMvc.perform(get("/api/taches/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    /* -------------------- GET ONE -------------------- */
    @Test
    @WithMockUser
    void getMyTache_shouldReturnDto() throws Exception {
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(service.getOne(1L, 1L)).thenReturn(new TacheMeDto());

        mockMvc.perform(get("/api/taches/me/1"))
                .andExpect(status().isOk());
    }

    /* -------------------- CREATE -------------------- */
    @Test
    @WithMockUser
    void create_shouldReturnCreatedDto() throws Exception {
        TacheMeCreateDto dto = new TacheMeCreateDto();
        dto.setTitre("Titre");

        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(service.create(eq(1L), any())).thenReturn(new TacheMeDto());

        mockMvc.perform(post("/api/taches/me")
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    /* -------------------- DELETE -------------------- */
    @Test
    @WithMockUser
    void delete_shouldReturn204() throws Exception {
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        doNothing().when(service).delete(1L, 1L);

        mockMvc.perform(delete("/api/taches/me/1").with(csrf()))
                .andExpect(status().isOk());
    }
}
