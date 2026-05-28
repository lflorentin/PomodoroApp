package com.app.pomodoroapp.repository;

import com.app.pomodoroapp.entity.SessionPomodoro;
import com.app.pomodoroapp.entity.SessionPomodoro.SessionStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SessionPomodoroRepository extends JpaRepository<SessionPomodoro, Long> {

    // Toutes les sessions d’un utilisateur pour une date
    @Query("""
                SELECT s FROM SessionPomodoro s
                WHERE s.utilisateur.idUser = :userId
                AND CAST(s.heureDebut AS date) = :date
            """)
    List<SessionPomodoro> findSessionsOfDay(Long userId, LocalDate date);

    // Nombre de sessions du jour
    @Query("""
                SELECT COUNT(s) FROM SessionPomodoro s
                WHERE s.utilisateur.idUser = :userId
                AND CAST(s.heureDebut AS date) = :date
            """)
    int countSessionsOfDay(Long userId, LocalDate date);

    // Somme des durées
    @Query("""
                SELECT COALESCE(SUM(s.duree), 0) FROM SessionPomodoro s
                WHERE s.utilisateur.idUser = :userId
                AND CAST(s.heureDebut AS date) = :date
            """)
    int getTotalTime(Long userId, LocalDate date);

    // Objectif = taches ayant une session travail + status completee
    @Query("""
                SELECT COUNT(DISTINCT s.tache.idTache)
                FROM SessionPomodoro s
                WHERE s.utilisateur.idUser = :userId
                AND CAST(s.heureDebut AS date) = :date
                AND s.type = com.app.pomodoroapp.entity.SessionPomodoro.SessionType.travail
                AND s.status = com.app.pomodoroapp.entity.SessionPomodoro.SessionStatus.completee
            """)
    int countCompletedTasksOfDay(Long userId, LocalDate date);

	// par utilisateur pour un intervalle de dates
    List<SessionPomodoro> findByUtilisateurIdUserAndHeureDebutBetween(
            Long utilisateurId,
            OffsetDateTime start,
            OffsetDateTime end);
    @Query("""
                SELECT s FROM SessionPomodoro s
                WHERE s.utilisateur.idUser = :userId
                AND s.heureDebut BETWEEN :start AND :end
            """)
    List<SessionPomodoro> findTodaySessionsByUser(Long userId, OffsetDateTime start, OffsetDateTime end);
    
	// id + status
	@Query("""
                SELECT s FROM SessionPomodoro s
                WHERE s.utilisateur.idUser = :userId
                  AND s.status = :status
            """)
    SessionPomodoro findCurrentByUser(Long userId, SessionPomodoro.SessionStatus status);

    List<SessionPomodoro> findByUtilisateurIdUser(Long userId);

    boolean existsByTache_IdTacheAndStatus(Long idTache, SessionStatus status);

	// id + date
    @Query("""
            SELECT s FROM SessionPomodoro s
            WHERE s.utilisateur.idUser = :userId
            AND DATE(s.heureDebut) = :date
            """)
    List<SessionPomodoro> findByUtilisateurIdUserAndDate(
            @Param("userId") Long userId,
            @Param("date") LocalDate date);

}