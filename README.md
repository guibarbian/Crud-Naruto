
# Crud-Naruto 🀄

## O Projeto 📊

Este é um CRUD simples de batalha Ninja

## Tecnologias Utilizadas 🧭

- **SpringBoot** - Framework principal
- **Spring Data JPA** - Para interações com o banco de dados
- **PostgreSQL** - Banco de dados
- **H2** - Banco de dados em memória para testes
- **Spring Security** - Para autenticação e autorização
- **Spring Validation** - Para validação de dados
- **Swagger** - Para documentação da API
- **Maven** - Gerenciador de dependências
- **JUnit** - Para testes unitários
- **MockMVC** - Para testes de integração
- **Docker** - Para containerização da aplicação e do banco de dados

## Pré-requisitos

- Java 8 ou superior
- Maven
- IDE(recomendado IntelliJ ou VSCode)

## Como rodar o projeto 💿

Este projeto tem porte para Docker e Swagger, então você pode tanto cirar uma imagem para 
rodar o projeto via Docker, quanto checar a documentação em Swagger, quanto rodá-lo
localmente, para qualquer um destes, basta seguir as instruções:

### 1. Copie o repositório 
```bash
git clone https://github.com/guibarbian/Crud-Naruto
cd Crud-Naruto
```
### 2. Instale as dependências
```bash
mvn install
```
### 3. Rode a aplicação

#### Com Docker
```bash
docker-compose up
```

#### Com Swagger/ Localmente

```bash
mvn spring-boot:run
```
A aplicação vai ser executada em http://localhost:8080

Você pode usar algum cliente de API como Postman ou Insomnia para testar os endpoints 
manualmente ou acessar http://localhost:8080/swagger-ui/index.html para visualizar a 
documentação destes.

# Endpoints
## Segurança

Esta API conta com um padrão de segurança baseado em JWT (JSON Web Token) para autenticação e
autorização. Para utilizar os seus Endpoints, você deve registrar um usuário e logar com as credenciais
nescessarias(email e senha).

Para criar um usuário, basta acessar o endpoint `/api/v2/auth/registration` com um JSON body contendo as
seguintes informações:

```json
{
  "nome": "nomeDoUsuário",
  "senha": "senhaDoUsuário"
}
```

Ao criar o seu usuário(Endpoint retornou 201), você pode acessar o `/api/v2/auth/login` para se autenticar e utilizar os
demais endpoints. A autenticação requer um JSON body com as seguintes informações:

```json
{
  "nome": "nomeDoUsuário",
  "senha": "senhaDoUsuário"
}
```

Caso a API retorne 200, significa que você está autenticado, copie o token retornado e use-o para acessoar os
endpoints de `personagem`

## Personagens

Esta API tem os seguintes Endpoints para organização de tarefas

| Método | Endpoint                   | Descrição                                 |
|--------|----------------------------|-------------------------------------------|
| GET    | `/api/v2/personagens`      | Retorna todos os personagens por Paginção |
| GET    | `/api/v2/personagens/{id}` | Retorna um personagem por ID              |
| POST   | `/api/v2/personagens`      | Cria um personagem                        |
| PUT    | `/api/v2/personagens/{id}` | Atualiza um personagem                    |
| DELETE | `/api/v2/personagens/{id}` | Deleta um personagem                      |

## 

Para criar ou atualizar uma tarefa, você deve enviar um corpo JSON com os seguintes atributos:
```json
{
  "nome": "Sasuke Uchiha",
  "vida": 100,
  "chakra": 120,
  "jutsus": {
    "Sharingan":60,
    "Chidori": 50
  },
  "especialidade": "Ninjutsu"
}
```

O atributo `especialidade` é especialmente importante!
É ele que vai definir o tipo de ninja que você está criando ou atualizando,
são permitidos os seguintes tipos:
- Ninjutsu
- Genjutu
- Taijutsu

A diferença entre eles é a mensagem ao utilizar os seus `jutsus`

## Batalha ⚔

Na versão 2 da API foi adicionada a função de simular uma batalha entre os ninjas, 
para simular um confronto final entre dois personagens, basta saber o ID de cada um. Assim, acessando o 
endpoint `api/v2/batalhas/{idDoPrimeiroPersonagem}/{idDoSegundoPersonagem}`, você poderá
imaginar um confronto entre os dois!

As batalhas são baseadas no uso de chakra e nas condições de vida dos ninjas, se utilizando de
turnos para calcular a chance do ninja desviar ou não do ataque do outro, o resultado nem 
sempre será o mesmo, mesmo com um ninja mais fraco, você ainda pode vencer!

Para acompanhar mais de perto o embate, cheque os logs no terminal da aplicação!

# Desenvolvido com ⚙

- **IntelliJ IDEA**

# Autor ✏

- Guilherme A. Barbian 