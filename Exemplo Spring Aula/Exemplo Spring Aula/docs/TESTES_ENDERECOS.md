# Evidências - CRUD de Endereços

## 1. Entidade e tabela
A entidade `Endereco` gera a tabela `enderecos` no PostgreSQL.

![Tabela enderecos no pgAdmin](enderecos/tabela-pgadmin.png)

### 1.2 Repository
O `EnderecoRepository` estende `JpaRepository<Endereco, Long>` e possui duas consultas
para a regra de duplicidade (cadastro e atualização). Ao subir a aplicação, o Spring
reconhece o repository e valida os nomes dos métodos contra a entidade.

![Aplicação subindo com o repository reconhecido](enderecos/repository-startup.png)