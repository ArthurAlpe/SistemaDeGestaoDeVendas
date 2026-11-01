package com.example.demo.mapper;

import com.example.demo.Entities.Cliente;
import com.example.demo.dto.ClienteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(source = "endereco.id", target = "enderecoId")
    ClienteDTO toDTO(Cliente cliente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endereco", ignore = true)
    Cliente toEntity(ClienteDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endereco", ignore = true)
    void updateEntity(ClienteDTO dto, @MappingTarget Cliente cliente);
}