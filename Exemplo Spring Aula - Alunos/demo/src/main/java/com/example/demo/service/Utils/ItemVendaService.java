package com.example.demo.service.Utils;

import com.example.demo.entity.ItemVenda;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemVendaService {

    private List<ItemVenda> itens = new ArrayList<>();

    public List<ItemVenda> listaritens() {
        return itens;
    }

    public ItemVenda cadastrarItem(ItemVenda item) {
        itens.add(item);
        return item;
    }
}

//ISSO AQUI É ONDE O CONTROLEER VAI PEDIR A INFORMAÇÃO, ELA VAI SER ENTREGUE ATRAVES DO SERVICE
