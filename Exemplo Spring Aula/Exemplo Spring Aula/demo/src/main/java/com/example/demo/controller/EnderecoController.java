package com.example.demo.controller;

import com.example.demo.dto.EnderecoDTO;
import com.example.demo.service.EnderecoService;
import com.example.demo.service.Utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enderecos")
@Tag(name = "Endereços", description = "CRUD de endereços")
public class EnderecoController {
    @Autowired
    private EnderecoService service;

    @GetMapping
    @Operation(summary = "Lista todos os endereços")
    public ResponseEntity<List<EnderecoDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos()); // Retorna 200
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um endereço por ID")
    public ResponseEntity<EnderecoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id)); // Retorna 200 ou 404
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo endereço")
    public ResponseEntity<ApiResponse<EnderecoDTO>> criar(@Valid @RequestBody EnderecoDTO dto) {
        EnderecoDTO criado = service.criar(dto);
        return ResponseEntity.created(URI.create("api/enderecos/" + criado.getId())).body(new ApiResponse<>(criado)); //201, 400, 409
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um endereço")
    public ResponseEntity<ApiResponse<EnderecoDTO>> atualizar(@PathVariable Long id, @Valid @RequestBody EnderecoDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(service.atualizar(id, dto))); // 200, 400, 404, 409
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um endereço")
    public ResponseEntity<Void> excluir (@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build(); // 204, 404
    }
}