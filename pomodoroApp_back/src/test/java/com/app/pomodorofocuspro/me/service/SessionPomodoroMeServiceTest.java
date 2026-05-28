package com.app.pomodoroapp.me.service;

import com.app.pomodoroapp.dto.me.session.*;
import com.app.pomodoroapp.entity.*;
import com.app.pomodoroapp.mapper.me.SessionPomodoroMeMapper;
import com.app.pomodoroapp.repository.*;
import com.app.pomodoroapp.service.admin.StatJourneeService;
import com.app.pomodoroapp.service.me.SessionPomodoroMeService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionPomodoroMeServiceTest {

    @Mock
    SessionPomodoroRepository repository;
    @Mock
    TacheRepository tacheRepository;
    @Mock
    UtilisateurRepository utilisateurRepository;
    @Mock
    StatJourneeRepository statJourneeRepository;
    @Mock
    StatJourneeService statJourneeService;
    @Mock
    SessionPomodoroMeMapper mapper;

    @InjectMocks
    SessionPomodoroMeService service;

    /* -------------------- GET CURRENT -------------------- */
    @Test
    void getCurrent_shouldReturnDto_whenActiveSessionExists() {
        SessionPomodoro session = new SessionPomodoro();

        when(repository.findCurrentByUser(1L, SessionPomodoro.SessionStatus.en_cours))
                .thenReturn(session);
        when(mapper.toDto(session)).thenReturn(new SessionPomodoroMeDto());

        SessionPomodoroMeDto dto = service.getCurrent(1L);

        assertThat(dto).isNotNull();
    }

    /* -------------------- CREATE -------------------- */
    @Test
    void create_shouldCreateSession_andRecalculateStatJournee() {
        Utilisateur user = new Utilisateur();
        user.setIdUser(1L);

        SessionPomodoroMeCreateDto dto = new SessionPomodoroMeCreateDto();
        dto.setStatus(SessionPomodoro.SessionStatus.completee);
        dto.setType(SessionPomodoro.SessionType.travail);
        dto.setDuree(25);
        dto.setHeureDebut(OffsetDateTime.now());
        dto.setHeureFin(OffsetDateTime.now().plusMinutes(25));

        StatJournee stat = new StatJournee();

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(user));
        when(statJourneeRepository.findByUtilisateurIdUserAndDate(any(), any()))
                .thenReturn(Optional.of(stat));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(mapper.toDto(any())).thenReturn(new SessionPomodoroMeDto());

        SessionPomodoroMeDto result = service.create(1L, dto);

        assertThat(result).isNotNull();
        verify(statJourneeService).recalculateStatJournee(eq(1L), any());
    }

    /* -------------------- CREATE (en_cours uniqueness) -------------------- */
    @Test
    void create_shouldFail_whenAnotherSessionIsActive() {
        Utilisateur user = new Utilisateur();
        user.setIdUser(1L);

        SessionPomodoroMeCreateDto dto = new SessionPomodoroMeCreateDto();
        dto.setStatus(SessionPomodoro.SessionStatus.en_cours);
        dto.setHeureDebut(OffsetDateTime.now());

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.findCurrentByUser(1L, SessionPomodoro.SessionStatus.en_cours))
                .thenReturn(new SessionPomodoro());

        assertThatThrownBy(() -> service.create(1L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("déjà en cours");
    }

    /* -------------------- DELETE -------------------- */
    @Test
    void delete_shouldRemoveSession_andRecalculateStatJournee() {
        Utilisateur user = new Utilisateur();
        user.setIdUser(1L);

        SessionPomodoro session = new SessionPomodoro();
        session.setUtilisateur(user);
        session.setHeureDebut(OffsetDateTime.now());

        when(repository.findById(1L)).thenReturn(Optional.of(session));

        service.delete(1L, 1L);

        verify(repository).delete(session);
        verify(statJourneeService).recalculateStatJournee(eq(1L), any());
    }
}
