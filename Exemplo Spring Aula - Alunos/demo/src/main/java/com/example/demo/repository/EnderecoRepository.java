package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> { 

    //Regra de duplicidade no cadastro (mesmo CEP + numero + complemento)
    boolean existsByCepAndNumeroAndComplemento(String cep, String numero, String complemento);

    //Regra de duplicidade na atualização (ignora o próprio registro)
    boolean existsByCepAndNumeroAndComplementoAndIdNot(String cep, String numero, String complemento, Long id);
}