package com.app.pomodoroapp.service.admin;

import com.app.pomodoroapp.dto.admin.params.ParamsCreateDto;
import com.app.pomodoroapp.dto.admin.params.ParamsDto;
import com.app.pomodoroapp.dto.admin.params.ParamsUpdateDto;
import com.app.pomodoroapp.entity.Params;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.mapper.admin.ParamsMapper;
import com.app.pomodoroapp.repository.ParamsRepository;
import com.app.pomodoroapp.repository.UtilisateurRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParamsService {

    private final ParamsRepository repository;
    private final ParamsMapper mapper;
    private final UtilisateurRepository utilisateurRepository;

    public ParamsService(ParamsRepository repository, ParamsMapper mapper,
            UtilisateurRepository utilisateurRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<ParamsDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public ParamsDto getById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElse(null);
    }

    public ParamsDto create(ParamsCreateDto dto) {

        Params params = mapper.toEntity(dto);

        Utilisateur user = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        params.setUtilisateur(user);

        return mapper.toDto(repository.save(params));
    }

    @Transactional
    public ParamsDto updateFull(Long id, ParamsDto dto) {
        Params params = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Params non trouvé"));

        params.setSonActif(dto.getSonActif());
        params.setTheme(dto.getTheme());

        return mapper.toDto(repository.save(params));
    }

    @Transactional
    public ParamsDto update(Long id, ParamsUpdateDto dto) {
        Params params = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Params non trouvé"));

        if (dto.getSonActif() != null)
            params.setSonActif(dto.getSonActif());

        if (dto.getTheme() != null)
            params.setTheme(dto.getTheme());

        return mapper.toDto(repository.save(params));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
