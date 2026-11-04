package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "itens_venda")


public class ItemVenda {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantidade;
    private Double precoUnitario;

    public ItemVenda(Long id, Integer quantidade, Double precoUnitario) {
        this.id = id;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    //Esses sãos os GETTERS que serão usados - isso é para visualizar os dados
    public Long getId() {
        return id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }
    // Esses são os SETTERS - esse é para alterar os valores

    public void setId(Long id) {
        this.id = id;
    }

    public ItemVenda() {
    }//Construtor vazio

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    //Essa aqui é onde o valor total é calculado
    //Com a quantidade de produtos multiplicado pelo preço
    public Double calcularTotal() {
        return quantidade * precoUnitario;
    }





}
