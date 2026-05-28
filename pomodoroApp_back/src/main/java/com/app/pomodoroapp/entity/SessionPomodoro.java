package com.app.pomodoroapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionPomodoro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSession;

    private Integer duree;

    private Integer dureeReelle;

    private OffsetDateTime heureDebut;
    private OffsetDateTime heureFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tache")
    private Tache tache;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_stat_journee")
    private StatJournee statJournee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private SessionStatus status;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private SessionType type;

    public enum SessionStatus {
        completee, annulee, en_cours
    }

    public enum SessionType {
        travail, pause
    }
}
