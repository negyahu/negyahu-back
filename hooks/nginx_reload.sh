#!/bin/bash

CURRENT_PORT=$(cat /home/ubuntu/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0
echo "> Nginx currently proxies to ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8001 ]; then
  TARGET_PORT=8002
elif [ ${CURRENT_PORT} -eq 8002 ]; then
  TARGET_PORT=8001
else
  echo "> No WAS is connected to nginx"
  exit 1
fi

echo "set \$service_url http://127.0.0.1:${TARGET_PORT};" |sudo tee /home/ubuntu/service_url.inc

echo "> Now Nginx proxies to ${TARGET_PORT}."

sudo service nginx reload

echo "> Nginx reloaded."

sudo sh /home/ubuntu/scripts/notion_curl.sh ${TARGET_PORT} SUCCESS

