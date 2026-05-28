package com.app.pomodoroapp.dto.admin.utilisateur;

import lombok.Data;

@Data
public class UtilisateurCreateDto {
    private String prenom;
    private String nom;
    private String email;
    private String password;
}
