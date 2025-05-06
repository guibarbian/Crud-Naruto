package com.db.crud_naruto.DTO.personagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class RequestPersonagemDto {

    String nome;
    Integer idade;
    String aldeia;
    Integer vida;
    Map<String, Integer> jutsus;
    String especialidade;
}
