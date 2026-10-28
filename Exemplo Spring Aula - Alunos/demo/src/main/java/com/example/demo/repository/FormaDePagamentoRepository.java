package com.example.demo.repository;

import com.example.demo.entities.FormaDePagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormaDePagamentoRepository extends JpaRepository<FormaDePagamentoEntity,Long> {
}
