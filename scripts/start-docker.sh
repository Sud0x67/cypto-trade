sudo docker stop crypto-strategy > /dev/null 2>&1  && sudo docker rm crypto-strategy > /dev/null 2>&1
sudo docker login
sudo docker pull sud0x67/crypto-strategy:latest
sudo docker run -d \
--restart=always \
--name crypto-strategy \
-v ./config/application-user.yaml:/etc/crypto-strategy/application-user.yaml \
-v ./log/:/var/log \
sud0x67/crypto-strategy:latest