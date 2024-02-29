cd ..
mvn clean package
sudo docker build . --file ./Dockerfile --tag sud0x67/crypto-strategy:local
sudo docker stop crypto-strategy-local > /dev/null 2>&1  && sudo docker rm crypto-strategy-local > /dev/null 2>&1
sudo docker run -d \
--restart=no \
--name crypto-strategy-local \
-v ./scripts/config/application-user.yaml:/etc/crypto-strategy/application-user.yaml \
-v -v ./log/:/var/log \
sud0x67/crypto-strategy:local