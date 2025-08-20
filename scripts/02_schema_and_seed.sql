-- scripts/02_schema_and_seed.sql
-- Execute conectado ao DB 'wefin' como usuário 'wefin'

SET search_path TO public;

-- ===== Esquema =====

CREATE TABLE IF NOT EXISTS moeda (
  codigo VARCHAR(10) PRIMARY KEY,
  nome   VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS reino (
  id   BIGSERIAL PRIMARY KEY,
  nome VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS produto (
  id       BIGSERIAL PRIMARY KEY,
  nome     VARCHAR(120) NOT NULL UNIQUE,
  reino_id BIGINT NOT NULL REFERENCES reino(id)
);

CREATE TABLE IF NOT EXISTS taxa_cambio (
  id            BIGSERIAL PRIMARY KEY,
  moeda_base    VARCHAR(10) NOT NULL REFERENCES moeda(codigo),
  moeda_destino VARCHAR(10) NOT NULL REFERENCES moeda(codigo),
  data_taxa     DATE NOT NULL,
  taxa          NUMERIC(18,6) NOT NULL,
  CONSTRAINT uk_taxa_dia UNIQUE (moeda_base, moeda_destino, data_taxa)
);

CREATE TABLE IF NOT EXISTS regra_conversao_produto (
  id BIGSERIAL PRIMARY KEY,
  produto_id     BIGINT NOT NULL REFERENCES produto(id),
  moeda_base     VARCHAR(10) NOT NULL REFERENCES moeda(codigo),
  moeda_destino  VARCHAR(10) NOT NULL REFERENCES moeda(codigo),
  multiplicador  NUMERIC(18,6) NOT NULL DEFAULT 1,
  tipo_formula   VARCHAR(40),
  parametros_formula TEXT,
  CONSTRAINT uk_regra UNIQUE (produto_id, moeda_base, moeda_destino)
);

CREATE TABLE IF NOT EXISTS transacao_conversao (
  id BIGSERIAL PRIMARY KEY,
  criada_em                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  produto_id               BIGINT NOT NULL REFERENCES produto(id),
  moeda_origem             VARCHAR(10) NOT NULL REFERENCES moeda(codigo),
  moeda_destino            VARCHAR(10) NOT NULL REFERENCES moeda(codigo),
  reino_id                 BIGINT NOT NULL REFERENCES reino(id),
  valor_origem             NUMERIC(18,6) NOT NULL,
  taxa_utilizada           NUMERIC(18,6) NOT NULL,
  multiplicador_utilizado  NUMERIC(18,6) NOT NULL,
  valor_destino            NUMERIC(18,6) NOT NULL,
  data_taxa                DATE NOT NULL,
  chave_idempotencia       VARCHAR(80),
  CONSTRAINT uk_idempotencia UNIQUE (chave_idempotencia)
);

CREATE INDEX IF NOT EXISTS idx_tx_criada_em ON transacao_conversao (criada_em);

-- ===== Seed (dados de teste) =====

INSERT INTO moeda(codigo,nome) VALUES
  ('OR','Ouro Real'),
  ('TIB','Tibar')
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO reino(nome) VALUES
  ('Wefin'),
  ('Montanhas Anas')
ON CONFLICT (nome) DO NOTHING;

-- Produtos de Wefin
INSERT INTO produto (nome, reino_id)
SELECT 'peles',  r.id FROM reino r WHERE r.nome='Wefin'
ON CONFLICT (nome) DO NOTHING;

INSERT INTO produto (nome, reino_id)
SELECT 'madeira', r.id FROM reino r WHERE r.nome='Wefin'
ON CONFLICT (nome) DO NOTHING;

INSERT INTO produto (nome, reino_id)
SELECT 'hidromel', r.id FROM reino r WHERE r.nome='Wefin'
ON CONFLICT (nome) DO NOTHING;

-- Taxa de hoje
INSERT INTO taxa_cambio (moeda_base, moeda_destino, data_taxa, taxa)
VALUES ('OR','TIB', CURRENT_DATE, 2.5)
ON CONFLICT (moeda_base, moeda_destino, data_taxa) DO NOTHING;

-- Regras por produto
INSERT INTO regra_conversao_produto (produto_id, moeda_base, moeda_destino, multiplicador, tipo_formula)
SELECT p.id, 'OR', 'TIB', 1.10, 'LINEAR' FROM produto p WHERE p.nome = 'hidromel'
ON CONFLICT (produto_id, moeda_base, moeda_destino) DO NOTHING;

INSERT INTO regra_conversao_produto (produto_id, moeda_base, moeda_destino, multiplicador, tipo_formula)
SELECT p.id, 'OR', 'TIB', 1.00, 'LINEAR' FROM produto p WHERE p.nome IN ('peles','madeira')
ON CONFLICT (produto_id, moeda_base, moeda_destino) DO NOTHING;

-- Conferência
-- SELECT * FROM taxa_cambio ORDER BY data_taxa DESC;
-- SELECT id, nome FROM produto;
