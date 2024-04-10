FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean
RUN ./gradlew build --warning-mode all

EXPOSE 8080

CMD ["sh", "-c", "./gradlew bootRun" ]
