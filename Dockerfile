#Use an official Maven image to build the project
FROM maven:3.8.4-openjdk-17 AS build
# Set the working directory inside the container
WORKDIR /app
# Copy the project's pom.xml file into the container
COPY pom.xml .
# Download the project dependencies
RUN mvn dependency:go-offline
COPY src/ /app/src/

RUN mvn package -Dmaven.test.skip=true
# Use the OpenJDK runtime as the final image
FROM openjdk:17-slim
# Set the working directory inside the container
WORKDIR /app
# Copy the compiled JAR file from the build stage
COPY --from=build /app/target/BoardGame.jar /app/BoardGame.jar

# Specify the command to run your application
CMD ["java", "-jar", "BoardGame.jar"]