package com.example.apiestoque.controllers;

import com.example.apiestoque.models.Produto;
import com.example.apiestoque.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/selecionar")
    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    @PostMapping("/inserir")
    public ResponseEntity<String> inserirProduto(@RequestBody Produto produto) {
        try {
            if(produto != null) {
                produtoRepository.save(produto);
                return ResponseEntity.ok("Produto inserido com sucesso!");
            } else {
                throw new DataIntegrityViolationException("Valor nulo!");
            }
        } catch (DataIntegrityViolationException dive){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    @Transactional
    public ResponseEntity<String> excluirProduto(@PathVariable Long id) {
        Long deleteContador = produtoRepository.deleteProdutoById(id);
        if (deleteContador > 0) {
            return ResponseEntity.ok("Produto excluido com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado!");
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        Optional<Produto> produtoExistente = produtoRepository.findById(id);
        if (produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();
            produto.setNome(produtoAtualizado.getNome());
            produto.setDescricao(produtoAtualizado.getDescricao());
            produto.setPreco(produtoAtualizado.getPreco());
            produto.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
            produtoRepository.save(produto);
            return ResponseEntity.ok("Produto atualizado com sucesso!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
