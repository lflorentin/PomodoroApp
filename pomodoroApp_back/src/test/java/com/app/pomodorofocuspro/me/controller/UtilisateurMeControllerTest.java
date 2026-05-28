package com.app.pomodoroapp.me.controller;

import com.app.pomodoroapp.config.JwtUtil;
import com.app.pomodoroapp.controller.me.UtilisateurMeController;
import com.app.pomodoroapp.dto.me.utilisateur.UtilisateurMeDto;
import com.app.pomodoroapp.dto.me.utilisateur.UtilisateurMePasswordUpdateDto;
import com.app.pomodoroapp.dto.me.utilisateur.UtilisateurMeUpdateDto;
import com.app.pomodoroapp.repository.UtilisateurRepository;
import com.app.pomodoroapp.security.CurrentUserService;
import com.app.pomodoroapp.service.AppUserDetailsService;
import com.app.pomodoroapp.service.me.UtilisateurMeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UtilisateurMeController.class)
class UtilisateurMeControllerTest {

        @Autowired
        MockMvc mockMvc;

        @Autowired
        ObjectMapper objectMapper;

        @MockitoBean
        CurrentUserService currentUserService;

        @MockitoBean
        UtilisateurMeService utilisateurMeService;

        @MockitoBean
        JwtUtil jwtUtil;
        @MockitoBean
        AppUserDetailsService appUserDetailsService;
        @MockitoBean
        UtilisateurRepository utilisateurRepository;

        /* -------------------- GET /me -------------------- */
        @Test
        @WithMockUser
        void getProfile_shouldReturnDto() throws Exception {

                UtilisateurMeDto dto = new UtilisateurMeDto();
                dto.setEmail("test@mail.com");

                when(currentUserService.getCurrentUserId()).thenReturn(1L);
                when(utilisateurMeService.getProfile(1L)).thenReturn(dto);

                mockMvc.perform(get("/api/users/me"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.email").value("test@mail.com"));
        }

        @Test
        @WithMockUser
        void getProfile_shouldReturn404_whenUserNotFound() throws Exception {

                when(currentUserService.getCurrentUserId()).thenReturn(1L);
                when(utilisateurMeService.getProfile(1L))
                                .thenThrow(new EntityNotFoundException("Utilisateur non trouvé"));

                mockMvc.perform(get("/api/users/me"))
                                .andExpect(status().isNotFound());
        }

        /* -------------------- PUT /me -------------------- */
        @Test
        @WithMockUser
        void updateProfile_shouldReturnUpdatedDto() throws Exception {

                UtilisateurMeUpdateDto updateDto = new UtilisateurMeUpdateDto();
                updateDto.setEmail("new@mail.com");

                UtilisateurMeDto returnedDto = new UtilisateurMeDto();
                returnedDto.setEmail("new@mail.com");

                when(currentUserService.getCurrentUserId()).thenReturn(1L);
                when(utilisateurMeService.updateProfile(eq(1L), any()))
                                .thenReturn(returnedDto);

                mockMvc.perform(put("/api/users/me")
                                .with(csrf())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updateDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.email").value("new@mail.com"));
        }

        @Test
        @WithMockUser
        void updateProfile_shouldReturn409_whenEmailAlreadyUsed() throws Exception {

                UtilisateurMeUpdateDto updateDto = new UtilisateurMeUpdateDto();
                updateDto.setEmail("used@mail.com");

                when(currentUserService.getCurrentUserId()).thenReturn(1L);
                when(utilisateurMeService.updateProfile(eq(1L), any()))
                                .thenThrow(new IllegalStateException("Cet email est déjà utilisé"));

                mockMvc.perform(put("/api/users/me")
                                .with(csrf())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updateDto)))
                                .andExpect(status().isConflict());
        }

        /* -------------------- PATCH /me/password -------------------- */
        @Test
        @WithMockUser
        void updatePassword_shouldReturn204() throws Exception {
                UtilisateurMePasswordUpdateDto dto = new UtilisateurMePasswordUpdateDto();
                dto.setOldPassword("old");
                dto.setNewPassword("new");

                when(currentUserService.getCurrentUserId()).thenReturn(1L);
                doNothing().when(utilisateurMeService).updatePassword(eq(1L), any());

                mockMvc.perform(patch("/api/users/me/password")
                                .with(csrf())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser
        void updatePassword_shouldReturn409_whenOldPasswordInvalid() throws Exception {
                UtilisateurMePasswordUpdateDto dto = new UtilisateurMePasswordUpdateDto();
                dto.setOldPassword("wrong");
                dto.setNewPassword("new");

                when(currentUserService.getCurrentUserId()).thenReturn(1L);
                doThrow(new IllegalStateException("Ancien mot de passe incorrect"))
                                .when(utilisateurMeService)
                                .updatePassword(eq(1L), any());

                mockMvc.perform(patch("/api/users/me/password")
                                .with(csrf())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isConflict());
        }
}
