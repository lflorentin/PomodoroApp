package com.app.pomodoroapp.controller.me;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.app.pomodoroapp.dto.me.taches.TacheMeCreateDto;
import com.app.pomodoroapp.dto.me.taches.TacheMeDto;
import com.app.pomodoroapp.dto.me.taches.TacheMeUpdateDto;
import com.app.pomodoroapp.security.CurrentUserService;
import com.app.pomodoroapp.service.me.TacheMeService;

@RestController
@RequestMapping("/api/taches/me")
public class TacheMeController {

    private final TacheMeService service;
    private final CurrentUserService currentUser;

    public TacheMeController(TacheMeService service, CurrentUserService currentUser) {
        this.service = service;
        this.currentUser = currentUser;
    }

    @GetMapping
    public List<TacheMeDto> getMyTaches() {
        return service.getAll(currentUser.getCurrentUserId());
    }

    @GetMapping("/{id}")
    public TacheMeDto getMyTache(@PathVariable Long id) {
        return service.getOne(currentUser.getCurrentUserId(), id);
    }

    @PostMapping
    public TacheMeDto create(@RequestBody TacheMeCreateDto dto) {
        return service.create(currentUser.getCurrentUserId(), dto);
    }

    @PatchMapping("/{id}")
    public TacheMeDto updatePartial(@PathVariable Long id, @RequestBody TacheMeUpdateDto dto) {
        return service.updatePartial(currentUser.getCurrentUserId(), id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(currentUser.getCurrentUserId(), id);
    }

    @PatchMapping("/{id}/reorder")
    public TacheMeDto reorder(@PathVariable Long id, @RequestParam int newPosition) {
        return service.reorder(currentUser.getCurrentUserId(), id, newPosition);
    }
}
