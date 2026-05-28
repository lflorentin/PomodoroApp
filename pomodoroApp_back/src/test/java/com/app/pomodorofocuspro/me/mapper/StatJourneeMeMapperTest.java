package com.app.pomodoroapp.me.mapper;

import com.app.pomodoroapp.dto.me.stat.StatJourneeMeDto;
import com.app.pomodoroapp.entity.StatJournee;
import com.app.pomodoroapp.mapper.me.StatJourneeMeMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StatJourneeMeMapperTest {

    private final StatJourneeMeMapper mapper = Mappers.getMapper(StatJourneeMeMapper.class);

    @Test
    void shouldMapBasicFields() {
        StatJournee entity = new StatJournee();
        entity.setIdStatJournee(10L);
        entity.setDate(LocalDate.of(2024, 12, 12));
        entity.setNbSessions(5);
        entity.setTempsTotal(120);
        entity.setObjectif(10);

        StatJourneeMeDto dto = mapper.toDto(entity);

        assertEquals(LocalDate.of(2024, 12, 12), dto.getDate());
        assertEquals(5, dto.getNbSessions());
        assertEquals(120, dto.getTempsTotal());
        assertEquals(10, dto.getObjectif());
    }
}
