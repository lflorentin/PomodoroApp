package com.app.pomodoroapp.mapper.me;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.app.pomodoroapp.dto.me.session.SessionPomodoroMeDto;
import com.app.pomodoroapp.entity.SessionPomodoro;

@Mapper(componentModel = "spring")
public interface SessionPomodoroMeMapper {

    @Mapping(source = "tache.idTache", target = "tacheId")
    @Mapping(source = "dureeReelle", target = "dureeReelle")
    SessionPomodoroMeDto toDto(SessionPomodoro entity);
}
