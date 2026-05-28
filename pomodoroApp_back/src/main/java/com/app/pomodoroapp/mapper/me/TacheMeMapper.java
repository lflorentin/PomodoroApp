package com.app.pomodoroapp.mapper.me;

import org.mapstruct.Mapper;

import com.app.pomodoroapp.dto.me.taches.TacheMeDto;
import com.app.pomodoroapp.entity.Tache;

@Mapper(componentModel = "spring")
public interface TacheMeMapper {
    TacheMeDto toDto(Tache entity);
}
