package com.db.crud_naruto.mapper;

import com.db.crud_naruto.DTO.personagem.ResponsePersonagemDto;
import com.db.crud_naruto.model.NinjaDeGenjutsu;
import com.db.crud_naruto.model.NinjaDeNinjutsu;
import com.db.crud_naruto.model.NinjaDeTaijutsu;
import com.db.crud_naruto.model.Personagem;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring")
public interface PersonagemMapper {

    @SubclassMapping( source = NinjaDeNinjutsu.class, target = ResponsePersonagemDto.class )
    @SubclassMapping( source = NinjaDeTaijutsu.class, target = ResponsePersonagemDto.class )
    @SubclassMapping( source = NinjaDeGenjutsu.class, target = ResponsePersonagemDto.class)
    ResponsePersonagemDto map(Personagem source);

}
