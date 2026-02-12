# docker build -t lead-training .
# docker run -p 8080:8080 lead-training

ARG REPO_URL=https://github.com/velindoychinov/lead-training.git
ARG REPO_REF=main

FROM maven:3.9.6-eclipse-temurin-17 AS build

ARG REPO_URL
ARG REPO_REF

WORKDIR /build
RUN apt-get update && apt-get install -y git

RUN git clone --branch ${REPO_REF} ${REPO_URL} .

RUN ./mvnw -B clean package -DskipTests


FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /build/target/*jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]