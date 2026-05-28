package com.app.pomodoroapp.dto.me.params;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import lombok.Data;

@Data
public class ParamsMeDto {
    private Long idParams;
    private Boolean sonActif;
    private String theme;
    @NotNull
    @Min(1)
    private Integer dureeTravail;
    @NotNull
    @Min(1)
    private Integer dureePauseCourte;
    @NotNull
    @Min(1)
    private Integer dureePauseLongue;
    @Min(1)
    private Integer objectifSessions;

}
