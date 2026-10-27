## Felipe Bernardi

**CRUD responsável:** Endereços (`/api/enderecos`)

### Responsabilidades

- Entity `Endereco` (tabela `enderecos`);
- Repository `IEnderecoRepository`;
- DTO `EnderecoDTO` e Mapper `EnderecoMapper` (MapStruct);
- Service `EnderecoService`;
- Controller `EnderecoController`;
- tratamento de erros do CRUD (`EnderecoExceptionHandler`);
- validações;
- regras de negócio;
- relacionamento: o Endereço é referenciado por Cliente e Fornecedor (N:1);
- testes dos endpoints (Postman);
- documentação dos endpoints (Swagger e este README).

### Finalidade

Guardar os endereços usados pelos clientes e fornecedores. Um endereço pode ser compartilhado por vários clientes ou fornecedores, e cada cliente ou fornecedor aponta para um único endereço.

### Tabela `enderecos`

| Campo | Tipo | Regra |
|-------|------|-------|
| id | Long | Chave primária, gerada pelo banco |
| cep | String (8) | Obrigatório, guardado só com dígitos |
| logradouro | String (150) | Obrigatório |
| numero | String (20) | Obrigatório |
| complemento | String (100) | Opcional |
| bairro | String (100) | Obrigatório |
| cidade | String (100) | Obrigatório |
| estado | String (2) | Obrigatório, sigla da UF em maiúsculas |

### Relacionamento

`CLIENTES N:1 ENDERECOS` e `FORNECEDORES N:1 ENDERECOS`.

A chave estrangeira `endereco_id` fica nas tabelas `clientes` e `fornecedores` (lado "N"), por isso a entidade `Endereco` não possui nenhum campo apontando para elas. Os outros CRUDs usam `EnderecoService.buscarEntidade(id)` para obter o endereço e ligar a chave estrangeira.

### Arquitetura

```
EnderecoController  ->  EnderecoService  ->  IEnderecoRepository  ->  PostgreSQL
 (HTTP)                 (regras)              (consultas)               (tabela enderecos)
```

| Camada | Arquivo | Função |
|--------|---------|--------|
| Entity | `Entities/Endereco.java` | Representa a tabela `enderecos` |
| DTO | `dto/EnderecoDTO.java` | Dados de entrada e saída da API, com as validações |
| Mapper | `mapper/EnderecoMapper.java` | Converte entre Entity e DTO |
| Repository | `repository/IEnderecoRepository.java` | Acesso ao banco (herda o CRUD do `JpaRepository`) |
| Service | `service/EnderecoService.java` | Regras de negócio |
| Controller | `controller/EnderecoController.java` | Endpoints REST |
| Tratamento de erros | `controller/EnderecoExceptionHandler.java` | Converte os erros do Service em respostas HTTP |

### Endpoints

| Método | Rota | Descrição | Sucesso | Erros possíveis |
|--------|------|-----------|---------|-----------------|
| POST | `/api/enderecos` | Cadastra um endereço | 201 | 400, 409 |
| GET | `/api/enderecos` | Lista todos | 200 | - |
| GET | `/api/enderecos/{id}` | Busca por ID | 200 | 404 |
| PUT | `/api/enderecos/{id}` | Atualiza um endereço | 200 | 400, 404, 409 |
| DELETE | `/api/enderecos/{id}` | Exclui um endereço | 204 | 404 |

**Parâmetro de rota:** `{id}` é o identificador numérico do endereço.

**Corpo da requisição (POST e PUT):**

```json
{
  "cep": "87020-000",
  "logradouro": "Avenida Colombo",
  "numero": "5790",
  "complemento": "Bloco 1",
  "bairro": "Zona 7",
  "cidade": "Maringá",
  "estado": "pr"
}
```

**Resposta de sucesso:** o endereço salvo, com o CEP sem hífen e o estado em maiúsculas (`"cep": "87020000"`, `"estado": "PR"`). Os endpoints de cadastro e atualização devolvem o endereço dentro do envelope `ApiResponse`, e os GET devolvem o endereço diretamente.

**Documentação interativa:** com a aplicação rodando, acesse `http://localhost:8080/swagger-ui.html`, seção "Endereços".

### Validações (`EnderecoDTO`)

- CEP obrigatório, no formato `00000-000` ou `00000000`;
- logradouro, número, bairro e cidade obrigatórios;
- estado obrigatório, com exatamente 2 letras;
- tamanho máximo em todos os textos (logradouro 150, número 20, complemento 100, bairro 100, cidade 100).

Quando alguma validação falha, a API devolve **400** com os campos que têm problema.

### Regras de negócio (`EnderecoService`)

**Regra 01: normalização do CEP e validação da UF**
Implementada no método `normalizar`. O CEP é guardado apenas com os 8 dígitos (`87020-000` vira `87020000`), e o estado é convertido para maiúsculas e precisa ser uma UF brasileira válida. Caso contrário, a API devolve **400**.

**Regra 02: não permitir endereço duplicado**
Implementada no método `validarDuplicidade`. Não é permitido ter dois endereços com o mesmo CEP, número e complemento. No cadastro usa `existsByCepAndNumeroAndComplemento` e, na atualização, `existsByCepAndNumeroAndComplementoAndIdNot`, para que o registro não seja considerado duplicado dele mesmo. Em caso de duplicidade, a API devolve **409**.

### Tratamento de erros

| Situação | Status |
|----------|--------|
| Cadastro ou atualização bem-sucedidos | 200 / 201 |
| Exclusão bem-sucedida | 204 |
| Dados inválidos, CEP ou UF inválidos, JSON malformado | 400 |
| Endereço não encontrado | 404 |
| Endereço duplicado | 409 |
| Erro interno | 500 |

### Testes

Os testes foram feitos no Postman, cobrindo as 5 operações do CRUD e as situações inválidas (duplicado, ID inexistente, atualizar e excluir inexistente, dados inválidos e regras violadas). As evidências com os prints estão em [`docs/TESTES_ENDERECOS.md`](TESTES_ENDERECOS.md).