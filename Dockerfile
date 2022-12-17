FROM openjdk:17-jdk

ADD build/libs/CashReceipt-0.0.1-SNAPSHOT.jar cashreceipt.jar

ENTRYPOINT ["java", "-jar", "cashreceipt.jar"]

EXPOSE 8080
