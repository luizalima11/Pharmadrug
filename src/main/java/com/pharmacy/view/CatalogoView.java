
package com.pharmacy.view;

// Importa a classe de produto
import com.pharmacy.model.Product;

// Importa o serviço de produtos
import com.pharmacy.service.ProductService;

// Importa componentes do Vaadin
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

// Para trabalhar com preços decimais
import java.math.BigDecimal;

// Define o endereço da página
@PageTitle("Catálogo de Produtos")
@Route("catalogo")
public class CatalogoView extends VerticalLayout {

    // Serviço responsável pelos produtos
    private final ProductService productService;

    // Tabela de produtos
    private final Grid<Product> tabelaProdutos =
            new Grid<>(Product.class, false);

    // Construtor da tela
    public CatalogoView(ProductService productService) {
        this.productService = productService;

        // Título da página
        H2 titulo = new H2("Catálogo de Produtos");

        // Botão para cadastrar um produto
        Button cadastrarProduto = new Button(
                "Cadastrar produto",
                evento -> getUI().ifPresent(
                        ui -> ui.navigate("cadastro-produto")
                )
        );

        // Cria as colunas da tabela
        tabelaProdutos.addColumn(Product::getId).setHeader("ID");
        tabelaProdutos.addColumn(Product::getName).setHeader("Nome");
        tabelaProdutos.addColumn(Product::getDescription).setHeader("Descrição");
        tabelaProdutos.addColumn(Product::getPrice).setHeader("Preço");
        tabelaProdutos.addColumn(Product::getCategory).setHeader("Categoria");
        tabelaProdutos.addColumn(Product::getStock).setHeader("Estoque");

        // Cria uma coluna com o botão Editar
        tabelaProdutos.addComponentColumn(produto -> {
            Button editar = new Button("Editar");

            // Abre a janela de edição do produto selecionado
            editar.addClickListener(evento -> abrirEdicao(produto));

            return editar;
        }).setHeader("Ações");

        // Configura a tabela
        tabelaProdutos.setWidthFull();

        // Adiciona os componentes à tela
        add(titulo, cadastrarProduto, tabelaProdutos);

        // Carrega os produtos do banco
        atualizarCatalogo();

        setWidthFull();
    }

    // Atualiza a tabela com os dados do banco
    private void atualizarCatalogo() {
        tabelaProdutos.setItems(productService.findAll());
    }

    // Abre uma janela para editar um produto
    private void abrirEdicao(Product produto) {

        // Cria a janela de edição
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Editar produto");

        // Cria os campos com os dados atuais do produto
        TextField nome = new TextField("Nome");
        nome.setValue(produto.getName());

        TextArea descricao = new TextArea("Descrição");
        descricao.setValue(produto.getDescription());

        NumberField preco = new NumberField("Preço");
        preco.setValue(produto.getPrice().doubleValue());
        preco.setMin(0);
        preco.setStep(0.01);

        TextField categoria = new TextField("Categoria");
        categoria.setValue(produto.getCategory());

        NumberField estoque = new NumberField("Estoque");
        estoque.setValue(produto.getStock().doubleValue());
        estoque.setMin(0);
        estoque.setStep(1);

        // Botão para salvar as alterações
        Button salvar = new Button("Salvar alterações", evento -> {

            // Verifica se todos os campos foram preenchidos
            if (nome.isEmpty()
                    || descricao.isEmpty()
                    || categoria.isEmpty()
                    || preco.getValue() == null
                    || estoque.getValue() == null) {

                Notification.show("Preencha todos os campos!");
                return;
            }

            // Verifica se o preço e o estoque são válidos
            if (preco.getValue() < 0
                    || estoque.getValue() < 0
                    || estoque.getValue() % 1 != 0) {

                Notification.show("Verifique o preço e o estoque.");
                return;
            }

            try {
                // Atualiza os dados do produto selecionado
                produto.setName(nome.getValue());
                produto.setDescription(descricao.getValue());
                produto.setPrice(BigDecimal.valueOf(preco.getValue()));
                produto.setCategory(categoria.getValue());
                produto.setStock(estoque.getValue().intValue());

                // Salva as alterações no banco de dados
                productService.update(produto);

                // Atualiza a tabela
                atualizarCatalogo();

                // Fecha a janela
                dialog.close();

                Notification.show("Produto atualizado com sucesso!");

            } catch (Exception erro) {
                Notification.show("Erro ao atualizar o produto.");
                erro.printStackTrace();
            }
        });

        // Botão para cancelar a edição
        Button cancelar = new Button("Cancelar", evento -> dialog.close());

        // Organiza os botões
        HorizontalLayout botoes = new HorizontalLayout(salvar, cancelar);

        // Organiza os campos da janela
        VerticalLayout formulario = new VerticalLayout(
                nome, descricao, preco, categoria, estoque, botoes
        );

        formulario.setWidth("400px");
        formulario.setMaxWidth("100%");

        dialog.add(formulario);
        dialog.open();
    }
}
