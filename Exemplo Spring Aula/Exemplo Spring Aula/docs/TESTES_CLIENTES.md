# Testes do CRUD de Clientes

**Responsável:** Arthur Almeida Pereira
**Base URL:** `http://localhost:8080/api/clientes`
**Ferramenta de teste:** Postman
**Banco de dados:** PostgreSQL (`sistema_gestao_vendas`, tabela `clientes`)

---

## 1. Estrutura do CRUD

| Camada | Arquivo |
|---|---|
| Entity | `Entities/Cliente.java` |
| DTO | `dto/ClienteDTO.java` |
| Mapper | `mapper/ClienteMapper.java` |
| Repository | `repository/IClienteRepository.java` |
| Service | `service/ClienteService.java` |
| Utilitário | `service/Utils/CpfUtils.java` |
| Controller | `controller/ClienteController.java` |
| Tratamento de erros | `controller/ClienteExceptionHandler.java` |

### Tabela `clientes`

| Coluna | Tipo | Regra |
|---|---|---|
| id | bigint | Chave primária |
| nome | varchar(150) | Obrigatório |
| cpf | varchar(11) | Obrigatório e único |
| telefone | varchar(20) | Opcional |
| email | varchar(150) | Opcional, formato válido |
| data_nascimento | date | Opcional, deve ser no passado |
| ativo | boolean | Obrigatório (padrão `true`) |
| endereco_id | bigint | Chave estrangeira para `enderecos` |

![Estrutura da tabela clientes](clientes/TABELA-PGADMIN.png)

---

## 2. Regras de negócio individuais

Implementadas em `ClienteService`.

1. **CPF não pode ser duplicado**, no cadastro e na atualização. Retorna 409.
   (`cadastrar` e `atualizar`, via `existsByCpf` e `existsByCpfAndIdNot`)
2. **CPF deve ser válido**, com os dígitos verificadores corretos. Retorna 400.
   (`validarCpf`, que usa `CpfUtils.isValido`)
3. **Cliente com vendas vinculadas não pode ser excluído.** Retorna 409.
   (`excluir`, que captura `DataIntegrityViolationException`.
   Será demonstrada após a integração com o CRUD de Vendas.)
4. **Endereço informado deve existir.** Retorna 404.
   (`buscarEndereco`)

---

## 3. Endpoints

| Método | Endpoint | Sucesso | Erros |
|---|---|---|---|
| POST | `/api/clientes` | 201 | 400, 404, 409 |
| GET | `/api/clientes` | 200 | - |
| GET | `/api/clientes/{id}` | 200 | 404 |
| PUT | `/api/clientes/{id}` | 200 | 400, 404, 409 |
| DELETE | `/api/clientes/{id}` | 204 | 404, 409 |

![Swagger](clientes/SWAGGER-CLIENTES.png)

---

## 4. Testes das operações

### 01 - POST (criar cliente)
Esperado: **201 Created**
![01](clientes/01-POST-SUCESSO.png)

### 02 - GET (listar todos)
Esperado: **200 OK**
![02](clientes/02-GET-LISTAR-TODOS.png)

### 03 - GET por ID
Esperado: **200 OK**
![03](clientes/03-GET-POR-ID.png)

### 04 - PUT (atualizar)
Esperado: **200 OK**, com o telefone atualizado
![04](clientes/04-PUT-ATUALIZAR.png)

---

## 5. Testes de situações inválidas

### 05 - CPF duplicado
Esperado: **409 Conflict**
![05](clientes/05-POST-CPF-DUPLICADO.png)

### 06 - Consultar ID inexistente
Esperado: **404 Not Found**
![06](clientes/06-GET-INEXISTENTE.png)

### 07 - Atualizar registro inexistente
Esperado: **404 Not Found**
![07](clientes/07-PUT-INEXISTENTE.png)

### 08 - Excluir registro inexistente
Esperado: **404 Not Found**
![08](clientes/08-DELETE-INEXISTENTE.png)

### 09 - Dados inválidos (corpo vazio)
Esperado: **400 Bad Request**
![09](clientes/09-POST-CORPO-VAZIO.png)

### 10 - E-mail inválido
Esperado: **400 Bad Request**
![10](clientes/10-POST-EMAIL-INVALIDO.png)

### 11 - CPF inválido (regra de negócio)
Esperado: **400 Bad Request**
![11](clientes/11-POST-CPF-INVALIDO.png)

### 12 - Endereço inexistente (relacionamento)
Esperado: **404 Not Found**
![12](clientes/12-POST-ENDERECO-INEXISTENTE.png)

### 13 - JSON malformado
Esperado: **400 Bad Request**
![13](clientes/13-POST-JSON-MALFORMADO.png)

---

## 6. Persistência e exclusão

### 14 - Dados gravados no banco
`SELECT * FROM clientes;`
![14](clientes/14-PGADMIN-SELECT.png)

### 15 - DELETE (excluir cliente)
Esperado: **204 No Content**
![15](clientes/15-DELETE-CLIENTE.png)

### 16 - Consulta após a exclusão
Esperado: **404 Not Found**
![16](clientes/16-GET-APOS-DELETE.png)

---

## 7. Resumo

| Resultado | Quantidade |
|---|---|
| Operações CRUD (POST, GET, GET por ID, PUT, DELETE) | 5 |
| Situações inválidas | 9 |
| Verificação de persistência | 1 |
| Verificação pós-exclusão | 1 |
| **Total** | **16** |