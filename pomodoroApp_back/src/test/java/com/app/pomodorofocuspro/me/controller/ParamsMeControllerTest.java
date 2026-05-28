package com.app.pomodoroapp.me.controller;

import com.app.pomodoroapp.config.JwtUtil;
import com.app.pomodoroapp.controller.me.ParamsMeController;
import com.app.pomodoroapp.dto.me.params.ParamsMeDto;
import com.app.pomodoroapp.dto.me.params.ParamsMeUpdateDto;
import com.app.pomodoroapp.repository.UtilisateurRepository;
import com.app.pomodoroapp.security.CurrentUserService;
import com.app.pomodoroapp.service.AppUserDetailsService;
import com.app.pomodoroapp.service.me.ParamsMeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParamsMeController.class)
class ParamsMeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ParamsMeService service;

    @MockitoBean
    CurrentUserService currentUserService;

    @MockitoBean
    JwtUtil jwtUtil;

    @MockitoBean
    AppUserDetailsService appUserDetailsService;

    @MockitoBean
    UtilisateurRepository utilisateurRepository;

    /* -------------------- GET /me -------------------- */
    @Test
    @WithMockUser
    void getMyParams_shouldReturnDto() throws Exception {
        ParamsMeDto dto = new ParamsMeDto();

        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(service.getForUser(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/params/me"))
                .andExpect(status().isOk());
    }

    /* -------------------- PUT /me -------------------- */
    @Test
    @WithMockUser
    void updateMyParams_shouldReturnUpdatedDto() throws Exception {
        ParamsMeUpdateDto update = new ParamsMeUpdateDto();
        update.setTheme("dark");
        update.setSonActif(true);

        ParamsMeDto dto = new ParamsMeDto();

        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(service.updateForUser(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/params/me")
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk());
    }

    /* -------------------- PATCH /me -------------------- */
    @Test
    @WithMockUser
    void patchMyParams_shouldReturnUpdatedDto() throws Exception {
        ParamsMeUpdateDto patch = new ParamsMeUpdateDto();
        patch.setTheme("dark");

        ParamsMeDto dto = new ParamsMeDto();

        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(service.patchForUser(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(patch("/api/params/me")
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk());
    }

}
