package com.example.demo.service;

import com.example.demo.Entities.Cliente;
import com.example.demo.Entities.Endereco;
import com.example.demo.dto.ClienteDTO;
import com.example.demo.mapper.ClienteMapper;
import com.example.demo.repository.IClienteRepository;
import com.example.demo.repository.IEnderecoRepository;
import com.example.demo.service.Utils.CpfUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClienteService {

    private final IClienteRepository clienteRepository;
    private final IEnderecoRepository enderecoRepository;
    private final ClienteMapper mapper;

    public ClienteService(IClienteRepository clienteRepository,
                          IEnderecoRepository enderecoRepository,
                          ClienteMapper mapper) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public ClienteDTO cadastrar(ClienteDTO dto) {
        validarCpf(dto.getCpf());

        // REGRA 1: CPF não pode ser duplicado
        if (clienteRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalStateException("Já existe um cliente cadastrado com este CPF");
        }

        Cliente cliente = mapper.toEntity(dto);
        cliente.setEndereco(buscarEndereco(dto.getEnderecoId()));
        return mapper.toDTO(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> listar() {
        return clienteRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ClienteDTO buscarPorId(Long id) {
        return mapper.toDTO(buscarEntidade(id));
    }

    @Transactional
    public ClienteDTO atualizar(Long id, ClienteDTO dto) {
        Cliente cliente = buscarEntidade(id);
        validarCpf(dto.getCpf());

        // REGRA 1 também na atualização
        if (clienteRepository.existsByCpfAndIdNot(dto.getCpf(), id)) {
            throw new IllegalStateException("Já existe outro cliente cadastrado com este CPF");
        }

        mapper.updateEntity(dto, cliente);
        cliente.setEndereco(buscarEndereco(dto.getEnderecoId()));
        return mapper.toDTO(clienteRepository.save(cliente));
    }

    public void excluir(Long id) {
        Cliente cliente = buscarEntidade(id);
        try {
            clienteRepository.delete(cliente);
            clienteRepository.flush();
        } catch (DataIntegrityViolationException e) {
            // REGRA 2: cliente com vendas vinculadas não pode ser excluído
            throw new IllegalStateException("Cliente possui vendas vinculadas e não pode ser excluído");
        }
    }

    // ===== Métodos auxiliares =====

    // Também serve para o CRUD de Vendas: "cliente deve existir"
    public Cliente buscarEntidade(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com id " + id));
    }

    private Endereco buscarEndereco(Long enderecoId) {
        if (enderecoId == null) return null;
        return enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new NoSuchElementException("Endereço não encontrado com id " + enderecoId));
    }

    private void validarCpf(String cpf) {
        if (!CpfUtils.isValido(cpf)) {
            throw new IllegalArgumentException("CPF inválido");
        }
    }
}