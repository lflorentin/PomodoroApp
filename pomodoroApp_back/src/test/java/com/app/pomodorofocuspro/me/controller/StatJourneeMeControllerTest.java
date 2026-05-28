package com.app.pomodoroapp.me.controller;

import com.app.pomodoroapp.config.JwtUtil;
import com.app.pomodoroapp.controller.me.StatJourneeMeController;
import com.app.pomodoroapp.dto.me.stat.StatJourneeMeDto;
import com.app.pomodoroapp.entity.StatJournee;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.mapper.me.StatJourneeMeMapper;
import com.app.pomodoroapp.repository.UtilisateurRepository;
import com.app.pomodoroapp.security.CurrentUserService;
import com.app.pomodoroapp.service.AppUserDetailsService;
import com.app.pomodoroapp.service.me.StatJourneeMeService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatJourneeMeController.class)
class StatJourneeMeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CurrentUserService currentUserService;

    @MockitoBean
    StatJourneeMeService statJourneeMeService;

    @MockitoBean
    StatJourneeMeMapper statJourneeMeMapper;

    @MockitoBean
    JwtUtil jwtUtil;
    @MockitoBean
    AppUserDetailsService appUserDetailsService;
    @MockitoBean
    UtilisateurRepository utilisateurRepository;

    @Test
    @WithMockUser
    void getToday_shouldReturnStatJournee() throws Exception {
        Utilisateur user = new Utilisateur();
        user.setIdUser(1L);

        StatJournee stat = new StatJournee();
        stat.setNbSessions(0);
        stat.setTempsTotal(0);
        stat.setObjectif(0);

        StatJourneeMeDto dto = new StatJourneeMeDto();
        dto.setNbSessions(0);
        dto.setTempsTotal(0);
        dto.setObjectif(0);

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(statJourneeMeService.getOrCreateToday(user)).thenReturn(stat);
        when(statJourneeMeMapper.toDto(stat)).thenReturn(dto);

        mockMvc.perform(get("/api/stat-journees/me/today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nbSessions").value(0))
                .andExpect(jsonPath("$.tempsTotal").value(0))
                .andExpect(jsonPath("$.objectif").value(0));
    }
}
