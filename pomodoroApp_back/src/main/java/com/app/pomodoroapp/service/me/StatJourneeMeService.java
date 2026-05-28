package com.app.pomodoroapp.service.me;

import com.app.pomodoroapp.dto.me.stat.StatJourneeMeDto;
import com.app.pomodoroapp.entity.StatJournee;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.mapper.me.StatJourneeMeMapper;
import com.app.pomodoroapp.repository.StatJourneeRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatJourneeMeService {

    private final StatJourneeRepository repository;
    private final StatJourneeMeMapper mapper;

    public StatJourneeMeService(
            StatJourneeRepository repository,
            StatJourneeMeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /* -------------------- GET /me -------------------- */
    public List<StatJourneeMeDto> getAllForUser(Long userId) {
        List<StatJournee> stats = repository.findByUtilisateurIdUserOrderByDateAsc(userId);
        return stats.stream().map(mapper::toDto).toList();
    }

    /* -------------------- GET /me/today -------------------- */
    public StatJourneeMeDto getToday(Long userId) {
        LocalDate today = LocalDate.now();

        StatJournee stat = repository
                .findByUtilisateurIdUserAndDate(userId, today)
                .orElse(null);

        return stat != null ? mapper.toDto(stat) : null;
    }

    /* -------------------- GET /me/today (new) -------------------- */

    public StatJournee getOrCreateToday(Utilisateur user) {
        LocalDate today = LocalDate.now();

        return repository.findByUtilisateurIdUserAndDate(user.getIdUser(), today)
                .orElseGet(() -> createEmptyStatJournee(user, today));
    }

    private StatJournee createEmptyStatJournee(Utilisateur user, LocalDate date) {
        StatJournee stat = new StatJournee();
        stat.setUtilisateur(user);
        stat.setDate(date);
        stat.setNbSessions(0);
        stat.setObjectif(0);
        stat.setTempsTotal(0);

        return repository.save(stat);
    }

    /* -------------------- GET /me/by-date -------------------- */
    public StatJourneeMeDto getByDate(Long userId, LocalDate date) {
        StatJournee stat = repository
                .findByUtilisateurIdUserAndDate(userId, date)
                .orElse(null);

        return stat != null ? mapper.toDto(stat) : null;
    }

}
