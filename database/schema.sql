
-- PRODUTOS
CREATE TABLE IF NOT EXISTS produto (
                                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                                       nome TEXT NOT NULL,
                                       descricao TEXT NOT NULL,
                                       preco REAL NOT NULL,
                                       categoria TEXT NOT NULL,
                                       estoque INTEGER NOT NULL,
                                       imagem TEXT
);

-- CLIENTES
CREATE TABLE IF NOT EXISTS cliente (
                                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                                       nome TEXT NOT NULL,
                                       email TEXT NOT NULL UNIQUE,
                                       senha_hash TEXT NOT NULL,
                                       cargo TEXT NOT NULL DEFAULT 'CLIENTE'
);



-- Produtos de teste para o catálogo
INSERT INTO produto (nome, descricao, preco, categoria, estoque, imagem)
SELECT 'Dipirona', 'Medicamento para aliviar dores e febre', 12.50, 'Medicamentos', 30, NULL
    WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome = 'Dipirona');

INSERT INTO produto (nome, descricao, preco, categoria, estoque, imagem)
SELECT 'Vitamina C', 'Suplemento de vitamina C', 19.90, 'Vitaminas', 20, NULL
    WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome = 'Vitamina C');

INSERT INTO produto (nome, descricao, preco, categoria, estoque, imagem)
SELECT 'Protetor Solar', 'Protetor solar FPS 50', 45.90, 'Cuidados com a pele', 15, NULL
    WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome = 'Protetor Solar');

INSERT INTO produto (nome, descricao, preco, categoria, estoque, imagem)
SELECT 'Shampoo', 'Shampoo para uso diário', 16.90, 'Higiene', 25, NULL
    WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome = 'Shampoo');

INSERT INTO produto (nome, descricao, preco, categoria, estoque, imagem)
SELECT 'Álcool em Gel', 'Álcool em gel para higienização das mãos', 8.50, 'Higiene', 40, NULL
    WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome = 'Álcool em Gel');
