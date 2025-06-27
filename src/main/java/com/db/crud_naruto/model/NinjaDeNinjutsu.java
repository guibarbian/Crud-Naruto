package com.db.crud_naruto.model;

import com.db.crud_naruto.DTO.personagem.ResponsePersonagemDto;

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
public class NinjaDeNinjutsu extends Personagem implements Ninja {

    @Override
    public String usarJutsu(){
        return this.getNome() + " está atacando com Ninjutsu";
    }

    @Override
    public String desviar(){
        return this.getNome() + " está desviando usando suas habilidades de Ninjutsu";
    }
}
