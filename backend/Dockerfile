# Estágio 1: Build com Maven
# Usamos uma imagem que já vem com Maven e o JDK 17, conforme seu pom.xml
FROM maven:3.9-eclipse-temurin-17 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia primeiro o pom.xml para aproveitar o cache de dependências do Docker
# Se o pom.xml não mudar, o Docker não baixará as dependências novamente
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o resto do código-fonte do projeto
COPY src ./src

# Executa o build do projeto, gerando o arquivo .jar. Pulamos os testes para um build mais rápido.
RUN mvn package -DskipTests

# Estágio 2: Execução
# Usamos uma imagem JRE (Java Runtime Environment) do Alpine, que é muito leve
FROM eclipse-temurin:17-jre-alpine

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo .jar gerado no estágio de build para a imagem final
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080, que é a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação quando o container for executado
ENTRYPOINT ["java", "-jar", "app.jar"]