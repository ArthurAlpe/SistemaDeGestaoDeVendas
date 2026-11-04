package com.example.demo.mapper;
import org.springframework.stereotype.Component;
import com.example.demo.dto.FormaDePagamentoDTO;
import com.example.demo.entities.FormaDePagamentoEntity;


@Component
public class FormaDePagamentoMapper {
    public FormaDePagamentoDTO toDTO(FormaDePagamentoEntity entity) {

        FormaDePagamentoDTO dto = new FormaDePagamentoDTO();

        dto.setDescricao(entity.getDescricao());

        dto.setAtivo(entity.isAtivo());

        dto.setId(entity.getId());

        return dto;
    }

    public FormaDePagamentoEntity toEntity(FormaDePagamentoDTO dto) {

        FormaDePagamentoEntity entity = new FormaDePagamentoEntity();

        entity.setDescricao(dto.getDescricao());

        entity.setAtivo(dto.isAtivo());

        entity.setId(dto.getId());

        return entity;
    }
}
