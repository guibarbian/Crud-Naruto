package com.db.crud_naruto.model;

import com.db.crud_naruto.DTO.personagem.ResponsePersonagemDto;
import com.db.crud_naruto.interfaces.Ninja;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class NinjaDeGenjutsu extends Personagem implements Ninja {

    @Override
    public String usarJutsu(){
        return this.getNome() + " está atacando com Genjutsu";
    }

    @Override
    public String desviar(){
        return this.getNome() + " está desviando usando suas habilidades de Genjutsu";
    }

    @Override
    public ResponsePersonagemDto toDto() {
        return ResponsePersonagemDto.builder()
                .id(this.getId())
                .nome(this.getNome())
                .idade(this.getIdade())
                .aldeia(this.getAldeia())
                .chakra(this.getChakra())
                .jutsus(this.getJutsus()).build();

    }
}
