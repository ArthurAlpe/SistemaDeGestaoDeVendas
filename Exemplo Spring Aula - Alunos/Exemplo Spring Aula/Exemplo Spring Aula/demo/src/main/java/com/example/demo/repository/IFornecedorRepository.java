package com.example.demo.repository;

import com.example.demo.Entities.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IFornecedorRepository extends JpaRepository<Fornecedor, Long> {

    boolean existsByCnpj(String cnpj);

    boolean existsByCnpjAndIdNot(String cnpj, Long id);

    boolean existsByEnderecoId(Long enderecoId);

    /**
     * O projeto ainda pode nao possuir a Entity Produto nesta branch.
     * A consulta so e executada quando o DELETE de fornecedor for chamado.
     */
    @Query(value = "SELECT EXISTS (SELECT 1 FROM produtos WHERE fornecedor_id = :fornecedorId)", nativeQuery = true)
    boolean existeProdutoVinculado(@Param("fornecedorId") Long fornecedorId);
}
