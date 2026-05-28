package com.app.pomodoroapp.dto.admin.session;

import lombok.Data;
import java.time.OffsetDateTime;
import com.app.pomodoroapp.entity.SessionPomodoro;

@Data
public class SessionPomodoroUpdateDto {

    private Integer duree;
    private Integer dureeReelle;
    private OffsetDateTime heureDebut;
    private OffsetDateTime heureFin;

    private Long tacheId;
    private Long statJourneeId;

    private SessionPomodoro.SessionStatus status;
    private SessionPomodoro.SessionType type;
}
