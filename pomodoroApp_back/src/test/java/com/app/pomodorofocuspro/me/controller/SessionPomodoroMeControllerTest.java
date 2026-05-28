package com.app.pomodoroapp.me.controller;

import com.app.pomodoroapp.config.JwtUtil;
import com.app.pomodoroapp.controller.me.SessionPomodoroMeController;
import com.app.pomodoroapp.dto.me.session.*;
import com.app.pomodoroapp.repository.UtilisateurRepository;
import com.app.pomodoroapp.security.CurrentUserService;
import com.app.pomodoroapp.service.AppUserDetailsService;
import com.app.pomodoroapp.service.me.SessionPomodoroMeService;
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

@WebMvcTest(SessionPomodoroMeController.class)
class SessionPomodoroMeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    SessionPomodoroMeService service;

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
    void getAllMySessions_shouldReturnList() throws Exception {
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(service.getAllForUser(1L)).thenReturn(List.of(new SessionPomodoroMeDto()));

        mockMvc.perform(get("/api/sessions/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    /* -------------------- GET TODAY -------------------- */
    @Test
    @WithMockUser
    void getToday_shouldReturnList() throws Exception {
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(service.getToday(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/sessions/me/today"))
                .andExpect(status().isOk());
    }

    /* -------------------- GET CURRENT -------------------- */
    @Test
    @WithMockUser
    void getCurrent_shouldReturn204_whenNoActiveSession() throws Exception {
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(service.getCurrent(1L)).thenReturn(null);

        mockMvc.perform(get("/api/sessions/me/current"))
                .andExpect(status().isNoContent());
    }

    /* -------------------- CREATE -------------------- */
    @Test
    @WithMockUser
    void create_shouldReturnDto() throws Exception {
        SessionPomodoroMeCreateDto dto = new SessionPomodoroMeCreateDto();

        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(service.create(eq(1L), any())).thenReturn(new SessionPomodoroMeDto());

        mockMvc.perform(post("/api/sessions/me")
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

        mockMvc.perform(delete("/api/sessions/me/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}
