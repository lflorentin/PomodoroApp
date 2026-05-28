package com.app.pomodoroapp.service.me;

import com.app.pomodoroapp.dto.me.params.ParamsMeDto;
import com.app.pomodoroapp.dto.me.params.ParamsMeUpdateDto;
import com.app.pomodoroapp.entity.Params;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.mapper.me.ParamsMeMapper;
import com.app.pomodoroapp.repository.ParamsRepository;
import com.app.pomodoroapp.repository.UtilisateurRepository;

import org.springframework.stereotype.Service;

@Service
public class ParamsMeService {

    private final ParamsRepository paramsRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ParamsMeMapper mapper;

    public ParamsMeService(
            ParamsRepository paramsRepository,
            UtilisateurRepository utilisateurRepository,
            ParamsMeMapper mapper) {
        this.paramsRepository = paramsRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.mapper = mapper;
    }

    /* ---------------- GET ---------------- */

    public ParamsMeDto getForUser(Long userId) {
        Params params = paramsRepository.findByUtilisateurIdUser(userId)
                .orElseGet(() -> {
                    Utilisateur user = utilisateurRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

                    Params p = new Params();
                    p.setUtilisateur(user);
                    p.setTheme("system");
                    p.setSonActif(true);
                    return paramsRepository.save(p);
                });

        return mapper.toDto(params);
    }

    /* ---------------- PUT ---------------- */

    public ParamsMeDto updateForUser(Long userId, ParamsMeUpdateDto dto) {
        Params params = paramsRepository.findByUtilisateurIdUser(userId)
                .orElseThrow(() -> new RuntimeException("Params introuvables"));

        params.setTheme(dto.getTheme());
        params.setSonActif(dto.getSonActif());
        params.setDureeTravail(dto.getDureeTravail());
        params.setDureePauseCourte(dto.getDureePauseCourte());
        params.setDureePauseLongue(dto.getDureePauseLongue());
        params.setObjectifSessions(dto.getObjectifSessions());

        return mapper.toDto(paramsRepository.save(params));
    }

    /* ---------------- PATCH ---------------- */

    public ParamsMeDto patchForUser(Long userId, ParamsMeUpdateDto dto) {
        Params params = paramsRepository.findByUtilisateurIdUser(userId)
                .orElseThrow(() -> new RuntimeException("Params introuvables"));

        if (dto.getTheme() != null)
            params.setTheme(dto.getTheme());

        if (dto.getSonActif() != null)
            params.setSonActif(dto.getSonActif());

        if (dto.getDureeTravail() != null)
            params.setDureeTravail(dto.getDureeTravail());

        if (dto.getDureePauseCourte() != null)
            params.setDureePauseCourte(dto.getDureePauseCourte());

        if (dto.getDureePauseLongue() != null)
            params.setDureePauseLongue(dto.getDureePauseLongue());

        if (dto.getObjectifSessions() != null)
            params.setObjectifSessions(dto.getObjectifSessions());

        return mapper.toDto(paramsRepository.save(params));
    }
}
