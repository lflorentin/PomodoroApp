package com.app.pomodoroapp.dto.admin.session;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class SessionPomodoroDto {
    private Long idSession;
    private Integer duree;
    private Integer dureeReelle;
    private OffsetDateTime heureDebut;
    private OffsetDateTime heureFin;
    private Long tacheId;
    private Long statJourneeId;
    private Long utilisateurId;
    private String status;
    private String type;
}
