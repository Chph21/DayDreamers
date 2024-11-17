FROM openjdk:17

WORKDIR /app

COPY ../.. .

RUN chmod +x ./mvnw

RUN ./mvnw clean package -DskipTests

RUN ./mvnw dependency:go-offline

COPY ./src ./src

EXPOSE 8080

CMD ["java", "-jar", "target/DayDreamer-0.0.1-SNAPSHOT.jar"]
