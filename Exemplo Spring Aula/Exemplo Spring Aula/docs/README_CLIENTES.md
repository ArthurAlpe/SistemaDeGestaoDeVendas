## Arthur Almeida Pereira

CRUD responsável: Clientes

Responsabilidades:
- Entity, DTO, Mapper, Repository, Service e Controller de Cliente
- Validações (nome, CPF, e-mail, data de nascimento) e tratamento de erros (400, 404, 409)
- Regras de negócio: CPF único, CPF válido (dígitos verificadores), cliente com vendas não pode ser excluído, endereço deve existir
- Relacionamento N:1 com Endereço e 1:N com Vendas
- Testes no Postman e documentação dos endpoints ([ver testes](TESTES_CLIENTES.md))