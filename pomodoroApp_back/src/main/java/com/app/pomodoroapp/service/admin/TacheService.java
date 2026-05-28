package com.app.pomodoroapp.service.admin;

import com.app.pomodoroapp.dto.admin.taches.TacheCreateDto;
import com.app.pomodoroapp.dto.admin.taches.TacheDto;
import com.app.pomodoroapp.dto.admin.taches.TacheUpdateDto;
import com.app.pomodoroapp.entity.Tache;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.mapper.admin.TacheMapper;
import com.app.pomodoroapp.repository.TacheRepository;
import com.app.pomodoroapp.repository.UtilisateurRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TacheService {

    private final TacheRepository tacheRepository;
    private final TacheMapper tacheMapper;
    private final UtilisateurRepository utilisateurRepository;

    public TacheService(TacheRepository tacheRepository, TacheMapper tacheMapper,
            UtilisateurRepository utilisateurRepository) {
        this.tacheRepository = tacheRepository;
        this.tacheMapper = tacheMapper;
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<TacheDto> getAllTaches() {
        return tacheRepository.findAll().stream()
                .map(tacheMapper::toDto)
                .collect(Collectors.toList());
    }

    public TacheDto getTacheById(Long id) {
        return tacheRepository.findById(id).map(tacheMapper::toDto).orElse(null);
    }

    public List<TacheDto> getTachesByUtilisateur(Long utilisateurId) {
        return tacheRepository.findByUtilisateurIdUser(utilisateurId)
                .stream()
                .map(tacheMapper::toDto)
                .collect(Collectors.toList());
    }

    public TacheDto createTache(TacheCreateDto dto) {

        Utilisateur user = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Tache tache = tacheMapper.toEntity(dto);
        tache.setUtilisateur(user);

        int maxPos = tacheRepository.findMaxPositionByUtilisateur(user.getIdUser());
        tache.setPosition(maxPos + 1);

        return tacheMapper.toDto(tacheRepository.save(tache));
    }

    @Transactional
    public TacheDto updateFull(Long id, TacheDto dto) {
        return tacheRepository.findById(id)
                .map(existing -> {
                    existing.setTitre(dto.getTitre());
                    existing.setDescription(dto.getDescription());
                    existing.setValidation(dto.getValidation());
                    existing.setPosition(dto.getPosition());
                    return tacheMapper.toDto(tacheRepository.save(existing));
                })
                .orElse(null);
    }

    @Transactional
    public TacheDto updatePartial(Long id, TacheUpdateDto dto) {
        return tacheRepository.findById(id)
                .map(existing -> {

                    if (dto.getTitre() != null)
                        existing.setTitre(dto.getTitre());

                    if (dto.getDescription() != null)
                        existing.setDescription(dto.getDescription());

                    if (dto.getValidation() != null)
                        existing.setValidation(dto.getValidation());

                    if (dto.getPosition() != null)
                        existing.setPosition(dto.getPosition());

                    return tacheMapper.toDto(tacheRepository.save(existing));
                })
                .orElse(null);
    }

    public void deleteTache(Long id) {
        tacheRepository.deleteById(id);
    }

    @Transactional
    public TacheDto reorder(Long idTache, int newPosition) {

        Tache tache = tacheRepository.findById(idTache)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

        Long userId = tache.getUtilisateur().getIdUser();

        List<Tache> taches = tacheRepository.findByUtilisateurIdUserOrderByPositionAsc(userId);

        newPosition = Math.max(1, Math.min(newPosition, taches.size()));

        int oldPosition = tache.getPosition();

        if (newPosition == oldPosition) {
            return tacheMapper.toDto(tache);
        }

        for (Tache t : taches) {
            if (oldPosition < newPosition) {
                if (t.getPosition() > oldPosition && t.getPosition() <= newPosition) {
                    t.setPosition(t.getPosition() - 1);
                }
            } else {
                if (t.getPosition() < oldPosition && t.getPosition() >= newPosition) {
                    t.setPosition(t.getPosition() + 1);
                }
            }
        }

        tache.setPosition(newPosition);
        tacheRepository.saveAll(taches);

        return tacheMapper.toDto(tache);
    }

}
