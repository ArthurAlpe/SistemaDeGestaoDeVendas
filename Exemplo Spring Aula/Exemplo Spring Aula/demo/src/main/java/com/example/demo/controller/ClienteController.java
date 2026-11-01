package com.example.demo.controller;

import com.example.demo.dto.ClienteDTO;
import com.example.demo.service.ClienteService;
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
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "CRUD de clientes")
public class ClienteController {
    @Autowired
    private ClienteService service;

    @GetMapping
    @Operation(summary = "Lista todos os clientes")
    public ResponseEntity<List<ClienteDTO>> listar() {
        return ResponseEntity.ok(service.listar()); // 200
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um cliente por ID")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id)); // 200 ou 404
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo cliente")
    public ResponseEntity<ApiResponse<ClienteDTO>> criar(@Valid @RequestBody ClienteDTO dto) {
        ClienteDTO criado = service.cadastrar(dto);
        return ResponseEntity.created(URI.create("api/clientes/" + criado.getId())).body(new ApiResponse<>(criado)); // 201, 400, 404, 409
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um cliente")
    public ResponseEntity<ApiResponse<ClienteDTO>> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(service.atualizar(id, dto))); // 200, 400, 404, 409
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um cliente")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build(); // 204, 404, 409
    }
}