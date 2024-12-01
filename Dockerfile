FROM maven:3-eclipse-temurin AS build
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-alpine
COPY --from=build /target/*.jar demo.jar
EXPOSE 8080 
ENTRYPOINT ["java","-jar","demo.jar"]
