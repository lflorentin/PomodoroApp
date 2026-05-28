package com.app.pomodoroapp.dto.me.params;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ParamsMeUpdateDto {
    private Boolean sonActif;
    private String theme;
    @Min(1)
    private Integer dureeTravail;
    @Min(1)
    private Integer dureePauseCourte;
    @Min(1)
    private Integer dureePauseLongue;
    private Integer objectifSessions;
}
