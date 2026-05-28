package com.app.pomodoroapp.mapper.admin;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.app.pomodoroapp.dto.admin.utilisateur.UtilisateurDto;
import com.app.pomodoroapp.entity.Utilisateur;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

    UtilisateurDto toDto(Utilisateur entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "role", ignore = true)
    Utilisateur toEntity(UtilisateurDto dto);
}
