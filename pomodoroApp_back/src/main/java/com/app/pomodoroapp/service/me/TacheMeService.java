package com.app.pomodoroapp.service.me;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.pomodoroapp.dto.me.taches.TacheMeCreateDto;
import com.app.pomodoroapp.dto.me.taches.TacheMeDto;
import com.app.pomodoroapp.dto.me.taches.TacheMeUpdateDto;
import com.app.pomodoroapp.entity.Tache;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.mapper.me.TacheMeMapper;
import com.app.pomodoroapp.repository.TacheRepository;
import com.app.pomodoroapp.repository.UtilisateurRepository;

import jakarta.transaction.Transactional;

@Service
public class TacheMeService {

    private final TacheRepository tacheRepository;
    private final TacheMeMapper mapper;
    private final UtilisateurRepository utilisateurRepository;

    public TacheMeService(TacheRepository tacheRepository,
            TacheMeMapper mapper,
            UtilisateurRepository utilisateurRepository) {
        this.tacheRepository = tacheRepository;
        this.mapper = mapper;
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<TacheMeDto> getAll(Long userId) {
        return tacheRepository.findByUtilisateurIdUserOrderByPositionAsc(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public TacheMeDto getOne(Long userId, Long tacheId) {
        return tacheRepository.findByIdTacheAndUtilisateurIdUser(tacheId, userId)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Transactional
    public TacheMeDto create(Long userId, TacheMeCreateDto dto) {

        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Tache tache = new Tache();
        tache.setTitre(dto.getTitre());
        tache.setDescription(dto.getDescription());
        tache.setValidation(false);
        tache.setUtilisateur(user);

        int maxPos = tacheRepository.findMaxPositionByUtilisateur(userId);
        tache.setPosition(maxPos + 1);

        return mapper.toDto(tacheRepository.save(tache));
    }

    @Transactional
    public TacheMeDto updatePartial(Long userId, Long id, TacheMeUpdateDto dto) {

        Tache tache = tacheRepository.findByIdTacheAndUtilisateurIdUser(id, userId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

        if (dto.getTitre() != null)
            tache.setTitre(dto.getTitre());
        if (dto.getDescription() != null)
            tache.setDescription(dto.getDescription());
        if (dto.getValidation() != null)
            tache.setValidation(dto.getValidation());

        return mapper.toDto(tacheRepository.save(tache));
    }

    @Transactional
    public void delete(Long userId, Long id) {
        Tache tache = tacheRepository.findByIdTacheAndUtilisateurIdUser(id, userId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));
        tacheRepository.delete(tache);
    }

    @Transactional
    public TacheMeDto reorder(Long userId, Long id, int newPosition) {

        Tache tache = tacheRepository.findByIdTacheAndUtilisateurIdUser(id, userId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

        List<Tache> taches = tacheRepository.findByUtilisateurIdUserOrderByPositionAsc(userId);

        newPosition = Math.max(1, Math.min(newPosition, taches.size()));
        int oldPosition = tache.getPosition();

        if (newPosition == oldPosition)
            return mapper.toDto(tache);

        for (Tache t : taches) {
            if (oldPosition < newPosition && t.getPosition() > oldPosition && t.getPosition() <= newPosition)
                t.setPosition(t.getPosition() - 1);

            else if (oldPosition > newPosition && t.getPosition() < oldPosition && t.getPosition() >= newPosition)
                t.setPosition(t.getPosition() + 1);
        }

        tache.setPosition(newPosition);
        tacheRepository.saveAll(taches);

        return mapper.toDto(tache);
    }
}
