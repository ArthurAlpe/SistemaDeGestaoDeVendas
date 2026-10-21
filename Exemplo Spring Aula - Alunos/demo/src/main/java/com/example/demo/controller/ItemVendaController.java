package com.example.demo.controller;

import com.example.demo.entity.ItemVenda;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemVendaController {

    @GetMapping("/itens-venda")
    public ItemVenda buscarItem() {
        return new ItemVenda(1L, 3, 50.0);
    }
}
