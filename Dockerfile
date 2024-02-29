FROM openjdk:8u282-jre-buster

RUN mkdir -p /opt/projects/crypto-web-backend

WORKDIR /opt/projects/crypto-web-backend

COPY crypto-web-api/target/crypto-web-api-0.0.1-SNAPSHOT.jar ./

CMD java -jar -Xms1024m -Xmx2048m -Xss512k -XX:SurvivorRatio=8 -Dspring.config.additional-location=/etc/crypto-strategy/application-user.yaml crypto-web-api-0.0.1-SNAPSHOT.jar
