FROM maven:3.8.3-openjdk-17 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
EXPOSE 8081
COPY --from=build /app/target/rectangles-0.0.1-SNAPSHOT.jar /rectangles-app.jar
ENTRYPOINT ["java","-jar","/rectangles-app.jar"]