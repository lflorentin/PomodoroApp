package com.app.pomodoroapp.dto.me.utilisateur;

import lombok.Data;

@Data
public class UtilisateurMePasswordUpdateDto {
    private String oldPassword;
    private String newPassword;
}
