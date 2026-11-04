package com.example.demo.service;

import com.example.demo.entities.FormaDePagamentoEntity;
import com.example.demo.repository.FormaDePagamentoRepository;
import com.example.demo.mapper.FormaDePagamentoMapper;
import com.example.demo.dto.FormaDePagamentoDTO;
import com.example.demo.exception.FormaDePagamentoNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class FormaDePagamentoService {

    @Autowired
    private FormaDePagamentoRepository formaDePagamentoRepository;

    public List<FormaDePagamentoDTO> listarTodos() {
        return formaDePagamentoRepository.findAll()
                .stream()
                .map(formaDePagamentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FormaDePagamentoDTO buscarPorId(Long id) {
        return formaDePagamentoRepository.findById(id)
                .map(formaDePagamentoMapper::toDTO)
                .orElseThrow(() -> new FormaDePagamentoNaoEncontradaException("Forma de pagamento não encontrada com ID: " + id));
    }

    public FormaDePagamentoDTO salvar(FormaDePagamentoDTO dto) {
        FormaDePagamentoEntity entity = formaDePagamentoMapper.toEntity(dto);
        FormaDePagamentoEntity entitySalva = formaDePagamentoRepository.save(entity);
        return formaDePagamentoMapper.toDTO(entitySalva);
    }

    public void deletar(Long id) {
        formaDePagamentoRepository.deleteById(id);
    }

    @Autowired
    private FormaDePagamentoMapper formaDePagamentoMapper;

}
