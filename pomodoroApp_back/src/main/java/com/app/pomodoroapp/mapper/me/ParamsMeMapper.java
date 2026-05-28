package com.app.pomodoroapp.mapper.me;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.app.pomodoroapp.dto.me.params.ParamsMeDto;
import com.app.pomodoroapp.dto.me.params.ParamsMeUpdateDto;
import com.app.pomodoroapp.entity.Params;

@Mapper(componentModel = "spring")
public interface ParamsMeMapper {

    @Mapping(source = "idParams", target = "idParams")
    @Mapping(source = "sonActif", target = "sonActif")
    @Mapping(source = "theme", target = "theme")
    ParamsMeDto toDto(Params entity);

    @Mapping(target = "idParams", ignore = true)
    @Mapping(target = "utilisateur", ignore = true)
    void update(@MappingTarget Params entity, ParamsMeUpdateDto dto);
}
