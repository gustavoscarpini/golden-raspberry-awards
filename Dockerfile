#First stage, run maven package
FROM maven:3.9.7-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn package -DskipTests

COPY target/*.jar /app/app.jar

#unzip jar
RUN jar xvf /app/app.jar

#list java modules
RUN cd /app &&\
     jdeps \
    --ignore-missing-deps \
    --recursive \
    --multi-release 21 \
    --class-path 'BOOT-INF/lib/*' \
    --list-deps app.jar > deps.txt


# Second stage, build the custom JRE
FROM eclipse-temurin:21-jdk-alpine AS jre-builder

# Install binutils, required by jlink
RUN apk update &&  \
    apk add binutils

COPY --from=build /app/deps.txt /deps.txt

# Build small JRE image
RUN jlink --compress=2  \
    --strip-debug \
    --no-header-files \
    --no-man-pages  \
 #   --add-modules $(sed '$!s/$/,/' /deps.txt) \
    --add-modules  java.base, jdk.internal.vm.annotation,\
    java.compiler,\
    java.desktop,\
    java.instrument,\
    java.logging,\
    java.management,\
    java.naming,\
    java.net.http,\
    java.prefs,\
    java.rmi,\
    java.scripting,\
    java.security.jgss,\
    java.sql,\
    java.sql.rowset,\
    java.transaction.xa,\
    java.xml,\
    jdk.jfr,\
    jdk.net,\
    jdk.unsupported \
    --output optimized-jdk-21

# Third stage, Use the custom JRE and build the app image
FROM alpine:latest
ENV JAVA_HOME=/opt/jdk/jdk-21
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# copy JRE from the base image
COPY --from=jre-builder /optimized-jdk-21 $JAVA_HOME

# Add app user
ARG APPLICATION_USER=spring

# Create a user to run the application, don't run as root
RUN addgroup --system $APPLICATION_USER &&  adduser --system $APPLICATION_USER --ingroup $APPLICATION_USER

# Create the application directory
RUN mkdir /app && chown -R $APPLICATION_USER /app

COPY --chown=$APPLICATION_USER:$APPLICATION_USER target/*.jar /app/app.jar

WORKDIR /app

USER $APPLICATION_USER

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]