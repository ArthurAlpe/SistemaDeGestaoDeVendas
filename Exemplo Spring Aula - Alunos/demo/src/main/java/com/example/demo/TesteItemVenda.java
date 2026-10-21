package com.example.demo;

import com.example.demo.entity.ItemVenda;


public class TesteItemVenda {

    public static void main(String[] args) {

        ItemVenda item = new ItemVenda(1L, 3, 50.0);

        System.out.println("Quantidade: " + item.getQuantidade());
        System.out.println("Preço unitário: "+ item.getPrecoUnitario());
        System.out.println("Total: "+ item.calcularTotal());
    }
}
