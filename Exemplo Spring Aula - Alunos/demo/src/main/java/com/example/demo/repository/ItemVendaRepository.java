package com.example.demo.repository;

import com.example.demo.entity.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemVendaRepository
        extends JpaRepository<ItemVenda, Long> {
}
