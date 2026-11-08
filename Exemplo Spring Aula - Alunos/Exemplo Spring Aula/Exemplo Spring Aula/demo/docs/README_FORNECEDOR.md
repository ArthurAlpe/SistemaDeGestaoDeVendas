# CRUD de Fornecedor — integracao no projeto atual

O CRUD de Fornecedor foi integrado ao projeto existente, mantendo o padrao `com.example.demo`, `ApiResponse`, Swagger e a arquitetura em camadas usada pelo CRUD de Enderecos.

## Arquivos
- `Entities/Fornecedor.java`
- `dto/FornecedorRequest.java`
- `dto/FornecedorResponse.java`
- `repository/IFornecedorRepository.java`
- `service/FornecedorService.java`
- `controller/FornecedorController.java`
- `controller/FornecedorExceptionHandler.java`

## Endpoints
- `POST /api/fornecedores`
- `GET /api/fornecedores`
- `GET /api/fornecedores/{id}`
- `PUT /api/fornecedores/{id}`
- `DELETE /api/fornecedores/{id}`
- `PATCH /api/fornecedores/{id}/inativar`
- `PATCH /api/fornecedores/{id}/ativar`

## Relacionamento com Endereco
`Fornecedor` possui `@ManyToOne` para `Endereco`, usando `endereco_id`, sem cascade. O DTO recebe `enderecoId` e o Service verifica a existencia do endereco antes da associacao. O CRUD de Enderecos tambem bloqueia a exclusao de endereco usado por fornecedor.

## Relacionamento com Produto
Nao foi adicionada `List<Produto>` em `Fornecedor`. A FK deve ficar no lado de Produto:

```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "fornecedor_id", nullable = false)
private Fornecedor fornecedor;
```

O DELETE verifica `produtos.fornecedor_id` e retorna 409 quando ha vinculo.

## Regras implementadas
1. CNPJ e normalizado; CNPJs numericos tem digitos verificadores validados.
2. E obrigatorio informar telefone ou e-mail.
3. CNPJ nao pode ser duplicado.
4. Fornecedor com produto vinculado nao pode ser excluido; pode ser inativado.

## Banco esperado
`fornecedores`: `id`, `razao_social`, `nome_fantasia`, `cnpj`, `telefone`, `email`, `endereco_id`, `ativo`.

A branch enviada ainda nao contem a Entity/CRUD de Produto; por isso a checagem de produtos vinculados usa consulta SQL nativa e sera compativel quando a tabela `produtos` existir com `fornecedor_id`.
