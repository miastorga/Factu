FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY target/hospital-vm-0.0.1-SNAPSHOT.jar app.jar
COPY Wallet_N72BZHZWYZGTE7OH /app/oracle_wallet

EXPOSE 8081

CMD ["java","-jar","app.jar"]