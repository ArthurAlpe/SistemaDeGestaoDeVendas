package com.example.demo.controller;
import com.example.demo.service.CategoriaService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Categoria;

@RestController
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/categorias") // vai receber o get nessa url
    public List<Categoria> listar() { // vai devolver a lista
     return categoriaService.listarTodas();   // vai retornar a mesma!
    }

    @PostMapping("/categorias")
    public Categoria cadastrar(@RequestBody Categoria categoria) {
        return categoriaService.cadastrar(categoria);
    }

    @GetMapping("/categorias/{id}")
    public Optional<Categoria> buscarPorId(@PathVariable Long id){
        return categoriaService.buscarPorId(id);
    }
    @PutMapping("/categorias/{id}")
    public Categoria atualizar(@PathVariable Long id, @RequestBody Categoria categoria){
        return categoriaService.atualizar(id, categoria);
    }

    @DeleteMapping("/categorias/{id}")
    public Categoria deletar(@PathVariable Long id){
        return categoriaService.deletar(id);
    }

}
