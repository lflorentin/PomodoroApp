package com.app.pomodoroapp.me.service;

import com.app.pomodoroapp.entity.StatJournee;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.repository.StatJourneeRepository;
import com.app.pomodoroapp.service.me.StatJourneeMeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatJourneeMeServiceTest {

    @Mock
    StatJourneeRepository repo;

    @InjectMocks
    StatJourneeMeService service;

    @Test
    void getOrCreateToday_createsIfMissing() {
        Utilisateur u = new Utilisateur();
        u.setIdUser(1L);

        when(repo.findByUtilisateurIdUserAndDate(1L, LocalDate.now()))
                .thenReturn(Optional.empty());

        when(repo.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        StatJournee stat = service.getOrCreateToday(u);

        assertEquals(0, stat.getNbSessions());
        assertEquals(LocalDate.now(), stat.getDate());
    }

    @Test
    void getOrCreateToday_returnsExisting() {
        Utilisateur u = new Utilisateur();
        u.setIdUser(1L);

        StatJournee existing = new StatJournee();
        existing.setNbSessions(5);

        when(repo.findByUtilisateurIdUserAndDate(1L, LocalDate.now()))
                .thenReturn(Optional.of(existing));

        StatJournee stat = service.getOrCreateToday(u);

        assertEquals(5, stat.getNbSessions());
    }
}
