FROM ghcr.io/graalvm/jdk-community:17 as graalvm

WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test


EXPOSE 8001



