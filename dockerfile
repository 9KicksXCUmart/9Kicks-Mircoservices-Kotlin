FROM eclipse-temurin:17.0.10_7-jre-alpine 

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean
RUN ./gradlew build --scan

EXPOSE 8080

CMD ["sh", "-c", "./gradlew bootRun" ]
