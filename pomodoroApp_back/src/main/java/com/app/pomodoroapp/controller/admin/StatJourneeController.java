package com.app.pomodoroapp.controller.admin;

import com.app.pomodoroapp.dto.admin.stat.StatJourneeDto;
import com.app.pomodoroapp.security.CurrentUserService;
import com.app.pomodoroapp.service.admin.StatJourneeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stat-journees")
public class StatJourneeController {

    private final StatJourneeService statJourneeService;
    private final CurrentUserService currentUserService;

    public StatJourneeController(StatJourneeService statJourneeService, CurrentUserService currentUserService) {
        this.statJourneeService = statJourneeService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<StatJourneeDto> getAll() {
        return statJourneeService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatJourneeDto> getById(@PathVariable Long id) {
        StatJourneeDto dto = statJourneeService.getById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/by-date")
    public ResponseEntity<StatJourneeDto> getByDate(
            @RequestParam String date,
            @RequestParam Long utilisateurId) {

        LocalDate parsedDate = LocalDate.parse(date);

        StatJourneeDto dto = statJourneeService.getByUserAndDateOrCreate(utilisateurId, parsedDate);

        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/today")
    public ResponseEntity<StatJourneeDto> getToday() {
        Long userId = currentUserService.getCurrentUserId();
        LocalDate today = LocalDate.now();

        StatJourneeDto dto = statJourneeService.getByUserAndDateOrCreate(userId, today);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public StatJourneeDto create(@RequestBody StatJourneeDto dto) {
        return statJourneeService.create(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatJourneeDto> update(@PathVariable Long id, @RequestBody StatJourneeDto dto) {
        StatJourneeDto updated = statJourneeService.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        statJourneeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
