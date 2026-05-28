package com.app.pomodoroapp.repository;

import com.app.pomodoroapp.entity.Params;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParamsRepository extends JpaRepository<Params, Long> {
    java.util.Optional<Params> findByUtilisateurIdUser(Long idUser);
}