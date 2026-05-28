package com.app.pomodoroapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "tache")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTache;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean validation = false;

    private String titre;

    @Column(nullable = false)
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private Utilisateur utilisateur;

}
