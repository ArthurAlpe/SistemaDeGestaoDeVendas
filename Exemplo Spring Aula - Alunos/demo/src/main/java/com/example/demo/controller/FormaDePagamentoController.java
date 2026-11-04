package com.example.demo.controller;

import com.example.demo.dto.FormaDePagamentoDTO;
import com.example.demo.service.FormaDePagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaDePagamentoController {
    @Autowired
    private FormaDePagamentoService formaDePagamentoService;

    @GetMapping
    public List<FormaDePagamentoDTO> findAll(){
        return formaDePagamentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public FormaDePagamentoDTO findById(@PathVariable Long id){
      return formaDePagamentoService.buscarPorId(id);
    }

    @PostMapping
    public FormaDePagamentoDTO salvar(@RequestBody FormaDePagamentoDTO formaDePagamentoDTO){
        return formaDePagamentoService.salvar(formaDePagamentoDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        formaDePagamentoService.deletar(id);
    }

}
