package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.example.demo.Entities.Endereco;

@Repository
public interface IEnderecoRepository extends JpaRepository<Endereco, Long> { 

    //Regra de duplicidade no cadastro (mesmo CEP + numero + complemento)
    boolean existsByCepAndNumeroAndComplemento(String cep, String numero, String complemento);

    //Regra de duplicidade na atualizacao (ignora o proprio registro)
    boolean existsByCepAndNumeroAndComplementoAndIdNot(String cep, String numero, String complemento, Long id);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM fornecedores WHERE endereco_id = :enderecoId)", nativeQuery = true)
    boolean existsFornecedorByEnderecoId(Long enderecoId);
}