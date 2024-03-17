#!/bin/bash
BUILD_PATH=$(ls /home/ubuntu/app/SobokSobok-0.0.1-SNAPSHOT.jar)

echo "> 현재 구동중인 Set 확인"
CURRENT_PROFILE=$(curl -s http://localhost/profile)
echo "> $CURRENT_PROFILE"

if [ $CURRENT_PROFILE == deploy-one ]
then
  IDLE_PROFILE=deploy-two
  IDLE_PORT=8082
  LIVE_PORT=8081
elif [ $CURRENT_PROFILE == deploy-two ]
then
  IDLE_PROFILE=deploy-one
  IDLE_PORT=8081
  LIVE_PORT=8082
else
  echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE"
  echo "> deploy-one을 할당합니다. IDLE_PROFILE: deploy-one"
  IDLE_PROFILE=deploy-one
  IDLE_PORT=8081
  LIVE_PORT=8082
fi

echo "> $IDLE_PROFILE 에서 구동중인 애플리케이션 pid 확인"
IDLE_PID=$(pgrep -f $IDLE_APPLICATION.$IDLE_PROFILE)

if [ -z "$IDLE_PID" ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $IDLE_PID"
  kill -15 $IDLE_PID
  sleep 10
fi

echo "> $IDLE_PROFILE 배포"
nohup java -jar -Duser.timezone=Asia/Seoul -Dspring.profiles.active=$IDLE_PROFILE $BUILD_PATH >> /home/ubuntu/app/nohup.out 2>&1 &

echo "> $IDLE_PROFILE 10초 후 Health check 시작"
echo "> curl -s http://localhost:$IDLE_PORT/actuator/health "
sleep 10

for retry_count in {1..10}
do
  response=$(curl -s http://localhost:$IDLE_PORT/actuator/health)
  up_count=$(echo $response | grep 'UP' | wc -l)

  if [ $up_count -ge 1 ]
  then
      echo "> Health check 성공"
      break
  else
      echo "> Health check의 응답을 알 수 없거나 혹은 status가 UP이 아닙니다."
      echo "> Health check: $response"
  fi

  if [ $retry_count -eq 10 ]
  then
    echo "> Health check 실패. "
    echo "> Nginx에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "> Health check 연결 실패. 재시도..."
  sleep 10
done

echo "> $LIVE_PORT에서 구동 중인 이전 버전의 애플리케이션 PID 확인"
LIVE_PID=$(sudo lsof -ti :$LIVE_PORT)

if [ ! -z "$LIVE_PID" ]; then
  echo "> kill -15 $LIVE_PID"
  kill -15 $LIVE_PID
  echo "> 이전 버전의 애플리케이션을 종료했습니다."
else
  echo "> 이전 버전의 애플리케이션이 구동 중이지 않아 종료하지 않습니다."
fi

echo "> 스위칭"
echo "> 전환할 Port: $IDLE_PORT"
echo "> Port 전환"
echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" |sudo tee /etc/nginx/conf.d/service-url.inc

PROXY_PORT=$(curl -s http://localhost/profile)
echo "> Nginx Current Proxy Port: $PROXY_PORT"

echo "> Nginx Reload"
sudo service nginx reload
