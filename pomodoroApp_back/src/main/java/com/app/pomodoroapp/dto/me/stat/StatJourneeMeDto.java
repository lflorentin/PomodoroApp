package com.app.pomodoroapp.dto.me.stat;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StatJourneeMeDto {
    private LocalDate date;
    private Integer nbSessions;
    private Integer tempsTotal;
    private Integer objectif;
}
