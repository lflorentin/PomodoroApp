package com.app.pomodoroapp.mapper.me;

import org.mapstruct.Mapper;
import com.app.pomodoroapp.dto.me.utilisateur.UtilisateurMeDto;
import com.app.pomodoroapp.entity.Utilisateur;

@Mapper(componentModel = "spring")
public interface UtilisateurMeMapper {

    UtilisateurMeDto toDto(Utilisateur entity);

}
