package com.app.pomodoroapp.dto.admin.stat;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StatJourneeDto {
    private Long idStatJournee;
    private LocalDate date;
    private Integer nbSessions;
    private Integer tempsTotal;
    private Integer objectif;
    private Long idUser;
}