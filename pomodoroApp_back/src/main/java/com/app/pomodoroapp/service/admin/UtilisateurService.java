package com.app.pomodoroapp.service.admin;

import com.app.pomodoroapp.dto.admin.utilisateur.UtilisateurCreateDto;
import com.app.pomodoroapp.dto.admin.utilisateur.UtilisateurDto;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.mapper.admin.UtilisateurMapper;
import com.app.pomodoroapp.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper,
            PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UtilisateurDto> getAllUtilisateurs() {
        return utilisateurRepository.findAll()
                .stream()
                .map(utilisateurMapper::toDto)
                .collect(Collectors.toList());
    }

    public UtilisateurDto getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id)
                .map(utilisateurMapper::toDto)
                .orElse(null);
    }

    public UtilisateurDto createUtilisateur(UtilisateurDto dto) {
        Utilisateur utilisateur = utilisateurMapper.toEntity(dto);
        Utilisateur saved = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.toDto(saved);
    }

    public UtilisateurDto updateUtilisateur(Long id, UtilisateurDto dto) {
        return utilisateurRepository.findById(id).map(existing -> {
            existing.setPrenom(dto.getPrenom());
            existing.setNom(dto.getNom());
            existing.setEmail(dto.getEmail());
            Utilisateur saved = utilisateurRepository.save(existing);
            return utilisateurMapper.toDto(saved);
        }).orElse(null);
    }

    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }

    public Utilisateur createFromCreateDto(UtilisateurCreateDto dto) {

        if (utilisateurRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        Utilisateur u = new Utilisateur();
        u.setNom(dto.getNom());
        u.setPrenom(dto.getPrenom());
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));

        return utilisateurRepository.save(u);
    }

}
