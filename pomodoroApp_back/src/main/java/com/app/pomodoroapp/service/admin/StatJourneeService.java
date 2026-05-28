package com.app.pomodoroapp.service.admin;

import com.app.pomodoroapp.dto.admin.stat.StatJourneeDto;
import com.app.pomodoroapp.entity.SessionPomodoro;
import com.app.pomodoroapp.entity.SessionPomodoro.SessionType;
import com.app.pomodoroapp.mapper.admin.StatJourneeMapper;
import com.app.pomodoroapp.entity.StatJournee;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.repository.SessionPomodoroRepository;
import com.app.pomodoroapp.repository.StatJourneeRepository;
import com.app.pomodoroapp.repository.UtilisateurRepository;

import com.app.pomodoroapp.entity.SessionPomodoro.SessionStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
public class StatJourneeService {

    private final StatJourneeRepository statJourneeRepository;
    private final StatJourneeMapper statJourneeMapper;
    private final UtilisateurRepository utilisateurRepository;
    private final SessionPomodoroRepository sessionPomodoroRepository;

    public StatJourneeService(StatJourneeRepository statJourneeRepository, StatJourneeMapper statJourneeMapper,
            UtilisateurRepository utilisateurRepository,
            SessionPomodoroRepository sessionPomodoroRepository) {
        this.statJourneeRepository = statJourneeRepository;
        this.statJourneeMapper = statJourneeMapper;
        this.utilisateurRepository = utilisateurRepository;
        this.sessionPomodoroRepository = sessionPomodoroRepository;
    }

    public List<StatJourneeDto> getAll() {
        return statJourneeRepository.findAll()
                .stream()
                .map(statJourneeMapper::toDto)
                .collect(Collectors.toList());
    }

    public StatJourneeDto getById(Long id) {
        return statJourneeRepository.findById(id)
                .map(statJourneeMapper::toDto)
                .orElse(null);
    }

    public StatJourneeDto getByUserAndDateOrCreate(Long userId, LocalDate date) {

        StatJournee stat = statJourneeRepository
                .findByUtilisateurIdUserAndDate(userId, date)
                .orElseGet(() -> {
                    StatJournee s = new StatJournee();
                    s.setDate(date);

                    Utilisateur user = utilisateurRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                            
                    s.setUtilisateur(user);
                    s.setNbSessions(0);
                    s.setTempsTotal(0);
                    s.setObjectif(0);

                    return statJourneeRepository.save(s);
                });

        return statJourneeMapper.toDto(stat);
    }

    public StatJourneeDto create(StatJourneeDto dto) {
        StatJournee entity = statJourneeMapper.toEntity(dto);
        return statJourneeMapper.toDto(statJourneeRepository.save(entity));
    }

    public StatJourneeDto update(Long id, StatJourneeDto dto) {
        return statJourneeRepository.findById(id).map(existing -> {
            existing.setDate(dto.getDate());
            existing.setNbSessions(dto.getNbSessions());
            existing.setTempsTotal(dto.getTempsTotal());
            existing.setObjectif(dto.getObjectif());
            StatJournee saved = statJourneeRepository.save(existing);
            return statJourneeMapper.toDto(saved);
        }).orElse(null);
    }

    public void delete(Long id) {
        statJourneeRepository.deleteById(id);
    }

    @Transactional
    public void recalculateStatJournee(Long userId, LocalDate date) {

        StatJournee stat = statJourneeRepository
                .findByUtilisateurIdUserAndDate(userId, date)
                .orElseGet(() -> {
                    StatJournee s = new StatJournee();
                    s.setDate(date);
                    s.setUtilisateur(utilisateurRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé")));
                    s.setNbSessions(0);
                    s.setTempsTotal(0);
                    s.setObjectif(8); 

                    return statJourneeRepository.save(s);
                });

        OffsetDateTime start = date.atStartOfDay().atOffset(OffsetDateTime.now().getOffset());
        OffsetDateTime end = date.plusDays(1).atStartOfDay().atOffset(OffsetDateTime.now().getOffset());

        List<SessionPomodoro> sessions = sessionPomodoroRepository
                .findByUtilisateurIdUserAndHeureDebutBetween(userId, start, end);

        int completedWorkSessions = (int) sessions.stream()
                .filter(s -> s.getStatus() == SessionStatus.completee &&
                        s.getType() == SessionType.travail)
                .count();

        stat.setNbSessions(completedWorkSessions);

        int totalMinutes = sessions.stream()
                .filter(s -> s.getStatus() == SessionStatus.completee &&
                        s.getType() == SessionType.travail &&
                        s.getDureeReelle() != null)
                .mapToInt(SessionPomodoro::getDureeReelle)
                .sum();

        stat.setTempsTotal(totalMinutes);

        statJourneeRepository.save(stat);
    }

}
