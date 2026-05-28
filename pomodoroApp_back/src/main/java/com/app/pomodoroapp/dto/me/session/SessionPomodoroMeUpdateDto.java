package com.app.pomodoroapp.dto.me.session;

import lombok.Data;
import java.time.OffsetDateTime;
import com.app.pomodoroapp.entity.SessionPomodoro;

@Data
public class SessionPomodoroMeUpdateDto {
    private Integer duree;
    private Integer dureeReelle;
    private OffsetDateTime heureDebut;
    private OffsetDateTime heureFin;
    private Long tacheId;
    private SessionPomodoro.SessionStatus status;
    private SessionPomodoro.SessionType type;
}
