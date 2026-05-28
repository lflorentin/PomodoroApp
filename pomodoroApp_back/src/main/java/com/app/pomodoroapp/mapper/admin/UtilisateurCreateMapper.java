package com.app.pomodoroapp.mapper.admin;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.app.pomodoroapp.dto.admin.utilisateur.UtilisateurCreateDto;
import com.app.pomodoroapp.entity.Utilisateur;

@Mapper(componentModel = "spring")
public interface UtilisateurCreateMapper {

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "dateCreation", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "role", ignore = true)
    Utilisateur toEntity(UtilisateurCreateDto dto);
}
