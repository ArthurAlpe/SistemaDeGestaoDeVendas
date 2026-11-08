## Felipe Bernardi

**CRUD responsavel:** Enderecos (`/api/enderecos`)

### Responsabilidades

- Entity `Endereco` (tabela `enderecos`);
- Repository `IEnderecoRepository`;
- DTO `EnderecoDTO` e Mapper `EnderecoMapper` (MapStruct);
- Service `EnderecoService`;
- Controller `EnderecoController`;
- tratamento de erros do CRUD (`EnderecoExceptionHandler`);
- validacoes;
- regras de negocio;
- relacionamento: o Endereco e referenciado por Cliente e Fornecedor (N:1);
- testes dos endpoints (Postman);
- documentacao dos endpoints (Swagger e este README).

### Finalidade

Guardar os enderecos usados pelos clientes e fornecedores. Um endereco pode ser compartilhado por varios clientes ou fornecedores, e cada cliente ou fornecedor aponta para um unico endereco.

### Tabela `enderecos`

| Campo | Tipo | Regra |
|-------|------|-------|
| id | Long | Chave primaria, gerada pelo banco |
| cep | String (8) | Obrigatorio, guardado so com digitos |
| logradouro | String (150) | Obrigatorio |
| numero | String (20) | Obrigatorio |
| complemento | String (100) | Opcional |
| bairro | String (100) | Obrigatorio |
| cidade | String (100) | Obrigatorio |
| estado | String (2) | Obrigatorio, sigla da UF em maiusculas |

### Relacionamento

`CLIENTES N:1 ENDERECOS` e `FORNECEDORES N:1 ENDERECOS`.

A chave estrangeira `endereco_id` fica nas tabelas `clientes` e `fornecedores` (lado "N"), por isso a entidade `Endereco` nao possui nenhum campo apontando para elas. Os outros CRUDs usam `EnderecoService.buscarEntidade(id)` para obter o endereco e ligar a chave estrangeira.

### Arquitetura

```
EnderecoController  ->  EnderecoService  ->  IEnderecoRepository  ->  PostgreSQL
 (HTTP)                 (regras)              (consultas)               (tabela enderecos)
```

| Camada | Arquivo | Funcao |
|--------|---------|--------|
| Entity | `Entities/Endereco.java` | Representa a tabela `enderecos` |
| DTO | `dto/EnderecoDTO.java` | Dados de entrada e saida da API, com as validacoes |
| Mapper | `mapper/EnderecoMapper.java` | Converte entre Entity e DTO |
| Repository | `repository/IEnderecoRepository.java` | Acesso ao banco (herda o CRUD do `JpaRepository`) |
| Service | `service/EnderecoService.java` | Regras de negocio |
| Controller | `controller/EnderecoController.java` | Endpoints REST |
| Tratamento de erros | `controller/EnderecoExceptionHandler.java` | Converte os erros do Service em respostas HTTP |

### Endpoints

| Metodo | Rota | Descricao | Sucesso | Erros possiveis |
|--------|------|-----------|---------|-----------------|
| POST | `/api/enderecos` | Cadastra um endereco | 201 | 400, 409 |
| GET | `/api/enderecos` | Lista todos | 200 | - |
| GET | `/api/enderecos/{id}` | Busca por ID | 200 | 404 |
| PUT | `/api/enderecos/{id}` | Atualiza um endereco | 200 | 400, 404, 409 |
| DELETE | `/api/enderecos/{id}` | Exclui um endereco | 204 | 404 |

**Parametro de rota:** `{id}` e o identificador numerico do endereco.

**Corpo da requisicao (POST e PUT):**

```json
{
  "cep": "87020-000",
  "logradouro": "Avenida Colombo",
  "numero": "5790",
  "complemento": "Bloco 1",
  "bairro": "Zona 7",
  "cidade": "Maringa",
  "estado": "pr"
}
```

**Resposta de sucesso:** o endereco salvo, com o CEP sem hifen e o estado em maiusculas (`"cep": "87020000"`, `"estado": "PR"`). Os endpoints de cadastro e atualizacao devolvem o endereco dentro do envelope `ApiResponse`, e os GET devolvem o endereco diretamente.

**Documentacao interativa:** com a aplicacao rodando, acesse `http://localhost:8080/swagger-ui.html`, secao "Enderecos".

### Validacoes (`EnderecoDTO`)

- CEP obrigatorio, no formato `00000-000` ou `00000000`;
- logradouro, numero, bairro e cidade obrigatorios;
- estado obrigatorio, com exatamente 2 letras;
- tamanho maximo em todos os textos (logradouro 150, numero 20, complemento 100, bairro 100, cidade 100).

Quando alguma validacao falha, a API devolve **400** com os campos que tem problema.

### Regras de negocio (`EnderecoService`)

**Regra 01: normalizacao do CEP e validacao da UF**
Implementada no metodo `normalizar`. O CEP e guardado apenas com os 8 digitos (`87020-000` vira `87020000`), e o estado e convertido para maiusculas e precisa ser uma UF brasileira valida. Caso contrario, a API devolve **400**.

**Regra 02: nao permitir endereco duplicado**
Implementada no metodo `validarDuplicidade`. Nao e permitido ter dois enderecos com o mesmo CEP, numero e complemento. No cadastro usa `existsByCepAndNumeroAndComplemento` e, na atualizacao, `existsByCepAndNumeroAndComplementoAndIdNot`, para que o registro nao seja considerado duplicado dele mesmo. Em caso de duplicidade, a API devolve **409**.

### Tratamento de erros

| Situacao | Status |
|----------|--------|
| Cadastro ou atualizacao bem-sucedidos | 200 / 201 |
| Exclusao bem-sucedida | 204 |
| Dados invalidos, CEP ou UF invalidos, JSON malformado | 400 |
| Endereco nao encontrado | 404 |
| Endereco duplicado | 409 |
| Erro interno | 500 |

### Testes

Os testes foram feitos no Postman, cobrindo as 5 operacoes do CRUD e as situacoes invalidas (duplicado, ID inexistente, atualizar e excluir inexistente, dados invalidos e regras violadas). As evidencias com os prints estao em [`docs/TESTES_ENDERECOS.md`](TESTES_ENDERECOS.md).