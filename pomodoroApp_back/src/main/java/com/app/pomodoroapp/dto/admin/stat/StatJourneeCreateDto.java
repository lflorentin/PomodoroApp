package com.app.pomodoroapp.dto.admin.stat;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StatJourneeCreateDto {
    private LocalDate date;
    private Integer objectif;
    private Long idUser;
}