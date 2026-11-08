# Evidencias - CRUD de Enderecos

## 1. Entidade e tabela
A entidade `Endereco` gera a tabela `enderecos` no PostgreSQL.

![Tabela enderecos no pgAdmin](enderecos/tabela-pgadmin.png)

### 1.2 Repository
O `EnderecoRepository` estende `JpaRepository<Endereco, Long>` e possui duas consultas
para a regra de duplicidade (cadastro e atualizacao). Ao subir a aplicacao, o Spring
reconhece o repository e valida os nomes dos metodos contra a entidade.

![Aplicacao subindo com o repository reconhecido](enderecos/repository-startup.png)

### 2. DTO

A classe `EnderecoDTO` define os dados utilizados na transferencia de informacoes de enderecos pela API. Possui validacoes para campos obrigatorios, limites de caracteres e formato do CEP. A classe foi criada e integrada ao projeto, porem suas validacoes serao testadas durante a implementacao do Controller e dos endpoints.

![Aplicacao iniciada apos a criacao do DTO](enderecos/dto-startup.png)

### 3. Mapper

A classe `EnderecoMapper` utiliza o MapStruct para converter dados entre `EnderecoDTO` e `Endereco`, incluindo a conversao de listas e a atualizacao de uma entidade existente sem alterar seu identificador.

Apos a implementacao da classe, a aplicacao foi iniciada para verificar se o projeto continuava executando normalmente.

![Compilacao do projeto com sucesso](enderecos/mapper-evidence.png)

## 4. Regras de negocio (EnderecoService)

O `EnderecoService` concentra as regras de negocio do CRUD de Enderecos. Ele usa o
`IEnderecoRepository` para consultar o banco e o `EnderecoMapper` para converter
entre entidade e DTO.

**Regra 01: normalizacao de CEP e validacao de UF**
- Implementada no metodo `normalizar`.
- O CEP e guardado apenas com os 8 digitos (`87020-000` vira `87020000`).
- O estado e convertido para maiusculas e precisa ser uma UF brasileira valida.
- Se violar, o Service lanca `IllegalArgumentException`, que resulta em HTTP 400.

**Regra 02: nao permitir endereco duplicado**
- Implementada no metodo `validarDuplicidade`.
- Nao aceita dois enderecos com o mesmo CEP, numero e complemento.
- No cadastro usa `existsByCepAndNumeroAndComplemento`. Na atualizacao usa
  `existsByCepAndNumeroAndComplementoAndIdNot`, para o registro nao ser considerado
  duplicado dele mesmo.
- Se violar, o Service lanca `IllegalStateException`, que resulta em HTTP 409.

**Registro nao encontrado**
- O metodo `buscarEntidade` lanca `NoSuchElementException` (HTTP 404). Ele e usado
  tambem pelos CRUDs de Clientes e Fornecedores para ligar a chave estrangeira.

*(As evidencias no Postman dessas regras serao adicionadas apos o Controller.)*

![Compilacao do projeto com sucesso apos o service](enderecos/service-evidence.png)

## 5. Testes dos endpoints

### POST /api/enderecos (cadastro)
- **Resultado:** 201 Created. CEP salvo sem hifen e estado em maiusculas.

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

## 3. Testes de situacoes invalidas

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

### Dados invalidos: campos obrigatorios vazios
- **Resultado:** 400 Bad Request, com os campos obrigatorios listados.

![Campos vazios](enderecos/09-post-campos-vazios-400.png)

### Dados invalidos: CEP fora do formato
- **Resultado:** 400 Bad Request.

![CEP invalido](enderecos/10-post-cep-invalido-400.png)

### Corpo da requisicao malformado
- **Resultado:** 400 Bad Request.

![JSON malformado](enderecos/12-post-json-malformado-400.png)

### Consulta apos exclusao
- **Resultado:** 404 Not Found.

![GET apagado](enderecos/14-get-apagado-404.png)

## 4. Regras de negocio

### Regra 01: CEP normalizado e UF valida
- Normalizacao do CEP: ver cadastro valido (teste 01).
- UF inexistente devolve 400:

![Estado XX](enderecos/11-post-estado-xx-400.png)

### Regra 02: endereco duplicado
- Cadastro repetido devolve 409 (ver teste 05).
- Atualizacao mantendo os proprios dados continua devolvendo 200 (ver teste 04).