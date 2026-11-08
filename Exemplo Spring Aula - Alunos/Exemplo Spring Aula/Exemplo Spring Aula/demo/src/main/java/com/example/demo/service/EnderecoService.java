package com.example.demo.service;

import com.example.demo.Entities.Endereco;
import com.example.demo.dto.EnderecoDTO;
import com.example.demo.mapper.EnderecoMapper;
import com.example.demo.repository.IEnderecoRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
    Regras de negocio do Crud de Enderecos:
    Regra 01: CEP normalizado para 8 digitos e estado deve ser uma UF brasileira valida (2 digitos)
    Regra 02: Nao permite endereco duplicado: mesmo CEP + numero + complemento.
*/

@Service 
public class EnderecoService {
    private static final Set<String> UFS = Set.of(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA",
            "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");

    @Autowired
    private IEnderecoRepository repository;

    @Autowired
    private EnderecoMapper mapper;

    /* Usado por Clientes e fornecedores (se quiserem) para ligar a FK. Lanca noSuchElementException se nao encontrar */
    @Transactional(readOnly = true)
    public Endereco buscarEntidade(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Endereco nao encontrado com id " + id));
    }

    /* Regra Individual 1 - Normalizacoes */
    private void normalizar(Endereco e) {
        String cep = e.getCep() == null ? "" : e.getCep().replaceAll("\\D", "");
        if (cep.length() != 8) {
            throw new IllegalArgumentException("CEP invalido: deve conter 8 digitos");
        }
        e.setCep(cep);

        String uf = e.getEstado() == null ? "" : e.getEstado().trim().toUpperCase();
        if (!UFS.contains(uf)) {
            throw new IllegalArgumentException("Estado invalido: '" + e.getEstado() + "' nao e uma UF valida");
        }
        e.setEstado(uf);

        e.setLogradouro(e.getLogradouro().trim());
        e.setNumero(e.getNumero().trim());
        e.setBairro(e.getBairro().trim());
        e.setCidade(e.getCidade().trim());
        String comp = e.getComplemento() == null ? null : e.getComplemento().trim();
        e.setComplemento(comp == null || comp.isEmpty() ? null : comp);
    }

    /* Regra Individual 2 - Validar duplicidades de enderecos cadastrados */
    private void validarDuplicidade(Endereco e, Long idAtual) {
        boolean duplicado = (idAtual == null)
        ? repository.existsByCepAndNumeroAndComplemento(e.getCep(), e.getNumero(), e.getComplemento())
        : repository.existsByCepAndNumeroAndComplementoAndIdNot(e.getCep(), e.getNumero(), e.getComplemento(), idAtual);
        if (duplicado) {
            throw new IllegalStateException("Ja existe um endereco cadastrado com este CEP, numero e complemento");
        }
    }

    @Transactional(readOnly = true)
    public List<EnderecoDTO> listarTodos() {
        return mapper.toDTOList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public EnderecoDTO buscarPorId(Long id) {
        return mapper.toDTO(buscarEntidade(id));
    }

    @Transactional
    public EnderecoDTO criar(EnderecoDTO dto) {
        Endereco endereco = mapper.toEntity(dto);
        endereco.setId(null);
        normalizar(endereco);
        validarDuplicidade(endereco, null);
        return mapper.toDTO(repository.save(endereco));
    }

    @Transactional
    public EnderecoDTO atualizar(Long id, EnderecoDTO dto) {
        Endereco existente = buscarEntidade(id);
        mapper.updateEntity(dto, existente);
        normalizar(existente);
        validarDuplicidade(existente, id);
        return mapper.toDTO(repository.save(existente));
    }

    @Transactional
    public void excluir(Long id) {
        Endereco endereco = buscarEntidade(id);
        if (repository.existsFornecedorByEnderecoId(id)) {
            throw new IllegalStateException("Endereco esta vinculado a um fornecedor e nao pode ser excluido");
        }
        repository.delete(endereco);
    }
}