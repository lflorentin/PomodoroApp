package com.app.pomodoroapp.controller.me;

import com.app.pomodoroapp.dto.me.params.ParamsMeDto;
import com.app.pomodoroapp.dto.me.params.ParamsMeUpdateDto;
import com.app.pomodoroapp.security.CurrentUserService;
import com.app.pomodoroapp.service.me.ParamsMeService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/params/me")
public class ParamsMeController {

    private final ParamsMeService service;
    private final CurrentUserService currentUserService;

    public ParamsMeController(ParamsMeService service, CurrentUserService currentUserService) {
        this.service = service;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<ParamsMeDto> getMyParams() {
        Long userId = currentUserService.getCurrentUserId();
        return ResponseEntity.ok(service.getForUser(userId));
    }

    @PutMapping
    public ResponseEntity<ParamsMeDto> updateMyParams(@Valid @RequestBody ParamsMeUpdateDto dto) {
        Long userId = currentUserService.getCurrentUserId();
        return ResponseEntity.ok(service.updateForUser(userId, dto));
    }

    @PatchMapping
    public ResponseEntity<ParamsMeDto> patchMyParams(@Valid @RequestBody ParamsMeUpdateDto dto) {
        Long userId = currentUserService.getCurrentUserId();
        return ResponseEntity.ok(service.patchForUser(userId, dto));
    }
}
