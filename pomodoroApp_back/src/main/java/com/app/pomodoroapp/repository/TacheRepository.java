package com.app.pomodoroapp.repository;

import com.app.pomodoroapp.entity.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TacheRepository extends JpaRepository<Tache, Long> {

    List<Tache> findByUtilisateurIdUserOrderByPositionAsc(Long idUser);

    List<Tache> findByUtilisateurIdUser(Long idUser);

    Optional<Tache> findByIdTacheAndUtilisateurIdUser(Long idTache, Long idUser);

    @Query("SELECT COALESCE(MAX(t.position), 0) FROM Tache t WHERE t.utilisateur.idUser = :idUser")
    int findMaxPositionByUtilisateur(@Param("idUser") Long idUser);

    @Query("SELECT COALESCE(MAX(t.position), 0) FROM Tache t")
    Optional<Integer> findMaxPosition();
}
