# Evidências - CRUD de Endereços

## 1. Entidade e tabela
A entidade `Endereco` gera a tabela `enderecos` no PostgreSQL.

![Tabela enderecos no pgAdmin](enderecos/tabela-pgadmin.png)

### 1.2 Repository
O `EnderecoRepository` estende `JpaRepository<Endereco, Long>` e possui duas consultas
para a regra de duplicidade (cadastro e atualização). Ao subir a aplicação, o Spring
reconhece o repository e valida os nomes dos métodos contra a entidade.

![Aplicação subindo com o repository reconhecido](enderecos/repository-startup.png)

### 1.3 DTO

A classe `EnderecoDTO` define os dados utilizados na transferência de informações de endereços pela API. Possui validações para campos obrigatórios, limites de caracteres e formato do CEP. A classe foi criada e integrada ao projeto, porém suas validações serão testadas durante a implementação do Controller e dos endpoints.

![Aplicação iniciada após a criação do DTO](enderecos/dto-startup.png)

### 1.4 Mapper

A classe `EnderecoMapper` utiliza o MapStruct para converter dados entre `EnderecoDTO` e `Endereco`, incluindo a conversão de listas e a atualização de uma entidade existente sem alterar seu identificador.

Após a implementação da classe, a aplicação foi iniciada para verificar se o projeto continuava executando normalmente.

![Compilação do projeto com sucesso](enderecos/mapper-evidence.png)