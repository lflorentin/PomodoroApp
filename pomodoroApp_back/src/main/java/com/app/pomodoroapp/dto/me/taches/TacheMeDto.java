package com.app.pomodoroapp.dto.me.taches;

import lombok.Data;

@Data
public class TacheMeDto {
    private Long idTache;
    private String titre;
    private String description;
    private Boolean validation;
    private Integer position;
}
