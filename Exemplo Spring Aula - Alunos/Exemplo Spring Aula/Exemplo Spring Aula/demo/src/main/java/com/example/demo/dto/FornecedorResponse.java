package com.example.demo.dto;

public record FornecedorResponse(
        Long id,
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String telefone,
        String email,
        Long enderecoId,
        boolean ativo
) {}
