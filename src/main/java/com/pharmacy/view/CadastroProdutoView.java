
package com.pharmacy.view;

// Importa a classe de produto
import com.pharmacy.model.Product;

// Importa o serviço de produtos
import com.pharmacy.service.ProductService;

// Importa os componentes do Vaadin
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

// Para trabalhar com preços decimais
import java.math.BigDecimal;

// Define o endereço da página
@PageTitle("Cadastro de Produtos")
@Route("cadastro-produto")
public class CadastroProdutoView extends VerticalLayout {

    // Serviço responsável por cadastrar produtos
    private final ProductService productService;

    // Campos do formulário
    private final TextField nome = new TextField("Nome do produto");
    private final TextArea descricao = new TextArea("Descrição");
    private final NumberField preco = new NumberField("Preço");
    private final TextField categoria = new TextField("Categoria");
    private final NumberField estoque = new NumberField("Estoque");

    // Construtor da tela
    public CadastroProdutoView(ProductService productService) {
        this.productService = productService;

        // Título da página
        H2 titulo = new H2("Cadastrar Produto");

        // Configura os campos obrigatórios
        nome.setRequiredIndicatorVisible(true);
        descricao.setRequiredIndicatorVisible(true);
        preco.setRequiredIndicatorVisible(true);
        categoria.setRequiredIndicatorVisible(true);
        estoque.setRequiredIndicatorVisible(true);

        // Define os exemplos e as configurações dos campos
        nome.setPlaceholder("Ex.: Dipirona");
        descricao.setPlaceholder("Descreva o produto");
        preco.setMin(0);
        preco.setStep(0.01);
        estoque.setMin(0);
        estoque.setStep(1);

        // Botão para salvar o produto
        Button salvar = new Button("Salvar produto", evento -> salvarProduto());

        // Botão para abrir o catálogo
        Button verCatalogo = new Button("Ver catálogo", evento -> {
            getUI().ifPresent(ui -> ui.navigate("catalogo"));
        });

        // Organiza os componentes na tela
        add(titulo, nome, descricao, preco, categoria, estoque,
                salvar, verCatalogo);

        // Define a largura da tela
        setWidth("500px");
        setMaxWidth("100%");
    }

    // Método que cadastra o produto
    private void salvarProduto() {

        // Verifica se os campos foram preenchidos
        if (nome.isEmpty()
                || descricao.isEmpty()
                || categoria.isEmpty()
                || preco.getValue() == null
                || estoque.getValue() == null) {

            Notification.show("Preencha todos os campos!");
            return;
        }

        // Verifica se os valores são válidos
        if (preco.getValue() < 0
                || estoque.getValue() < 0
                || estoque.getValue() % 1 != 0) {

            Notification.show(
                    "O preço deve ser positivo e o estoque deve ser um número inteiro."
            );
            return;
        }

        try {
            // Cria um objeto com os dados informados
            Product produto = new Product(
                    nome.getValue(),
                    descricao.getValue(),
                    BigDecimal.valueOf(preco.getValue()),
                    categoria.getValue(),
                    estoque.getValue().intValue(),
                    null
            );

            // Envia o produto para ser salvo no banco de dados
            productService.register(produto);

            // Mostra uma mensagem de sucesso
            Notification.show("Produto cadastrado com sucesso!");

            // Limpa os campos após salvar
            nome.clear();
            descricao.clear();
            preco.clear();
            categoria.clear();
            estoque.clear();

        } catch (Exception erro) {

            // Mostra uma mensagem caso ocorra um erro
            Notification.show("Erro ao cadastrar o produto.");
            erro.printStackTrace();
        }
    }
}
