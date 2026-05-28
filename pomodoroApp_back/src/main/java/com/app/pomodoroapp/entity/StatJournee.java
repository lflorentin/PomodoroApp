package com.app.pomodoroapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "stat_journee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatJournee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStatJournee;

    private LocalDate date;

    private Integer nbSessions = 0;

    private Integer tempsTotal = 0;

    private Integer objectif = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;
}
