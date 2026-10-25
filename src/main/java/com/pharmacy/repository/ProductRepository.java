
package com.pharmacy.repository;

import com.pharmacy.model.Product;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {

    // Conexão com o banco de dados via JDBC
    private final Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    // Busca todos os produtos cadastrados no banco
    public List<Product> findAll() {

        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT id, nome, descricao, preco,
                       categoria, estoque, imagem
                FROM produto
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Product product = new Product();

                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("nome"));
                product.setDescription(resultSet.getString("descricao"));
                product.setPrice(resultSet.getBigDecimal("preco"));
                product.setCategory(resultSet.getString("categoria"));
                product.setStock(resultSet.getInt("estoque"));
                product.setImage(resultSet.getString("imagem"));

                products.add(product);
            }

        } catch (Exception erro) {
            throw new RuntimeException("Erro ao listar produtos.", erro);
        }

        return products;
    }

    // Cadastra um novo produto no banco de dados
    public Product save(Product product) {

        String sql = """
                INSERT INTO produto
                    (nome, descricao, preco, categoria, estoque, imagem)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setBigDecimal(3, product.getPrice());
            statement.setString(4, product.getCategory());
            statement.setInt(5, product.getStock());
            statement.setString(6, product.getImage());

            statement.executeUpdate();

            // Recupera o ID gerado pelo banco
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    product.setId(keys.getInt(1));
                }
            }

            return product;

        } catch (Exception erro) {
            throw new RuntimeException("Erro ao cadastrar produto.", erro);
        }
    }

    // Atualiza os dados de um produto existente
    public void update(Product product) {

        String sql = """
                UPDATE produto
                SET nome = ?,
                    descricao = ?,
                    preco = ?,
                    categoria = ?,
                    estoque = ?,
                    imagem = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setBigDecimal(3, product.getPrice());
            statement.setString(4, product.getCategory());
            statement.setInt(5, product.getStock());
            statement.setString(6, product.getImage());
            statement.setInt(7, product.getId());

            // Executa a atualização no banco
            int linhasAlteradas = statement.executeUpdate();

            // Verifica se o produto foi encontrado
            if (linhasAlteradas == 0) {
                throw new RuntimeException("Produto não encontrado.");
            }

        } catch (Exception erro) {
            throw new RuntimeException("Erro ao atualizar produto.", erro);
        }
    }
}
