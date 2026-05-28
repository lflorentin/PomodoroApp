package com.app.pomodoroapp.controller.me;

import com.app.pomodoroapp.dto.me.stat.StatJourneeMeDto;
import com.app.pomodoroapp.entity.StatJournee;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.mapper.me.StatJourneeMeMapper;
import com.app.pomodoroapp.security.CurrentUserService;
import com.app.pomodoroapp.service.me.StatJourneeMeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stat-journees/me")
public class StatJourneeMeController {

    private final StatJourneeMeService service;
    private final CurrentUserService currentUserService;
    private final StatJourneeMeMapper statJourneeMapper;

    public StatJourneeMeController(
            StatJourneeMeService service,
            CurrentUserService currentUserService, StatJourneeMeMapper statJourneeMapper) {
        this.service = service;
        this.currentUserService = currentUserService;
        this.statJourneeMapper = statJourneeMapper;
    }

    @GetMapping
    public List<StatJourneeMeDto> getMyStats() {
        Long userId = currentUserService.getCurrentUserId();
        return service.getAllForUser(userId);
    }

    @GetMapping("/today")
    public ResponseEntity<StatJourneeMeDto> getToday() {
        Utilisateur user = currentUserService.getCurrentUser();

        StatJournee stat = service.getOrCreateToday(user);

        return ResponseEntity.ok(statJourneeMapper.toDto(stat));
    }

    @GetMapping("/by-date")
    public ResponseEntity<StatJourneeMeDto> getMyStatByDate(@RequestParam String date) {
        Long userId = currentUserService.getCurrentUserId();
        LocalDate parsed = LocalDate.parse(date);
        StatJourneeMeDto dto = service.getByDate(userId, parsed);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.noContent().build();
    }
}
