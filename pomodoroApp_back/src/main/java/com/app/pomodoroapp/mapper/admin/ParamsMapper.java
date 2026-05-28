package com.app.pomodoroapp.mapper.admin;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.app.pomodoroapp.dto.admin.params.ParamsCreateDto;
import com.app.pomodoroapp.dto.admin.params.ParamsDto;
import com.app.pomodoroapp.entity.Params;

@Mapper(componentModel = "spring")
public interface ParamsMapper {

    @Mapping(target = "utilisateurId", source = "utilisateur.idUser")
    ParamsDto toDto(Params entity);

    @Mapping(target = "idParams", ignore = true)
    @Mapping(target = "utilisateur", ignore = true)
    Params toEntity(ParamsCreateDto dto);
}
