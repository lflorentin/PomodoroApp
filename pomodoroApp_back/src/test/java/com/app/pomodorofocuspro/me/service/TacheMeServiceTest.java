package com.app.pomodoroapp.me.service;

import com.app.pomodoroapp.dto.me.taches.*;
import com.app.pomodoroapp.entity.*;
import com.app.pomodoroapp.mapper.me.TacheMeMapper;
import com.app.pomodoroapp.repository.TacheRepository;
import com.app.pomodoroapp.repository.UtilisateurRepository;
import com.app.pomodoroapp.service.me.TacheMeService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TacheMeServiceTest {

    @Mock
    TacheRepository tacheRepository;

    @Mock
    UtilisateurRepository utilisateurRepository;

    @Mock
    TacheMeMapper mapper;

    @InjectMocks
    TacheMeService service;

    /* -------------------- GET ALL -------------------- */
    @Test
    void getAll_shouldReturnDtos() {
        Tache t1 = new Tache();
        Tache t2 = new Tache();

        when(tacheRepository.findByUtilisateurIdUserOrderByPositionAsc(1L))
                .thenReturn(List.of(t1, t2));
        when(mapper.toDto(any())).thenReturn(new TacheMeDto());

        List<TacheMeDto> result = service.getAll(1L);

        assertThat(result).hasSize(2);
    }

    /* -------------------- GET ONE -------------------- */
    @Test
    void getOne_shouldReturnDto() {
        Tache tache = new Tache();

        when(tacheRepository.findByIdTacheAndUtilisateurIdUser(1L, 1L))
                .thenReturn(Optional.of(tache));
        when(mapper.toDto(tache)).thenReturn(new TacheMeDto());

        TacheMeDto dto = service.getOne(1L, 1L);

        assertThat(dto).isNotNull();
    }

    /* -------------------- CREATE -------------------- */
    @Test
    void create_shouldCreateTaskAtEnd() {
        Utilisateur user = new Utilisateur();
        user.setIdUser(1L);

        TacheMeCreateDto dto = new TacheMeCreateDto();
        dto.setTitre("Titre");

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(user));
        when(tacheRepository.findMaxPositionByUtilisateur(1L)).thenReturn(2);
        when(tacheRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(mapper.toDto(any())).thenReturn(new TacheMeDto());

        TacheMeDto result = service.create(1L, dto);

        assertThat(result).isNotNull();
    }

    /* -------------------- UPDATE -------------------- */
    @Test
    void updatePartial_shouldUpdateFields() {
        Tache tache = new Tache();

        TacheMeUpdateDto dto = new TacheMeUpdateDto();
        dto.setTitre("New");

        when(tacheRepository.findByIdTacheAndUtilisateurIdUser(1L, 1L))
                .thenReturn(Optional.of(tache));
        when(tacheRepository.save(any())).thenReturn(tache);
        when(mapper.toDto(any())).thenReturn(new TacheMeDto());

        TacheMeDto result = service.updatePartial(1L, 1L, dto);

        assertThat(result).isNotNull();
    }

    /* -------------------- DELETE -------------------- */
    @Test
    void delete_shouldRemoveTask() {
        Tache tache = new Tache();

        when(tacheRepository.findByIdTacheAndUtilisateurIdUser(1L, 1L))
                .thenReturn(Optional.of(tache));

        service.delete(1L, 1L);

        verify(tacheRepository).delete(tache);
    }
}
