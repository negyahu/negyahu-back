#!/bin/bash
# Crawl current connected port of WAS
CURRENT_PORT=$(cat /home/ubuntu/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0 # Toggle port Number
if [ ${CURRENT_PORT} -eq 8001 ]; then
  TARGET_PORT=8002
elif [ ${CURRENT_PORT} -eq 8002 ]; then
  TARGET_PORT=8001
else echo "> No WAS is connected to nginx"
  exit 1
fi

echo "> Start health check of WAS at 'http://127.0.0.1:${TARGET_PORT}' ..."

echo "wait 5s.."
sleep 10

for RETRY_COUNT in 1 2 3 4 5 6 7 8 9 10
do
  echo "> #${RETRY_COUNT} trying..."
  sleep 5
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://127.0.0.1:${TARGET_PORT}/)
  echo "curl result : ${RESPONSE_CODE}"
  if [ ${RESPONSE_CODE} -eq 200 ]; then
    echo "> New WAS successfully running"
    exit 0
  elif [ ${RETRY_COUNT} -eq 10 ]; then
    echo "> Health check failed."
    sudo sh /home/ubuntu/scripts/notion_curl.sh ${TARGET_PORT} FAIL
    exit 1
  fi
done

