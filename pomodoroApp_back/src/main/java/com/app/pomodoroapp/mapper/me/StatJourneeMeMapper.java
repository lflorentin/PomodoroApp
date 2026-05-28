package com.app.pomodoroapp.mapper.me;

import org.mapstruct.Mapper;

import com.app.pomodoroapp.dto.me.stat.StatJourneeMeDto;
import com.app.pomodoroapp.entity.StatJournee;

@Mapper(componentModel = "spring")
public interface StatJourneeMeMapper {

    StatJourneeMeDto toDto(StatJournee entity);
}
