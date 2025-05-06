package com.db.crud_naruto.DTO.personagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class ResponsePersonagemDto {
    Long id;
    String nome;
    Integer idade;
    String aldeia;
    Integer vida;
    Integer chakra;
    Map<String, Integer> jutsus;
}
