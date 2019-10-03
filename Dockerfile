FROM hseeberger/scala-sbt:8u212_1.2.8_2.12.8 as build-deps

WORKDIR /usr/app/src

COPY build.sbt ./
COPY database-infrastructure ./database-infrastructure
COPY domain ./domain
COPY http-api ./http-api
COPY project ./project

RUN sbt httpApi/assembly

FROM openjdk:8-jdk-slim

WORKDIR /usr/app

COPY --from=build-deps /usr/app/src/http-api/target/scala-2.12/assembly.jar assembly.jar
COPY canal.db ./canal.db

EXPOSE 9000
CMD ["java", "-jar", "assembly.jar"]