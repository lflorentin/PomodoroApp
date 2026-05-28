package com.app.pomodoroapp.service.me;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.pomodoroapp.dto.me.utilisateur.UtilisateurMeDto;
import com.app.pomodoroapp.dto.me.utilisateur.UtilisateurMeUpdateDto;
import com.app.pomodoroapp.dto.me.utilisateur.UtilisateurMePasswordUpdateDto;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.repository.UtilisateurRepository;
import com.app.pomodoroapp.mapper.me.UtilisateurMeMapper;

@Service
public class UtilisateurMeService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMeMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurMeService(
            UtilisateurRepository utilisateurRepository,
            UtilisateurMeMapper mapper,
            PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    /* -------------------- GET /me -------------------- */
    public UtilisateurMeDto getProfile(Long userId) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        return mapper.toDto(user);
    }

    /* -------------------- PUT /me ------------------- */
    @Transactional
    public UtilisateurMeDto updateProfile(Long userId, UtilisateurMeUpdateDto dto) {

        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        // Email déjà utilisé → 409
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (utilisateurRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalStateException("Cet email est déjà utilisé");
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getPrenom() != null) {
            user.setPrenom(dto.getPrenom());
        }

        if (dto.getNom() != null) {
            user.setNom(dto.getNom());
        }

        return mapper.toDto(utilisateurRepository.save(user));
    }

    /* -------------------- PATCH /me/password -------------------- */
    @Transactional
    public void updatePassword(Long userId, UtilisateurMePasswordUpdateDto dto) {

        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new IllegalStateException("Ancien mot de passe incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        utilisateurRepository.save(user);
    }
}
