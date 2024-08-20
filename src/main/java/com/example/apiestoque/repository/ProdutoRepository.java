package com.example.apiestoque.repository;

import com.example.apiestoque.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  ProdutoRepository extends JpaRepository<Produto, Long> {
    Long deleteProdutoById(Long id);
}
