# SRM – Mercado de Pulgas dos Mil Saberes (Back-end)

API em **Spring Boot** para conversão de moedas (Ouro Real ↔ Tibar) com regras por **produto** e registro de **transações**.

## 🔧 Stack
- Java 17+ (recomendado 17 ou 21)
- Spring Boot 3.5.x (Web, Data JPA, Validation)
- PostgreSQL 14+ 
- springdoc-openapi (Swagger UI)

## 📁 Estrutura (pacotes principais)
```
com.srm.wefin
 ├─ api/                    (controladores REST)
 ├─ config/                 (OpenAPI etc.)
 ├─ domain/                 (entidades JPA)
 ├─ dto/                    (requests/responses)
 ├─ repository/             (Spring Data JPA)
 └─ service/
     ├─ strategy/           (estratégias de conversão)
     └─ util/               (utilidades)
```

## ✅ Pré-requisitos
1. **Java**: `java -version` → 17+  
2. **Maven** (ou Maven Wrapper `mvnw`/`mvnw.cmd`)  
3. **PostgreSQL** rodando localmente

## 🗄️ Banco de dados
Use os scripts em [`scripts/`](scripts/) para criar **DB/usuário**, **tabelas** e **dados de teste**.

### Opção A) Terminal (psql)
```bash
# 1) Crie DB e usuário (executar conectado ao db 'postgres' como superuser)
psql -h localhost -U postgres -d postgres -f scripts/01_create_db_and_user.sql

# 2) Crie tabelas + seed (executar conectado ao db 'wefin' como usuário 'wefin')
psql -h localhost -U wefin -d wefin -f scripts/02_schema_and_seed.sql
```

### Opção B) pgAdmin
- Abra o **Query Tool** no banco **postgres** → rode `01_create_db_and_user.sql`.
- Depois abra o **Query Tool** no banco **wefin** → rode `02_schema_and_seed.sql`.

## ⚙️ Configuração (PostgreSQL)
O projeto deve ter `src/main/resources/application.yml` apontando para o Postgres local.  
Exemplo:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/wefin
    username: wefin
    password: wefin
    driver-class-name: org.postgresql.Driver
  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
        jdbc:
          time_zone: UTC

springdoc:
  api-docs.path: /api-docs
  swagger-ui.path: /swagger-ui.html
```

> Para sobrescrever via VM options (IntelliJ):  
> `-Dspring.datasource.url=jdbc:postgresql://localhost:5432/wefin -Dspring.datasource.username=wefin -Dspring.datasource.password=wefin`

## ▶️ Como rodar

### Usando Maven Wrapper (recomendado)
```bash
# Linux/macOS
./mvnw -U spring-boot:run

# Windows (PowerShell/CMD)
mvnw.cmd -U spring-boot:run
```

### Build do artefato
```bash
./mvnw -U clean package
```
> O projeto está com **packaging WAR**. Você pode:
> - rodar via `spring-boot:run` (dev), **ou**
> - fazer **deploy do WAR** em um Tomcat externo.

## 📚 Documentação (Swagger)
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## 🔌 Endpoints principais

### 1) Taxa de câmbio (GET)
```
GET /v1/taxas-cambio?base=OR&destino=TIB&data=2025-08-18
```

### 2) Criar/atualizar taxa (POST)
```http
POST /v1/taxas-cambio
Content-Type: application/json

{
  "moedaBase": "OR",
  "moedaDestino": "TIB",
  "data": "2025-08-19",
  "taxa": 2.40
}
```

### 3) Converter valor por produto (POST)
```http
POST /v1/conversoes
Content-Type: application/json

{
  "produtoId": 1,
  "moedaOrigem": "OR",
  "moedaDestino": "TIB",
  "valor": 10,
  "naData": "2025-08-18",
  "chaveIdempotencia": "teste-123"
}
```

## 🧪 Smoke test (cURL)
```bash
curl "http://localhost:8080/v1/taxas-cambio?base=OR&destino=TIB&data=$(date +%F)"

curl -X POST http://localhost:8080/v1/taxas-cambio   -H "Content-Type: application/json"   -d '{"moedaBase":"OR","moedaDestino":"TIB","data":"2025-08-19","taxa":2.4}'

curl -X POST http://localhost:8080/v1/conversoes   -H "Content-Type: application/json"   -d '{"produtoId":1,"moedaOrigem":"OR","moedaDestino":"TIB","valor":10,"naData":"2025-08-18"}'
```

## 🛠️ Erros comuns
- **“relação não existe”**: tabelas não foram criadas no **mesmo banco** do `application.yml`. Reexecute os scripts no DB correto (e.g., `wefin`).
- **400 no GET**: verifique espaços na URL (`&data=2025-08-18` sem espaços).
- **404 na taxa**: não há taxa para a data informada → crie com o POST ou insira via SQL.

---

Projeto de desafio técnico SRM – uso educacional/demonstração.
