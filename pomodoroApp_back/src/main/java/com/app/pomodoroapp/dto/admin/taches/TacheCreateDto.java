package com.app.pomodoroapp.dto.admin.taches;

import lombok.Data;

@Data
public class TacheCreateDto {
    private String titre;
    private String description;
    private Boolean validation;
    private Integer position;
    private Long utilisateurId;
}