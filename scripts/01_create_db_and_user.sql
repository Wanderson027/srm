-- scripts/01_create_db_and_user.sql
-- Execute conectado ao DB 'postgres' como superusuário (ex.: usuário postgres)

-- 1) Criar DATABASE (se já existir, ignore o erro)
CREATE DATABASE wefin;

-- 2) Criar usuário (se já existir, ignore o erro)
CREATE USER wefin WITH ENCRYPTED PASSWORD 'wefin';

-- 3) Permissões e search_path
GRANT ALL PRIVILEGES ON DATABASE wefin TO wefin;
-- Define 'public' como schema padrão ao conectar no DB 'wefin'
-- (execute este ALTER ROLE após o DB existir)
ALTER ROLE wefin IN DATABASE wefin SET search_path TO public;
