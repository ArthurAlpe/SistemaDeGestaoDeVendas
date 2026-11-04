package com.example.demo.dto;

public class FormaDePagamentoDTO {

    private String descricao;
    private boolean ativo;
    private Long id;

    public FormaDePagamentoDTO() {
    }

    public FormaDePagamentoDTO(String descricao, boolean ativo, Long id) {
        this.descricao = descricao;
        this.ativo = ativo;
        this.id = id;
    }



    public String getDescricao() {
        return descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Long getId() {
        return id;
    }



    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void setId(Long id) {
        this.id = id;
    }
}