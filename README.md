# EventHub API

API RESTful para gerenciamento de eventos e venda de ingressos.

---

## Tecnologias

- Java 21
- Spring Boot 3.5.11
- Spring Data JPA + Hibernate
- H2 (banco in-memory)
- Bean Validation (Hibernate Validator)
- Lombok 1.18.30
- JUnit 5 + Mockito

---

## Como rodar o projeto

### Pré-requisitos

- Java 21 instalado e configurado.

### 1. Clonar o repositório

```bash
git clone <url-do-repositorio>
cd eventhub-api
```

### 2. Gerar o JAR e rodar a aplicação

```bash
.\mvnw.cmd clean package -DskipTests
java -jar target\eventhub-api-0.0.1-SNAPSHOT.jar
```

A API sobe em http://localhost:8080 com banco H2 em memória.

---

## Decisões técnicas

### Banco de dados: H2 in-memory

O banco escolhido para desenvolvimento e testes é o H2. A motivação principal é a zero configuração de ambiente: ao clonar o repositório, só executar o projeto e utilizar, sem precisar instalar ou configurar um banco externo.

### Persistência: Spring Data JPA + Hibernate

O Spring Data JPA abstrai o acesso a dados via interfaces JpaRepository, eliminando código repetitivo. O Hibernate atua como a implementação do JPA, gerenciando o ciclo de vida das entidades.

### Redução de boilerplate: Lombok

O Lombok gera em tempo de compilação os métodos getters, setters, construtores, builders, evitando código repetitivo nas classes.

### Validação: Bean Validation (Hibernate Validator)

As validações dos dados de entrada são declaradas via anotações Bean Validation diretamente nos DTOs e entidades. Qualquer violação é capturada e convertida em 400 Bad Request pelo GlobalExceptionHandler.

### Tratamento de erros: @RestControllerAdvice centralizado

Um único GlobalExceptionHandler intercepta todas as exceções de negócio e de validação da aplicação.

### Arquitetura em camadas: Controller → Service → Repository

A separação em camadas garante responsabilidade única para cada componente da camada: o controller apenas deserializa a requisição e delega; o service contém toda a lógica de negócio; o repository é responsável exclusivamente pelo acesso a dados.

### DTOs: Java Records

Os DTOs de entrada e saída da API são implementados como Java Records, que são imutáveis.

### Logs estruturados: SLF4J com @Slf4j

Os logs das operações críticas são emitidos via @Slf4j do Lombok na stdout.

### Graceful Shutdown

A propriedade server.shutdown=graceful garante que, ao receber um sinal de encerramento, o servidor Tomcat conclui as requisições em andamento antes de fechar.
