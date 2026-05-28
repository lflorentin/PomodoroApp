package com.app.pomodoroapp.service.me;

import com.app.pomodoroapp.dto.me.session.*;
import com.app.pomodoroapp.entity.*;
import com.app.pomodoroapp.mapper.me.SessionPomodoroMeMapper;
import com.app.pomodoroapp.repository.*;
import com.app.pomodoroapp.service.admin.StatJourneeService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class SessionPomodoroMeService {

    private final SessionPomodoroRepository repository;
    private final TacheRepository tacheRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final StatJourneeRepository statJourneeRepository;
    private final StatJourneeService statJourneeService;
    private final SessionPomodoroMeMapper mapper;

    public SessionPomodoroMeService(
            SessionPomodoroRepository repository,
            TacheRepository tacheRepository,
            UtilisateurRepository utilisateurRepository,
            StatJourneeRepository statJourneeRepository,
            StatJourneeService statJourneeService,
            SessionPomodoroMeMapper mapper) {

        this.repository = repository;
        this.tacheRepository = tacheRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.statJourneeRepository = statJourneeRepository;
        this.statJourneeService = statJourneeService;
        this.mapper = mapper;
    }

    /* -------------------- GET ALL -------------------- */
    public List<SessionPomodoroMeDto> getAllForUser(Long userId) {
        return repository.findByUtilisateurIdUser(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    /* -------------------- GET TODAY -------------------- */
    public List<SessionPomodoroMeDto> getToday(Long userId) {

        LocalDate today = LocalDate.now();
        OffsetDateTime start = today.atStartOfDay().atOffset(OffsetDateTime.now().getOffset());
        OffsetDateTime end = today.atTime(23, 59, 59).atOffset(OffsetDateTime.now().getOffset());

        return repository.findTodaySessionsByUser(userId, start, end)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    /* -------------------- GET CURRENT SESSION -------------------- */
    public SessionPomodoroMeDto getCurrent(Long userId) {
        SessionPomodoro active = repository.findCurrentByUser(
                userId, SessionPomodoro.SessionStatus.en_cours);
        return active != null ? mapper.toDto(active) : null;
    }

    /* -------------------- CREATE -------------------- */
    @Transactional
    public SessionPomodoroMeDto create(Long userId, SessionPomodoroMeCreateDto dto) {

        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        SessionPomodoro session = new SessionPomodoro();
        session.setUtilisateur(user);
        session.setType(dto.getType());
        session.setStatus(dto.getStatus());
        session.setDuree(dto.getDuree());
        session.setDureeReelle(0);
        session.setHeureDebut(dto.getHeureDebut());
        session.setHeureFin(dto.getHeureFin());

        if (dto.getTacheId() != null) {
            Tache tache = tacheRepository.findByIdTacheAndUtilisateurIdUser(dto.getTacheId(), userId)
                    .orElseThrow(() -> new RuntimeException("Tâche invalide"));
            session.setTache(tache);
        }

        LocalDate date = dto.getHeureDebut().toLocalDate();

        StatJournee stat = statJourneeRepository
                .findByUtilisateurIdUserAndDate(userId, date)
                .orElseGet(() -> {
                    StatJournee s = new StatJournee();
                    s.setDate(date);
                    s.setUtilisateur(user);
                    return statJourneeRepository.save(s);
                });

        session.setStatJournee(stat);

        if (dto.getStatus() == SessionPomodoro.SessionStatus.en_cours) {
            SessionPomodoro active = repository.findCurrentByUser(userId, SessionPomodoro.SessionStatus.en_cours);
            if (active != null) {
                throw new RuntimeException("Une session est déjà en cours !");
            }
        }

        SessionPomodoro saved = repository.save(session);

        statJourneeService.recalculateStatJournee(userId, date);

        return mapper.toDto(saved);
    }

    /* -------------------- PATCH UPDATE -------------------- */
    @Transactional
    public SessionPomodoroMeDto patch(Long userId, Long sessionId, SessionPomodoroMeUpdateDto dto) {

        SessionPomodoro session = repository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session introuvable"));

        if (!session.getUtilisateur().getIdUser().equals(userId))
            throw new RuntimeException("Non autorisé");

        LocalDate oldDate = session.getHeureDebut().toLocalDate();

        if (dto.getDuree() != null)
            session.setDuree(dto.getDuree());
        if (dto.getHeureDebut() != null)
            session.setHeureDebut(dto.getHeureDebut());
        if (dto.getHeureFin() != null)
            session.setHeureFin(dto.getHeureFin());
        if (dto.getType() != null)
            session.setType(dto.getType());

        if (dto.getStatus() != null) {
            if (dto.getStatus() == SessionPomodoro.SessionStatus.en_cours) {
                SessionPomodoro active = repository.findCurrentByUser(userId, SessionPomodoro.SessionStatus.en_cours);
                if (active != null && !active.getIdSession().equals(sessionId))
                    throw new RuntimeException("Une autre session est déjà en cours");
            }
            if (dto.getStatus() == SessionPomodoro.SessionStatus.completee) {
                OffsetDateTime end = dto.getHeureFin() != null
                        ? dto.getHeureFin()
                        : OffsetDateTime.now();

                long minutes = Duration.between(
                        session.getHeureDebut(),
                        end).toMinutes();

                session.setDureeReelle((int) minutes);
            }

            session.setStatus(dto.getStatus());
        }

        if (dto.getTacheId() != null) {
            Tache tache = tacheRepository.findByIdTacheAndUtilisateurIdUser(dto.getTacheId(), userId)
                    .orElseThrow(() -> new RuntimeException("Tâche invalide"));
            session.setTache(tache);
        }

        LocalDate newDate = session.getHeureDebut().toLocalDate();

        SessionPomodoro saved = repository.save(session);

        statJourneeService.recalculateStatJournee(userId, oldDate);
        statJourneeService.recalculateStatJournee(userId, newDate);

        return mapper.toDto(saved);
    }

    /* -------------------- DELETE -------------------- */
    @Transactional
    public void delete(Long userId, Long sessionId) {

        SessionPomodoro session = repository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session introuvable"));

        if (!session.getUtilisateur().getIdUser().equals(userId))
            throw new RuntimeException("Non autorisé");

        if (session.getStatus() == SessionPomodoro.SessionStatus.en_cours) {
            throw new RuntimeException("Impossible de supprimer une session en cours");
        }

        LocalDate date = session.getHeureDebut() != null
                ? session.getHeureDebut().toLocalDate()
                : LocalDate.now();

        repository.delete(session);

        statJourneeService.recalculateStatJournee(userId, date);
    }
}
