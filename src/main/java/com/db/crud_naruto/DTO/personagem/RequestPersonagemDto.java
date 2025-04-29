package com.db.crud_naruto.DTO.personagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RequestPersonagemDto {

    String nome;
    Integer idade;
    String aldeia;
    Integer chakra;
    List<String> jutsus;
    String especialidade;
}
