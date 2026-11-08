package com.example.demo.service;

import com.example.demo.Entities.Endereco;
import com.example.demo.Entities.Fornecedor;
import com.example.demo.dto.FornecedorRequest;
import com.example.demo.dto.FornecedorResponse;
import com.example.demo.repository.IEnderecoRepository;
import com.example.demo.repository.IFornecedorRepository;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FornecedorService {

    @Autowired
    private IFornecedorRepository repository;

    @Autowired
    private IEnderecoRepository enderecoRepository;

    @Transactional(readOnly = true)
    public Fornecedor buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Fornecedor nao encontrado com id " + id));
    }

    @Transactional(readOnly = true)
    public List<FornecedorResponse> listarTodos() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FornecedorResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public FornecedorResponse criar(FornecedorRequest request) {
        String cnpj = normalizarCnpj(request.cnpj());
        validarCnpj(cnpj);
        validarDuplicidade(cnpj, null);
        validarContato(request.telefone(), request.email());

        Fornecedor fornecedor = new Fornecedor();
        preencher(fornecedor, request, cnpj);
        return toResponse(repository.save(fornecedor));
    }

    @Transactional
    public FornecedorResponse atualizar(Long id, FornecedorRequest request) {
        Fornecedor fornecedor = buscarEntidade(id);
        String cnpj = normalizarCnpj(request.cnpj());
        validarCnpj(cnpj);
        validarDuplicidade(cnpj, id);
        validarContato(request.telefone(), request.email());

        preencher(fornecedor, request, cnpj);
        return toResponse(repository.save(fornecedor));
    }

    @Transactional
    public void excluir(Long id) {
        Fornecedor fornecedor = buscarEntidade(id);
        if (repository.existeProdutoVinculado(id)) {
            throw new IllegalStateException("Fornecedor possui produtos vinculados e nao pode ser excluido. Inative o fornecedor em vez de exclui-lo.");
        }
        repository.delete(fornecedor);
    }

    @Transactional
    public FornecedorResponse inativar(Long id) {
        Fornecedor fornecedor = buscarEntidade(id);
        fornecedor.setAtivo(false);
        return toResponse(repository.save(fornecedor));
    }

    @Transactional
    public FornecedorResponse ativar(Long id) {
        Fornecedor fornecedor = buscarEntidade(id);
        fornecedor.setAtivo(true);
        return toResponse(repository.save(fornecedor));
    }

    private void preencher(Fornecedor fornecedor, FornecedorRequest request, String cnpj) {
        fornecedor.setRazaoSocial(request.razaoSocial().trim());
        fornecedor.setNomeFantasia(normalizarTextoOpcional(request.nomeFantasia()));
        fornecedor.setCnpj(cnpj);
        fornecedor.setTelefone(normalizarTextoOpcional(request.telefone()));
        fornecedor.setEmail(normalizarTextoOpcional(request.email()));

        if (request.enderecoId() == null) {
            fornecedor.setEndereco(null);
        } else {
            Endereco endereco = enderecoRepository.findById(request.enderecoId())
                    .orElseThrow(() -> new NoSuchElementException("Endereco nao encontrado com id " + request.enderecoId()));
            fornecedor.setEndereco(endereco);
        }
    }

    private void validarDuplicidade(String cnpj, Long idAtual) {
        boolean existe = idAtual == null
                ? repository.existsByCnpj(cnpj)
                : repository.existsByCnpjAndIdNot(cnpj, idAtual);
        if (existe) {
            throw new IllegalStateException("Ja existe um fornecedor cadastrado com este CNPJ");
        }
    }

    private void validarContato(String telefone, String email) {
        if (vazio(telefone) && vazio(email)) {
            throw new IllegalArgumentException("Informe pelo menos um meio de contato: telefone ou e-mail");
        }
    }

    private String normalizarCnpj(String valor) {
        if (valor == null) return "";
        return valor.replaceAll("[.\\-/\\s]", "").toUpperCase(Locale.ROOT);
    }

    private void validarCnpj(String cnpj) {
        if (cnpj.length() != 14 || !cnpj.matches("[A-Z0-9]{14}")) {
            throw new IllegalArgumentException("CNPJ invalido: informe 14 caracteres alfanumericos");
        }

        // Para CNPJs exclusivamente numericos, tambem validamos os digitos verificadores.
        if (cnpj.matches("\\d{14}") && !validarDigitosCnpj(cnpj)) {
            throw new IllegalArgumentException("CNPJ invalido: digitos verificadores incorretos");
        }
    }

    private boolean validarDigitosCnpj(String cnpj) {
        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int d1 = calcularDigito(cnpj.substring(0, 12), pesos1);
        int d2 = calcularDigito(cnpj.substring(0, 12) + d1, pesos2);
        return d1 == Character.digit(cnpj.charAt(12), 10)
                && d2 == Character.digit(cnpj.charAt(13), 10);
    }

    private int calcularDigito(String valor, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += Character.digit(valor.charAt(i), 10) * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    private String normalizarTextoOpcional(String valor) {
        if (vazio(valor)) return null;
        return valor.trim();
    }

    private boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private FornecedorResponse toResponse(Fornecedor fornecedor) {
        Long enderecoId = fornecedor.getEndereco() == null ? null : fornecedor.getEndereco().getId();
        return new FornecedorResponse(
                fornecedor.getId(), fornecedor.getRazaoSocial(), fornecedor.getNomeFantasia(),
                fornecedor.getCnpj(), fornecedor.getTelefone(), fornecedor.getEmail(),
                enderecoId, fornecedor.isAtivo());
    }
}
