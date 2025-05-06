package com.db.crud_naruto.model;

import com.db.crud_naruto.DTO.personagem.ResponsePersonagemDto;
import com.db.crud_naruto.exceptions.BadRequestException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "personagens")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "especialidade")
public abstract class Personagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "idade", nullable = false)
    private Integer idade;

    @Column(name = "aldeia", nullable = false)
    private String aldeia;

    @Column(name = "vida", nullable = false)
    private Integer vida;

    @Column(name = "chakra", nullable = false)
    private Integer chakra;

    @ElementCollection
    @CollectionTable(name = "personagem_jutsus", joinColumns = @JoinColumn(name = "personagem_id"))
    @MapKeyColumn(name = "jutsu")
    @Column(name = "dano")
    private Map<String, Integer> jutsus;

    public void addJutsu(String nomeJutsu, Integer dano){
        if(!jutsus.containsKey(nomeJutsu)){
            jutsus.put(nomeJutsu, dano);
        } else {
            throw new BadRequestException("Ninja já tem esse jutsu!");
        }
    }

    public abstract ResponsePersonagemDto toDto();
}
