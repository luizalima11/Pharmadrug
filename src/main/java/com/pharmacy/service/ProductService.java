
package com.pharmacy.service;

// Representa os dados de um produto
import com.pharmacy.model.Product;

// Permite acessar o banco de dados por meio do repositório
import com.pharmacy.repository.ProductRepository;

// Identifica esta classe como um serviço do Spring
import org.springframework.stereotype.Service;

// Permite trabalhar com listas de produtos
import java.util.List;

@Service
public class ProductService {

    // Repositório responsável pelo acesso ao banco de dados
    private final ProductRepository productRepository;

    // Recebe o repositório pelo construtor
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Lista todos os produtos cadastrados
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // Valida e cadastra um novo produto
    public Product register(Product product) {

        // Verifica se o produto foi informado
        if (product == null) {
            throw new IllegalArgumentException(
                    "O produto não foi informado."
            );
        }

        // Verifica se o nome foi preenchido
        if (product.getName() == null
                || product.getName().isBlank()) {
            throw new IllegalArgumentException(
                    "O nome do produto é obrigatório."
            );
        }

        // Verifica se a descrição foi preenchida
        if (product.getDescription() == null
                || product.getDescription().isBlank()) {
            throw new IllegalArgumentException(
                    "A descrição do produto é obrigatória."
            );
        }

        // Verifica se o preço foi informado e não é negativo
        if (product.getPrice() == null
                || product.getPrice().signum() < 0) {
            throw new IllegalArgumentException(
                    "Informe um preço válido."
            );
        }

        // Verifica se a categoria foi preenchida
        if (product.getCategory() == null
                || product.getCategory().isBlank()) {
            throw new IllegalArgumentException(
                    "A categoria é obrigatória."
            );
        }

        // Verifica se o estoque foi informado e não é negativo
        if (product.getStock() == null
                || product.getStock() < 0) {
            throw new IllegalArgumentException(
                    "O estoque não pode ser negativo."
            );
        }

        // Envia o produto validado para ser cadastrado
        return productRepository.save(product);
    }

    // Valida e atualiza um produto existente
    public void update(Product product) {

        // Verifica se o produto foi informado
        if (product == null) {
            throw new IllegalArgumentException(
                    "O produto não foi informado."
            );
        }

        // Verifica se o produto possui um ID
        if (product.getId() == null) {
            throw new IllegalArgumentException(
                    "O produto precisa ter um ID para ser atualizado."
            );
        }

        // Verifica se o nome foi preenchido
        if (product.getName() == null
                || product.getName().isBlank()) {
            throw new IllegalArgumentException(
                    "O nome do produto é obrigatório."
            );
        }

        // Verifica se a descrição foi preenchida
        if (product.getDescription() == null
                || product.getDescription().isBlank()) {
            throw new IllegalArgumentException(
                    "A descrição do produto é obrigatória."
            );
        }

        // Verifica se o preço foi informado e não é negativo
        if (product.getPrice() == null
                || product.getPrice().signum() < 0) {
            throw new IllegalArgumentException(
                    "Informe um preço válido."
            );
        }

        // Verifica se a categoria foi preenchida
        if (product.getCategory() == null
                || product.getCategory().isBlank()) {
            throw new IllegalArgumentException(
                    "A categoria é obrigatória."
            );
        }

        // Verifica se o estoque foi informado e não é negativo
        if (product.getStock() == null
                || product.getStock() < 0) {
            throw new IllegalArgumentException(
                    "O estoque não pode ser negativo."
            );
        }

        // Envia o produto validado para ser atualizado no banco
        productRepository.update(product);
    }
}
