package com.example.demo.controller;

import com.example.demo.dto.FornecedorRequest;
import com.example.demo.dto.FornecedorResponse;
import com.example.demo.service.FornecedorService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fornecedores")
@Tag(name = "Fornecedores", description = "CRUD de fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService service;

    @GetMapping
    @Operation(summary = "Lista todos os fornecedores")
    public ResponseEntity<List<FornecedorResponse>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um fornecedor por ID")
    public ResponseEntity<FornecedorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo fornecedor")
    public ResponseEntity<ApiResponse<FornecedorResponse>> criar(@Valid @RequestBody FornecedorRequest request) {
        FornecedorResponse criado = service.criar(request);
        return ResponseEntity.created(URI.create("/api/fornecedores/" + criado.id()))
                .body(new ApiResponse<>(criado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um fornecedor")
    public ResponseEntity<ApiResponse<FornecedorResponse>> atualizar(
            @PathVariable Long id, @Valid @RequestBody FornecedorRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(service.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um fornecedor sem produtos vinculados")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativa um fornecedor")
    public ResponseEntity<ApiResponse<FornecedorResponse>> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(service.inativar(id)));
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativa um fornecedor")
    public ResponseEntity<ApiResponse<FornecedorResponse>> ativar(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(service.ativar(id)));
    }
}
