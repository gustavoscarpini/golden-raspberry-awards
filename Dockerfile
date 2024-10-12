#First stage, run maven generate package
FROM maven:3.9-amazoncorretto-17-alpine AS build

WORKDIR /app

COPY pom.xml ./

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# Second stage, build the custom JRE
FROM  amazoncorretto:17.0.10-alpine3.18 AS jre-builder

RUN mkdir /app

COPY --from=build /app/target/*.jar /app/app.jar

#unzip jar
RUN jar xvf /app/app.jar

#list java modules
RUN  jdeps  --ignore-missing-deps -q  \
      --recursive  \
      --multi-release 17  \
      --print-module-deps  \
      --class-path 'BOOT-INF/lib/*'  \
      /app/app.jar > /app/deps.txt

# Install binutils, required by jlink
RUN apk update &&  \
    apk add binutils

# Build small JRE image
RUN jlink --compress=2  \
    --verbose \
    --no-header-files \
    --strip-debug \
    --no-header-files \
    --no-man-pages  \
    --add-modules $(cat /app/deps.txt) \
    --output optimized-jdk-17

# Third stage, Use the custom JRE and build the app image
FROM alpine:latest
ENV JAVA_HOME=/opt/jdk/jdk-17
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# copy JRE from the base image
COPY --from=jre-builder /optimized-jdk-17 $JAVA_HOME

# Add app user
ARG APPLICATION_USER=spring

# Create a user to run the application, don't run as root
RUN addgroup --system $APPLICATION_USER &&  adduser --system $APPLICATION_USER --ingroup $APPLICATION_USER

# Create the application directory
RUN mkdir /app && chown -R $APPLICATION_USER /app

COPY --chown=$APPLICATION_USER:$APPLICATION_USER --from=build /app/target/*.jar /app/app.jar

WORKDIR /app

USER $APPLICATION_USER

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]
