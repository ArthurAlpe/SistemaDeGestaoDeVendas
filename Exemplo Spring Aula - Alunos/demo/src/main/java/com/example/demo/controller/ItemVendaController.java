package com.example.demo.controller;

import com.example.demo.entity.ItemVenda;
import com.example.demo.service.Utils.ItemVendaService; // AQUI ELE PUXA OS DADOS LÁ DO ITEM VENDA SERVICE
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ItemVendaController {

    private final ItemVendaService service; // Isso cria um atributo para o Controller utilizar os métodos do Service.

    public ItemVendaController(ItemVendaService service) {
        this.service = service;
    }

    @GetMapping("/itens-venda")
    public List<ItemVenda> listarItens() {
        return service.listaritens();
    }

    //Cria uma rota para receber requisições POST
    @PostMapping("/itens-venda") //
    public ItemVenda cadstrarItem(@RequestBody ItemVenda item) { // Converte os dados JSON recebidos em um objeto Java
        return service.cadastrarItem(item);// Solicita ao Service que cadastre o item
    }
}
// O Controller recebe a requisição e utiliza
// o Service para buscar os dados do item.