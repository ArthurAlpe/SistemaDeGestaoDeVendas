package com.example.demo.controller;

import com.example.demo.entities.FormaDePagamentoEntity;
import com.example.demo.service.FormaDePagamentoService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaDePagamentoController {
    @Autowired
    private FormaDePagamentoService formaDePagamentoService;

    @GetMapping
    public List<FormaDePagamentoEntity> findAll(){
        return formaDePagamentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional <FormaDePagamentoEntity> findById(@PathVariable Long id){
      return formaDePagamentoService.buscarPorId(id);
    }

    @PostMapping
    public FormaDePagamentoEntity salvar(@RequestBody FormaDePagamentoEntity formaDePagamentoEntity){
        return formaDePagamentoService.salvar(formaDePagamentoEntity);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        formaDePagamentoService.deletar(id);
    }

}
