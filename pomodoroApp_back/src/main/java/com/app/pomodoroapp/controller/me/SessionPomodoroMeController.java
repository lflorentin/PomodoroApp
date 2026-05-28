package com.app.pomodoroapp.controller.me;

import com.app.pomodoroapp.dto.me.session.*;
import com.app.pomodoroapp.security.CurrentUserService;
import com.app.pomodoroapp.service.me.SessionPomodoroMeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions/me")
public class SessionPomodoroMeController {

    private final SessionPomodoroMeService service;
    private final CurrentUserService currentUserService;

    public SessionPomodoroMeController(SessionPomodoroMeService service, CurrentUserService currentUserService) {
        this.service = service;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<SessionPomodoroMeDto> getAllMySessions() {
        return service.getAllForUser(currentUserService.getCurrentUserId());
    }

    @GetMapping("/today")
    public List<SessionPomodoroMeDto> getToday() {
        return service.getToday(currentUserService.getCurrentUserId());
    }

    @GetMapping("/current")
    public ResponseEntity<SessionPomodoroMeDto> getCurrent() {
        SessionPomodoroMeDto dto = service.getCurrent(currentUserService.getCurrentUserId());
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.noContent().build();
    }

    @PostMapping
    public SessionPomodoroMeDto create(@RequestBody SessionPomodoroMeCreateDto dto) {
        return service.create(currentUserService.getCurrentUserId(), dto);
    }

    @PatchMapping("/{id}")
    public SessionPomodoroMeDto update(
            @PathVariable Long id,
            @RequestBody SessionPomodoroMeUpdateDto dto) {
        return service.patch(currentUserService.getCurrentUserId(), id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(currentUserService.getCurrentUserId(), id);
        return ResponseEntity.noContent().build();
    }
}
