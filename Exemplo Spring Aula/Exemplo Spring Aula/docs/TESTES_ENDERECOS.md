# Evidências - CRUD de Endereços

## 1. Entidade e tabela
A entidade `Endereco` gera a tabela `enderecos` no PostgreSQL.

![Tabela enderecos no pgAdmin](enderecos/tabela-pgadmin.png)

### 1.2 Repository
O `EnderecoRepository` estende `JpaRepository<Endereco, Long>` e possui duas consultas
para a regra de duplicidade (cadastro e atualização). Ao subir a aplicação, o Spring
reconhece o repository e valida os nomes dos métodos contra a entidade.

![Aplicação subindo com o repository reconhecido](enderecos/repository-startup.png)

### 2. DTO

A classe `EnderecoDTO` define os dados utilizados na transferência de informações de endereços pela API. Possui validações para campos obrigatórios, limites de caracteres e formato do CEP. A classe foi criada e integrada ao projeto, porém suas validações serão testadas durante a implementação do Controller e dos endpoints.

![Aplicação iniciada após a criação do DTO](enderecos/dto-startup.png)

### 3. Mapper

A classe `EnderecoMapper` utiliza o MapStruct para converter dados entre `EnderecoDTO` e `Endereco`, incluindo a conversão de listas e a atualização de uma entidade existente sem alterar seu identificador.

Após a implementação da classe, a aplicação foi iniciada para verificar se o projeto continuava executando normalmente.

![Compilação do projeto com sucesso](enderecos/mapper-evidence.png)

## 4. Regras de negócio (EnderecoService)

O `EnderecoService` concentra as regras de negócio do CRUD de Endereços. Ele usa o
`IEnderecoRepository` para consultar o banco e o `EnderecoMapper` para converter
entre entidade e DTO.

**Regra 01: normalização de CEP e validação de UF**
- Implementada no método `normalizar`.
- O CEP é guardado apenas com os 8 dígitos (`87020-000` vira `87020000`).
- O estado é convertido para maiúsculas e precisa ser uma UF brasileira válida.
- Se violar, o Service lança `IllegalArgumentException`, que resulta em HTTP 400.

**Regra 02: não permitir endereço duplicado**
- Implementada no método `validarDuplicidade`.
- Não aceita dois endereços com o mesmo CEP, número e complemento.
- No cadastro usa `existsByCepAndNumeroAndComplemento`. Na atualização usa
  `existsByCepAndNumeroAndComplementoAndIdNot`, para o registro não ser considerado
  duplicado dele mesmo.
- Se violar, o Service lança `IllegalStateException`, que resulta em HTTP 409.

**Registro não encontrado**
- O método `buscarEntidade` lança `NoSuchElementException` (HTTP 404). Ele é usado
  também pelos CRUDs de Clientes e Fornecedores para ligar a chave estrangeira.

*(As evidências no Postman dessas regras serão adicionadas após o Controller.)*

![Compilação do projeto com sucesso após o service](enderecos/service-evidence.png)

## 5. Testes dos endpoints

### POST /api/enderecos (cadastro)
- **Resultado:** 201 Created. CEP salvo sem hífen e estado em maiúsculas.

![POST cadastro](enderecos/01-post-sucesso.png)

### GET /api/enderecos (listar todos)
- **Resultado:** 200 OK.

![GET todos](enderecos/02-get-todos.png)

### GET /api/enderecos/{id} (buscar por ID)
- **Resultado:** 200 OK.

![GET por ID](enderecos/03-get-por-id.png)

### PUT /api/enderecos/{id} (atualizar)
- **Resultado:** 200 OK. Bairro atualizado para "Zona 8".

![PUT](enderecos/04-put-sucesso.png)

### DELETE /api/enderecos/{id} (excluir)
- **Resultado:** 204 No Content.

![DELETE](enderecos/13-delete-sucesso-204.png)

## 3. Testes de situações inválidas

### Cadastro duplicado
- **Resultado:** 409 Conflict.

![POST duplicado](enderecos/05-post-duplicado-409.png)

### Consultar ID inexistente
- **Resultado:** 404 Not Found.

![GET inexistente](enderecos/06-get-inexistente-404.png)

### Atualizar ID inexistente
- **Resultado:** 404 Not Found.

![PUT inexistente](enderecos/07-put-inexistente-404.png)

### Excluir ID inexistente
- **Resultado:** 404 Not Found.

![DELETE inexistente](enderecos/08-delete-inexistente-404.png)

### Dados inválidos: campos obrigatórios vazios
- **Resultado:** 400 Bad Request, com os campos obrigatórios listados.

![Campos vazios](enderecos/09-post-campos-vazios-400.png)

### Dados inválidos: CEP fora do formato
- **Resultado:** 400 Bad Request.

![CEP inválido](enderecos/10-post-cep-invalido-400.png)

### Corpo da requisição malformado
- **Resultado:** 400 Bad Request.

![JSON malformado](enderecos/12-post-json-malformado-400.png)

### Consulta após exclusão
- **Resultado:** 404 Not Found.

![GET apagado](enderecos/14-get-apagado-404.png)

## 4. Regras de negócio

### Regra 01: CEP normalizado e UF válida
- Normalização do CEP: ver cadastro válido (teste 01).
- UF inexistente devolve 400:

![Estado XX](enderecos/11-post-estado-xx-400.png)

### Regra 02: endereço duplicado
- Cadastro repetido devolve 409 (ver teste 05).
- Atualização mantendo os próprios dados continua devolvendo 200 (ver teste 04).