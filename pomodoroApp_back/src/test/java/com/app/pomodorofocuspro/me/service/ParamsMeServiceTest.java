package com.app.pomodoroapp.me.service;

import com.app.pomodoroapp.dto.me.params.ParamsMeDto;
import com.app.pomodoroapp.dto.me.params.ParamsMeUpdateDto;
import com.app.pomodoroapp.entity.Params;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.mapper.me.ParamsMeMapper;
import com.app.pomodoroapp.repository.ParamsRepository;
import com.app.pomodoroapp.repository.UtilisateurRepository;
import com.app.pomodoroapp.service.me.ParamsMeService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParamsMeServiceTest {

    @Mock
    ParamsRepository paramsRepository;

    @Mock
    UtilisateurRepository utilisateurRepository;

    @Mock
    ParamsMeMapper mapper;

    @InjectMocks
    ParamsMeService service;

    @Test
    void getForUser_shouldReturnExistingParams() {
        Params params = new Params();
        ParamsMeDto dto = new ParamsMeDto();

        when(paramsRepository.findByUtilisateurIdUser(1L))
                .thenReturn(Optional.of(params));
        when(mapper.toDto(params)).thenReturn(dto);

        ParamsMeDto result = service.getForUser(1L);

        assertThat(result).isNotNull();
        verify(utilisateurRepository, never()).findById(any());
    }

    @Test
    void getForUser_shouldCreateDefaultParams_whenNotExisting() {
        Utilisateur user = new Utilisateur();
        Params params = new Params();
        ParamsMeDto dto = new ParamsMeDto();

        when(paramsRepository.findByUtilisateurIdUser(1L))
                .thenReturn(Optional.empty());
        when(utilisateurRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(paramsRepository.save(any())).thenReturn(params);
        when(mapper.toDto(params)).thenReturn(dto);

        ParamsMeDto result = service.getForUser(1L);

        assertThat(result).isNotNull();
        verify(paramsRepository).save(any());
    }

    @Test
    void updateForUser_shouldUpdateAllFields() {
        Params params = new Params();
        ParamsMeDto dto = new ParamsMeDto();

        ParamsMeUpdateDto update = new ParamsMeUpdateDto();
        update.setTheme("dark");
        update.setSonActif(false);
        update.setDureeTravail(20);
        update.setDureePauseCourte(7);
        update.setDureePauseLongue(25);
        update.setObjectifSessions(14);

        when(paramsRepository.findByUtilisateurIdUser(1L))
                .thenReturn(Optional.of(params));
        when(paramsRepository.save(params)).thenReturn(params);
        when(mapper.toDto(params)).thenReturn(dto);

        ParamsMeDto result = service.updateForUser(1L, update);

        assertThat(result).isNotNull();
        assertThat(params.getTheme()).isEqualTo("dark");
        assertThat(params.getSonActif()).isFalse();
        assertThat(params.getDureeTravail()).isEqualTo(20);
        assertThat(params.getDureePauseCourte()).isEqualTo(7);
        assertThat(params.getDureePauseLongue()).isEqualTo(25);
        assertThat(params.getObjectifSessions()).isEqualTo(14);
    }

    @Test
    void patchForUser_shouldUpdateOnlyProvidedFields() {
        Params params = new Params();
        params.setTheme("light");
        params.setSonActif(true);

        ParamsMeDto dto = new ParamsMeDto();

        ParamsMeUpdateDto patch = new ParamsMeUpdateDto();
        patch.setTheme("dark");

        when(paramsRepository.findByUtilisateurIdUser(1L))
                .thenReturn(Optional.of(params));
        when(paramsRepository.save(params)).thenReturn(params);
        when(mapper.toDto(params)).thenReturn(dto);

        ParamsMeDto result = service.patchForUser(1L, patch);

        assertThat(result).isNotNull();
        assertThat(params.getTheme()).isEqualTo("dark");
        assertThat(params.getSonActif()).isTrue();
    }
}
