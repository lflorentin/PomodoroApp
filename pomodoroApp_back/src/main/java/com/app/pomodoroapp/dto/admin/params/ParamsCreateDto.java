package com.app.pomodoroapp.dto.admin.params;

import lombok.Data;

@Data
public class ParamsCreateDto {
    private Boolean sonActif;
    private String theme;
    private Long utilisateurId;
}
