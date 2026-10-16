package com.example.demo.entity;

import jakarta.persistence.*;                                 //importando todas as classes do pacote jakarta

import lombok.Data;

@Data
@Entity
@Table(name = "categorias")

public class Categoria {

    @Id                                                         //Representa a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)         //configurado para gerar auutomaticamente com a estrategia Identity
    private Long id;                                            //Declarando o atributto como o privado LONG

    @Column(name="nome", nullable = false, unique = true)       //Name(Definir nome da coluna) nullable = false(impede valores nulos)
    private String nome;                                        //unique= true

    @Column(name="descricao", nullable = true)                  //nullable=true(Permite valores nulos, visto que a descrção não precisa de
    private String descricao;                                   //uma descrição)

    @Column(name="situacao", nullable = false)                  //Usando boolean Situação = Ativo(True) || Situação = Inativo(False).
    private boolean situacao;
}
