package com.example.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.demo.Entities.Endereco;
import com.example.demo.dto.EnderecoDTO;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {
    EnderecoDTO toDTO(Endereco endereco);

    Endereco toEntity(EnderecoDTO dto);
    List<EnderecoDTO> toDTOList(List<Endereco> enderecos);

    //Usado no PUT: copia os dados do DTO para a entidade existente.
    @Mapping(target = "id", ignore = true)
    void updateEntity(EnderecoDTO dto, @MappingTarget Endereco endereco);
}