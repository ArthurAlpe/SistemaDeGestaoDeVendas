package com.example.demo.service;


import com.example.demo.entity.Categoria;
import com.example.demo.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;


@Service
public class CategoriaService {


    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarTodas(){
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Long id){
        return categoriaRepository.findById(id);
    }

    private void validarCategoria(Categoria categoria){
        if (categoria == null || categoria.getNome() == null || categoria.getNome().isBlank()){
            throw new IllegalArgumentException("O nome da categoria é obrigadorio");
        }
    }

    public Categoria cadastrar(Categoria novaCategoria){

        validarCategoria(novaCategoria);
        return categoriaRepository.save(novaCategoria);
    }

    public Categoria atualizar(Long id, Categoria dadosAtualizados){

        Optional<Categoria> categoriaExistente = categoriaRepository.findById(id);

        if (categoriaExistente.isPresent()){
            Categoria categoria = categoriaExistente.get();
            validarCategoria(dadosAtualizados);

            categoria.setNome(dadosAtualizados.getNome());
            categoria.setDescricao(dadosAtualizados.getDescricao());
            categoria.setSituacao(dadosAtualizados.isSituacao());

            return categoriaRepository.save(categoria);

        }
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Categoria não encontrado"
            );
    }

    public Categoria deletar(Long id){

        Optional<Categoria> categoriaExistente = categoriaRepository.findById(id);

        if (categoriaExistente.isPresent()){
            Categoria categoria = categoriaExistente.get();
            //validarCategoria(categoria); desnecessario visto que uma linha para ser excluida precisa ter algum nome
            categoriaRepository.delete(categoria);
            return categoria;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Categoria não encontrado"
        );
    }

}

