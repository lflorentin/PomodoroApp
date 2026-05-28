package com.app.pomodoroapp.service.admin;

import com.app.pomodoroapp.dto.admin.session.SessionPomodoroCreateDto;
import com.app.pomodoroapp.dto.admin.session.SessionPomodoroDto;
import com.app.pomodoroapp.dto.admin.session.SessionPomodoroUpdateDto;
import com.app.pomodoroapp.entity.SessionPomodoro;
import com.app.pomodoroapp.entity.SessionPomodoro.SessionStatus;
import com.app.pomodoroapp.mapper.admin.SessionPomodoroMapper;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.entity.Tache;
import com.app.pomodoroapp.entity.StatJournee;
import com.app.pomodoroapp.repository.SessionPomodoroRepository;
import com.app.pomodoroapp.repository.UtilisateurRepository;
import com.app.pomodoroapp.repository.TacheRepository;
import com.app.pomodoroapp.repository.StatJourneeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionPomodoroService {

    private final SessionPomodoroRepository repository;
    private final UtilisateurRepository utilisateurRepository;
    private final TacheRepository tacheRepository;
    private final StatJourneeRepository statJourneeRepository;
    private final StatJourneeService statJourneeService;
    private final SessionPomodoroMapper mapper;

    public SessionPomodoroService(SessionPomodoroRepository repository,
            UtilisateurRepository utilisateurRepository,
            TacheRepository tacheRepository,
            StatJourneeRepository statJourneeRepository,
            StatJourneeService statJourneeService,
            SessionPomodoroMapper mapper) {
        this.repository = repository;
        this.utilisateurRepository = utilisateurRepository;
        this.tacheRepository = tacheRepository;
        this.statJourneeRepository = statJourneeRepository;
        this.statJourneeService = statJourneeService;
        this.mapper = mapper;
    }

    /* -------------------- GET -------------------- */

    public List<SessionPomodoroDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public SessionPomodoroDto getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    public List<SessionPomodoroDto> getAllByUser(Long userId) {
        return repository.findByUtilisateurIdUser(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    /* -------------------- CREATE -------------------- */-
    @Transactional
    public SessionPomodoroDto create(SessionPomodoroCreateDto dto) {

        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        SessionPomodoro session = new SessionPomodoro();
        session.setType(dto.getType());
        session.setDuree(dto.getDuree());
        session.setHeureDebut(dto.getHeureDebut());
        session.setHeureFin(dto.getHeureFin());
        session.setStatus(dto.getStatus());
        session.setUtilisateur(utilisateur);

        if (dto.getTacheId() != null) {
            Tache tache = tacheRepository.findById(dto.getTacheId())
                    .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

            if (!tache.getUtilisateur().getIdUser().equals(utilisateur.getIdUser())) {
                throw new RuntimeException("La tâche n'appartient pas à l'utilisateur");
            }

            session.setTache(tache);
        }

        if (dto.getType() == SessionPomodoro.SessionType.travail && dto.getTacheId() == null) {
            throw new RuntimeException("Une session de travail doit avoir une tâche.");
        }

        LocalDate dateSession = dto.getHeureDebut().toLocalDate();

        StatJournee stat = statJourneeRepository
                .findByUtilisateurIdUserAndDate(utilisateur.getIdUser(), dateSession)
                .orElseGet(() -> {
                    StatJournee s = new StatJournee();
                    s.setDate(dateSession);
                    s.setUtilisateur(utilisateur);
                    return statJourneeRepository.save(s);
                });

        session.setStatJournee(stat);

        if (dto.getStatus() == SessionPomodoro.SessionStatus.en_cours) {
            SessionPomodoro active = repository.findCurrentByUser(utilisateur.getIdUser(), SessionStatus.en_cours);
            if (active != null) {
                throw new RuntimeException("Une session est déjà en cours pour cet utilisateur.");
            }
        }

        SessionPomodoro saved = repository.save(session);

        statJourneeService.recalculateStatJournee(utilisateur.getIdUser(), dateSession);

        return mapper.toDto(saved);
    }

     /* -------------------- UPDATE -------------------- */
    @Transactional
    public SessionPomodoroDto update(Long id, SessionPomodoroCreateDto dto) {
        return repository.findById(id).map(existing -> {

            LocalDate oldDate = existing.getHeureDebut().toLocalDate();
            Long userId = existing.getUtilisateur().getIdUser();

            existing.setType(dto.getType());
            existing.setDuree(dto.getDuree());
            existing.setHeureDebut(dto.getHeureDebut());
            existing.setHeureFin(dto.getHeureFin());
            existing.setStatus(dto.getStatus());

            if (dto.getUtilisateurId() != null) {
                Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                existing.setUtilisateur(utilisateur);
            }

            if (dto.getTacheId() != null) {
                Tache tache = tacheRepository.findById(dto.getTacheId())
                        .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

                if (!tache.getUtilisateur().getIdUser()
                        .equals(existing.getUtilisateur().getIdUser())) {
                    throw new RuntimeException("La tâche n'appartient pas à l'utilisateur");
                }

                existing.setTache(tache);
            }

            LocalDate newDate = (dto.getHeureDebut() != null)
                    ? dto.getHeureDebut().toLocalDate()
                    : existing.getHeureDebut().toLocalDate();

            StatJournee stat = statJourneeRepository
                    .findByUtilisateurIdUserAndDate(userId, newDate)
                    .orElseGet(() -> {
                        StatJournee s = new StatJournee();
                        s.setDate(newDate);
                        s.setUtilisateur(existing.getUtilisateur());
                        return statJourneeRepository.save(s);
                    });

            existing.setStatJournee(stat);

            if (dto.getStatus() == SessionPomodoro.SessionStatus.en_cours) {
                SessionPomodoro active = repository.findCurrentByUser(userId, SessionStatus.en_cours);
                if (active != null && !active.getIdSession().equals(id)) {
                    throw new RuntimeException("Une autre session est déjà en cours.");
                }
            }

            SessionPomodoro saved = repository.save(existing);
            statJourneeService.recalculateStatJournee(userId, oldDate);
            statJourneeService.recalculateStatJournee(userId, newDate);

            return mapper.toDto(saved);

        }).orElse(null);
    }

    /* -------------------- PATCH -------------------- */
    @Transactional
    public SessionPomodoroDto patchUpdate(Long id, SessionPomodoroUpdateDto dto) {

        return repository.findById(id).map(session -> {

            Long userId = session.getUtilisateur().getIdUser();
            LocalDate oldDate = session.getHeureDebut().toLocalDate();

            if (dto.getDuree() != null)
                session.setDuree(dto.getDuree());

            if (dto.getHeureDebut() != null)
                session.setHeureDebut(dto.getHeureDebut());

            if (dto.getHeureFin() != null)
                session.setHeureFin(dto.getHeureFin());

            if (dto.getStatus() != null)
                session.setStatus(dto.getStatus());

            if (dto.getType() != null)
                session.setType(dto.getType());

            if (dto.getTacheId() != null) {
                Tache tache = tacheRepository.findById(dto.getTacheId())
                        .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

                if (!tache.getUtilisateur().getIdUser().equals(userId)) {
                    throw new RuntimeException("La tâche n'appartient pas à l'utilisateur");
                }

                session.setTache(tache);
            }

            LocalDate newDate = session.getHeureDebut().toLocalDate();

            if (dto.getStatus() == SessionPomodoro.SessionStatus.en_cours) {
                SessionPomodoro active = repository.findCurrentByUser(userId, SessionStatus.en_cours);
                if (active != null && !active.getIdSession().equals(id)) {
                    throw new RuntimeException("Une autre session est déjà en cours.");
                }
            }

            SessionPomodoro saved = repository.save(session);

            statJourneeService.recalculateStatJournee(userId, oldDate);
            statJourneeService.recalculateStatJournee(userId, newDate);

            return mapper.toDto(saved);

        }).orElse(null);
    }

    /* -------------------- DELETE -------------------- */
    @Transactional
    public void delete(Long id) {

        SessionPomodoro session = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));

        Long userId = session.getUtilisateur().getIdUser();
        LocalDate date = session.getHeureDebut().toLocalDate();

        repository.delete(session);

        statJourneeService.recalculateStatJournee(userId, date);
    }

}
