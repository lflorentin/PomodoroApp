package com.app.pomodoroapp.mapper.admin;

import com.app.pomodoroapp.dto.admin.stat.StatJourneeDto;
import com.app.pomodoroapp.entity.StatJournee;
import com.app.pomodoroapp.entity.Utilisateur;
import com.app.pomodoroapp.repository.UtilisateurRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class StatJourneeMapper {

    @Autowired
    protected UtilisateurRepository utilisateurRepository;

    @Autowired
    protected UtilisateurMapper utilisateurMapper;

    @Mapping(target = "utilisateur", expression = "java(getUtilisateurEntity(dto.getIdUser()))")
    public abstract StatJournee toEntity(StatJourneeDto dto);

    @Mapping(target = "idUser", source = "utilisateur.idUser")
    public abstract StatJourneeDto toDto(StatJournee entity);

    protected Utilisateur getUtilisateurEntity(Long idUtilisateur) {
        if (idUtilisateur == null) {
            return null;
        }
        return utilisateurRepository.findById(idUtilisateur).orElse(null);
    }
}
