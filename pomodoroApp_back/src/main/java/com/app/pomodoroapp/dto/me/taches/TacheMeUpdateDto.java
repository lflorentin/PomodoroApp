package com.app.pomodoroapp.dto.me.taches;

import lombok.Data;

@Data
public class TacheMeUpdateDto {
    private String titre;
    private String description;
    private Boolean validation;
}
