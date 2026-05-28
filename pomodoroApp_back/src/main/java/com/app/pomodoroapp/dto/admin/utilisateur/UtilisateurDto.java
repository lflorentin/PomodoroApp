package com.app.pomodoroapp.dto.admin.utilisateur;

import lombok.Data;

@Data
public class UtilisateurDto {
    private Long idUser;
    private String prenom;
    private String nom;
    private String email;
}