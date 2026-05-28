package com.app.pomodoroapp.controller.admin;

import com.app.pomodoroapp.dto.admin.taches.TacheCreateDto;
import com.app.pomodoroapp.dto.admin.taches.TacheDto;
import com.app.pomodoroapp.dto.admin.taches.TacheUpdateDto;
import com.app.pomodoroapp.service.admin.TacheService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taches")
public class TacheController {

    private final TacheService tacheService;

    public TacheController(TacheService tacheService) {
        this.tacheService = tacheService;
    }

    @GetMapping
    public List<TacheDto> getAll() {
        return tacheService.getAllTaches();
    }

    @GetMapping("/{id}")
    public TacheDto getById(@PathVariable Long id) {
        return tacheService.getTacheById(id);
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public List<TacheDto> getByUtilisateur(@PathVariable Long utilisateurId) {
        return tacheService.getTachesByUtilisateur(utilisateurId);
    }

    @PostMapping
    public TacheDto create(@RequestBody TacheCreateDto dto) {
        return tacheService.createTache(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TacheDto> update(
            @PathVariable Long id,
            @RequestBody TacheDto dto) {

        TacheDto updated = tacheService.updateFull(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TacheDto> patchUpdate(
            @PathVariable Long id,
            @RequestBody TacheUpdateDto dto) {

        TacheDto updated = tacheService.updatePartial(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tacheService.deleteTache(id);
    }

    @PatchMapping("/{id}/reorder")
    public TacheDto reorder(
            @PathVariable Long id,
            @RequestParam int newPosition) {
        return tacheService.reorder(id, newPosition);
    }

}
