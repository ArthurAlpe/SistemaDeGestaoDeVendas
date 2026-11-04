
package com.example.demo.service.Utils;

import com.example.demo.entity.ItemVenda;
import com.example.demo.repository.ItemVendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


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


    public ItemVenda buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }


    // POST - Cadastrar item
    public ItemVenda cadastrarItem(ItemVenda item) {
        validarItem(item);
        item.setId(null);
        return repository.save(item);
    }

    // PUT - Atualizar item
    public ItemVenda atualizarItem(Long id, ItemVenda novosDados) {
        validarItem(novosDados);

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


    private void validarItem(ItemVenda item) {
        if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A quantidade deve ser maior que zero"
            );
        }

        if (item.getPrecoUnitario() == null ||
                !Double.isFinite(item.getPrecoUnitario()) ||
                item.getPrecoUnitario() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O preço unitário deve ser válido e não pode ser negativo"
            );
        }
    }

}
