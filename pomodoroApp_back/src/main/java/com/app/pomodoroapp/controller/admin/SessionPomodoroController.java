package com.app.pomodoroapp.controller.admin;

import com.app.pomodoroapp.dto.admin.session.SessionPomodoroCreateDto;
import com.app.pomodoroapp.dto.admin.session.SessionPomodoroDto;
import com.app.pomodoroapp.dto.admin.session.SessionPomodoroUpdateDto;
import com.app.pomodoroapp.service.admin.SessionPomodoroService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionPomodoroController {

    private final SessionPomodoroService service;

    public SessionPomodoroController(SessionPomodoroService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<SessionPomodoroDto> getAll() {
        return service.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<SessionPomodoroDto> getById(@PathVariable Long id) {
        SessionPomodoroDto dto = service.getById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public SessionPomodoroDto create(@RequestBody SessionPomodoroCreateDto dto) {
        return service.create(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<SessionPomodoroDto> update(@PathVariable Long id, @RequestBody SessionPomodoroCreateDto dto) {
        SessionPomodoroDto updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<SessionPomodoroDto> patchUpdate(
            @PathVariable Long id,
            @RequestBody SessionPomodoroUpdateDto dto) {

        SessionPomodoroDto updated = service.patchUpdate(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
