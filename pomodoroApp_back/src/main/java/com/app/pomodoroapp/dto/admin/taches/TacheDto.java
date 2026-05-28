package com.app.pomodoroapp.dto.admin.taches;

import lombok.Data;

@Data
public class TacheDto {
    private Long idTache;
    private String titre;
    private String description;
    private Boolean validation;
    private Integer position;
}