package com.app.pomodoroapp.controller.me;

import com.app.pomodoroapp.dto.me.utilisateur.UtilisateurMeDto;
import com.app.pomodoroapp.dto.me.utilisateur.UtilisateurMeUpdateDto;
import com.app.pomodoroapp.dto.me.utilisateur.UtilisateurMePasswordUpdateDto;
import com.app.pomodoroapp.security.CurrentUserService;
import com.app.pomodoroapp.service.me.UtilisateurMeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me")
public class UtilisateurMeController {

    private final UtilisateurMeService service;
    private final CurrentUserService currentUserService;

    public UtilisateurMeController(UtilisateurMeService service, CurrentUserService currentUserService) {
        this.service = service;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<UtilisateurMeDto> getMyProfile() {
        Long userId = currentUserService.getCurrentUserId();
        return ResponseEntity.ok(service.getProfile(userId));
    }

    /* PUT  email/prénom/nom */
    @PutMapping
    public ResponseEntity<UtilisateurMeDto> updateMyProfile(
            @RequestBody UtilisateurMeUpdateDto dto) {

        Long userId = currentUserService.getCurrentUserId();
        UtilisateurMeDto updated = service.updateProfile(userId, dto);
        return ResponseEntity.ok(updated);
    }

    /* PATCH  mot de passe */
    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody UtilisateurMePasswordUpdateDto dto) {
        Long userId = currentUserService.getCurrentUserId();
        service.updatePassword(userId, dto);
        return ResponseEntity.noContent().build();
    }
}
