package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record FornecedorRequest(
        @NotBlank(message = "A razao social e obrigatoria")
        @Size(max = 150, message = "A razao social deve ter no maximo 150 caracteres")
        String razaoSocial,

        @Size(max = 150, message = "O nome fantasia deve ter no maximo 150 caracteres")
        String nomeFantasia,

        @NotBlank(message = "O CNPJ e obrigatorio")
        @Size(min = 14, max = 18, message = "O CNPJ deve possuir 14 digitos ou estar no formato com mascara")
        String cnpj,

        @Size(max = 20, message = "O telefone deve ter no maximo 20 caracteres")
        String telefone,

        @Email(message = "O e-mail informado e invalido")
        @Size(max = 150, message = "O e-mail deve ter no maximo 150 caracteres")
        String email,

        @Positive(message = "O id do endereco deve ser positivo")
        Long enderecoId
) {}
