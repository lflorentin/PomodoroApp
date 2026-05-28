package com.app.pomodoroapp.repository;

import com.app.pomodoroapp.entity.StatJournee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StatJourneeRepository extends JpaRepository<StatJournee, Long> {
    Optional<StatJournee> findByUtilisateurIdUserAndDate(Long idUser, LocalDate date);

    List<StatJournee> findByUtilisateurIdUserOrderByDateAsc(Long userId);
}
