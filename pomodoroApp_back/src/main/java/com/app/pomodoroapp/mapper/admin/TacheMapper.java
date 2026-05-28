package com.app.pomodoroapp.mapper.admin;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.app.pomodoroapp.dto.admin.taches.TacheCreateDto;
import com.app.pomodoroapp.dto.admin.taches.TacheDto;
import com.app.pomodoroapp.entity.Tache;

@Mapper(componentModel = "spring")
public interface TacheMapper {

    TacheDto toDto(Tache entity);

    @Mapping(target = "idTache", ignore = true)
    @Mapping(target = "validation", expression = "java(false)")
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "utilisateur", ignore = true) 
    Tache toEntity(TacheCreateDto dto);
}
