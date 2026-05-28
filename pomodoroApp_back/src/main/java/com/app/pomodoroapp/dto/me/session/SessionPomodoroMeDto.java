package com.app.pomodoroapp.dto.me.session;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class SessionPomodoroMeDto {
    private Long idSession;
    private Integer duree;
    private Integer dureeReelle;
    private OffsetDateTime heureDebut;
    private OffsetDateTime heureFin;

    private Long tacheId;
    private String status;
    private String type;
}
