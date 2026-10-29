package com.example.demo.service;

import com.example.demo.entities.FormaDePagamentoEntity;
import com.example.demo.repository.FormaDePagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class FormaDePagamentoService  {

    @Autowired
    private FormaDePagamentoRepository formaDePagamentoRepository;
        public List<FormaDePagamentoEntity> listarTodos(){
            return formaDePagamentoRepository.findAll();
        }

         public Optional <FormaDePagamentoEntity> buscarPorId(Long id){
            return formaDePagamentoRepository.findById(id);
    }

        public FormaDePagamentoEntity salvar(FormaDePagamentoEntity formaDePagamentoEntity){
            return formaDePagamentoRepository.save(formaDePagamentoEntity);
    }

        public void deletar(Long id){
            formaDePagamentoRepository.deleteById(id);
        }


}
