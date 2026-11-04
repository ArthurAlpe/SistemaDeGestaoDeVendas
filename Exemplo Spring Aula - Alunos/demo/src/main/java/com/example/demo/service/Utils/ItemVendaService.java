
package com.example.demo.service.Utils;

import com.example.demo.entity.ItemVenda;
import com.example.demo.repository.ItemVendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemVendaService {

    private final ItemVendaRepository repository;

    public ItemVendaService(ItemVendaRepository repository) {
        this.repository = repository;
    }

    // GET - Listar todos os itens
    public List<ItemVenda> listaritens() {
        return repository.findAll();
    }

    // POST - Cadastrar item
    public ItemVenda cadastrarItem(ItemVenda item) {
        item.setId(null);
        return repository.save(item);
    }

    // PUT - Atualizar item
    public ItemVenda atualizarItem(Long id, ItemVenda novosDados) {
        return repository.findById(id)
                .map(item -> {
                    item.setQuantidade(novosDados.getQuantidade());
                    item.setPrecoUnitario(novosDados.getPrecoUnitario());
                    return repository.save(item);
                })
                .orElse(null);
    }

    // DELETE - Excluir item
    public boolean excluirItem(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}
