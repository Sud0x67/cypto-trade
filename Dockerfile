FROM amd64/eclipse-temurin:17.0.10_7-jdk-focal

RUN mkdir -p /opt/projects/crypto-trade

WORKDIR /opt/projects/crypto-trade

COPY crypto-trade-web-api/target/crypto-trade-web-api-0.0.1.jar ./

CMD java -jar -Xms1024m -Xmx2048m -Xss512k -XX:SurvivorRatio=8 -Dspring.config.additional-location=/etc/crypto-strategy/application-user.yaml crypto-trade-web-api-0.0.1.jar
