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
git clone https://github.com/seu-usuario/nome-do-repositorio.git
cd nome-do-repositorio
```

2. Execute o comando Maven para baixar as dependências e construir o projeto:
```
git https://github.com/gustavoscarpini/golden-raspberry-awards
cd golden-raspberry-awards
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

1. Certifique-se de que o Docker está instalado e em execução.
2. Construa a imagem Docker do projeto:
```
docker build -t goldenraspberryawards .
```

3.	Execute o container Docker:
```
docker run -p 8080:8080 nome-do-projeto
```

4.	Acesse o Swagger UI no navegador:
```
http://localhost:8080/swagger-ui.html
```



[github-actions-generator-image]:https://github.com/gustavoscarpini/golden-raspberry-awards/actions/workflows/build-docker.yml/badge.svg
[github-actions-build-image]:https://github.com/gustavoscarpini/golden-raspberry-awards/actions/workflows/qa.yml/badge.svg
[github-actions-url]: https://github.com/gustavoscarpini/golden-raspberry-awards/actions
[sonar-url]: https://sonarcloud.io/dashboard?id=gustavoscarpini_golden-raspberry-awards
[sonar-quality-gate]: https://sonarcloud.io/api/project_badges/measure?project=gustavoscarpini_golden-raspberry-awards&metric=alert_status
[sonar-coverage]: https://sonarcloud.io/api/project_badges/measure?project=gustavoscarpini_golden-raspberry-awards&metric=coverage
[sonar-bugs]: https://sonarcloud.io/api/project_badges/measure?project=gustavoscarpini_golden-raspberry-awards&metric=bugs
[sonar-vulnerabilities]: https://sonarcloud.io/api/project_badges/measure?project=gustavoscarpini_golden-raspberry-awards&metric=vulnerabilities
