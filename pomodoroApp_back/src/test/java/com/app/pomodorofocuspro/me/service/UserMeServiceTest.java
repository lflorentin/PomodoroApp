package com.app.pomodoroapp.me.service;

import com.app.pomodoroapp.dto.me.utilisateur.*;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.mapper.me.UtilisateurMeMapper;
import com.app.pomodoroapp.repository.UtilisateurRepository;
import com.app.pomodoroapp.service.me.UtilisateurMeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UtilisateurMeServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private UtilisateurMeMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UtilisateurMeService service;

    private Utilisateur user;

    @BeforeEach
    void setup() {
        user = new Utilisateur();
        user.setIdUser(1L);
        user.setEmail("john@test.com");
        user.setPrenom("John");
        user.setNom("Doe");
        user.setPassword("hashed");
    }

    /* -------------------- getProfile -------------------- */
    @Test
    void shouldReturnProfile() {
        UtilisateurMeDto dto = new UtilisateurMeDto();
        dto.setEmail("john@test.com");

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.toDto(user)).thenReturn(dto);

        UtilisateurMeDto result = service.getProfile(1L);

        assertEquals("john@test.com", result.getEmail());
    }

    /* -------------------- udpateProfile -------------------- */
    @Test
    void shouldUpdateProfile() {
        UtilisateurMeUpdateDto updateDto = new UtilisateurMeUpdateDto();
        updateDto.setPrenom("Jane");
        updateDto.setEmail("jane@test.com");

        UtilisateurMeDto returnedDto = new UtilisateurMeDto();
        returnedDto.setPrenom("Jane");
        returnedDto.setEmail("jane@test.com");

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(user));
        when(utilisateurRepository.existsByEmail("jane@test.com")).thenReturn(false);
        when(utilisateurRepository.save(user)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(returnedDto);

        UtilisateurMeDto updated = service.updateProfile(1L, updateDto);

        assertEquals("Jane", updated.getPrenom());
        assertEquals("jane@test.com", updated.getEmail());
    }


    /* -------------------- updatePassword -------------------- */
    @Test
    void shouldUpdatePassword() {
        UtilisateurMePasswordUpdateDto dto = new UtilisateurMePasswordUpdateDto();
        dto.setOldPassword("oldPass");
        dto.setNewPassword("newPass");

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPass", "hashed")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("newHashed");

        service.updatePassword(1L, dto);

        verify(passwordEncoder).encode("newPass");
        verify(utilisateurRepository).save(user);
        assertEquals("newHashed", user.getPassword());
    }
}
