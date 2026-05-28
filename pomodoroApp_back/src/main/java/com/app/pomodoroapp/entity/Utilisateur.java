package com.app.pomodoroapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "utilisateurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private OffsetDateTime dateCreation = OffsetDateTime.now();

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role = "USER";

}