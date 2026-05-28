package com.app.pomodoroapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "params")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Params {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idParams;

    private Boolean sonActif = true;

    private String theme = "light";

    private int dureeTravail = 25;
    private int dureePauseCourte = 5;
    private int dureePauseLongue = 15;
    private int objectifSessions = 8;

    @OneToOne
    @JoinColumn(name = "id_user")
    private Utilisateur utilisateur;
}
