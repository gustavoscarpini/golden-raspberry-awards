## Golden Raspberry Awards

[![Generator Build Status][github-actions-build-image]][github-actions-url]
[![Generator Build Status][github-actions-generator-image]][github-actions-url]

[![sonar-quality-gate][sonar-quality-gate]][sonar-url] [![sonar-coverage][sonar-coverage]][sonar-url] [![sonar-bugs][sonar-bugs]][sonar-url] [![sonar-vulnerabilities][sonar-vulnerabilities]][sonar-url]


Este projeto tem como objetivo identificar o produtor com o maior intervalo entre dois prêmios consecutivos e o produtor que obteve dois prêmios mais rapidamente.


#### Tecnologias Utilizadas

- Java 17
- Spring Boot
- H2 Database
- Liquibase
- Swagger
- JUnit 
- Docker
- GitHub Actions para CI
- SonarCloud para Análise de Qualidade do Código

#### Funcionalidades

- API REST para gerenciar filmes e produtores
- Swagger UI para explorar e testar a API
- Liquibase para gerenciamento do esquema de banco de dados
- Testes de integração para garantir a confiabilidade da API
- Docker para facilitar a execução do projeto em um ambiente containerizado

#### Pré-requisitos

- Java 17
- Maven
- Docker (opcional, caso queira rodar via container)

### Como Executar o Projeto

#### Executando localmente (Java)

1.	Clone o repositório:
```
git clone https://github.com/gustavoscarpini/golden-raspberry-awards
cd golden-raspberry-awards
```

2. Execute o comando Maven para baixar as dependências e construir o projeto:
```
mvn clean install
```

3.	Inicie a aplicação:
```
mvn spring-boot:run
```

4.	Acesse o Swagger UI no seguinte endereço:
```
http://localhost:8080/swagger-ui.html
```

5. O banco de dados H2 pode ser acessado em:
```
http://localhost:8080/h2-console
```
	•	JDBC URL: jdbc:h2:mem:goldenraspberryawards
	•	User: sa
	•	Password: password

#### Executando via Docker

1. Baixe a imagem Docker
```
docker pull gustavoscarpini/golden-raspberry-awards:latest

```

2.	Execute o container Docker:
```
docker run -p 8080:8080 gustavoscarpini/golden-raspberry-awards
```

3. Acesse o Swagger UI no navegador:
```
http://localhost:8080/swagger-ui.html
```

#### Contruindo e Executando via Docker

1. Certifique-se de que o Docker está instalado e em execução.
2. Construa a imagem Docker do projeto:
```
docker build -t goldenraspberryawards .
```

3.	Execute o container Docker:
```
docker run -p 8080:8080 goldenraspberryawards
```

4.	Acesse o Swagger UI no navegador:
```
http://localhost:8080/swagger-ui.html
```


#### Integração Contínua e Entrega Contínua (CI/CD)

Este projeto utiliza GitHub Actions para automatizar o processo de CI/CD. As configurações incluem:

- Publicação de Imagem no Docker Hub: A imagem do Docker é construída e publicada automaticamente no Docker Hub sempre que há uma nova atualização no repositório.
- Análise de Qualidade do Código com SonarCloud: O código é analisado no SonarCloud para garantir que atenda aos padrões de qualidade e segurança antes da publicação.

[github-actions-generator-image]:https://github.com/gustavoscarpini/golden-raspberry-awards/actions/workflows/build-docker.yml/badge.svg
[github-actions-build-image]:https://github.com/gustavoscarpini/golden-raspberry-awards/actions/workflows/qa.yml/badge.svg
[github-actions-url]: https://github.com/gustavoscarpini/golden-raspberry-awards/actions
[sonar-url]: https://sonarcloud.io/dashboard?id=gustavoscarpini_golden-raspberry-awards
[sonar-quality-gate]: https://sonarcloud.io/api/project_badges/measure?project=gustavoscarpini_golden-raspberry-awards&metric=alert_status
[sonar-coverage]: https://sonarcloud.io/api/project_badges/measure?project=gustavoscarpini_golden-raspberry-awards&metric=coverage
[sonar-bugs]: https://sonarcloud.io/api/project_badges/measure?project=gustavoscarpini_golden-raspberry-awards&metric=bugs
[sonar-vulnerabilities]: https://sonarcloud.io/api/project_badges/measure?project=gustavoscarpini_golden-raspberry-awards&metric=vulnerabilities
