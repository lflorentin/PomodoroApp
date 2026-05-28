package com.app.pomodoroapp.controller.admin;

import com.app.pomodoroapp.dto.admin.utilisateur.UtilisateurCreateDto;
import com.app.pomodoroapp.dto.admin.utilisateur.UtilisateurDto;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.mapper.admin.UtilisateurMapper;
import com.app.pomodoroapp.service.admin.UtilisateurService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final UtilisateurMapper utilisateurMapper;

    public UtilisateurController(UtilisateurService utilisateurService, UtilisateurMapper utilisateurMapper) {
        this.utilisateurService = utilisateurService;
        this.utilisateurMapper = utilisateurMapper;
    }

    @GetMapping
    public List<UtilisateurDto> getAll() {
        return utilisateurService.getAllUtilisateurs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDto> getById(@PathVariable Long id) {
        UtilisateurDto dto = utilisateurService.getUtilisateurById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public UtilisateurDto create(@RequestBody UtilisateurCreateDto dto) {
        Utilisateur u = utilisateurService.createFromCreateDto(dto);
        return utilisateurMapper.toDto(u);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDto> update(@PathVariable Long id, @RequestBody UtilisateurDto dto) {
        UtilisateurDto updated = utilisateurService.updateUtilisateur(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.noContent().build();
    }
}
