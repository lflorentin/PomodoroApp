package com.app.pomodoroapp.mapper.admin;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.app.pomodoroapp.dto.admin.session.SessionPomodoroCreateDto;
import com.app.pomodoroapp.dto.admin.session.SessionPomodoroDto;
import com.app.pomodoroapp.entity.SessionPomodoro;

@Mapper(componentModel = "spring")
public interface SessionPomodoroMapper {

    @Mapping(source = "tache.idTache", target = "tacheId")
    @Mapping(source = "statJournee.idStatJournee", target = "statJourneeId")
    @Mapping(source = "utilisateur.idUser", target = "utilisateurId")
    SessionPomodoroDto toDto(SessionPomodoro entity);

    @Mapping(target = "idSession", ignore = true)
    @Mapping(target = "tache", ignore = true)
    @Mapping(target = "statJournee", ignore = true)
    @Mapping(target = "utilisateur", ignore = true)
    SessionPomodoro toEntity(SessionPomodoroCreateDto dto);
}
