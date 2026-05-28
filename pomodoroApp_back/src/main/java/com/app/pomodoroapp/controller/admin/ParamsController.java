package com.app.pomodoroapp.controller.admin;

import com.app.pomodoroapp.dto.admin.params.ParamsCreateDto;
import com.app.pomodoroapp.dto.admin.params.ParamsDto;
import com.app.pomodoroapp.dto.admin.params.ParamsUpdateDto;
import com.app.pomodoroapp.service.admin.ParamsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/params")
public class ParamsController {

    private final ParamsService service;

    public ParamsController(ParamsService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ParamsDto> getAll() {
        return service.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ParamsDto> getById(@PathVariable Long id) {
        ParamsDto dto = service.getById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ParamsDto create(@RequestBody ParamsCreateDto dto) {
        return service.create(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ParamsDto> update(@PathVariable Long id, @RequestBody ParamsDto dto) {
        ParamsDto updated = service.updateFull(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ParamsDto patchUpdate(
            @PathVariable Long id,
            @RequestBody ParamsUpdateDto dto) {
        return service.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
