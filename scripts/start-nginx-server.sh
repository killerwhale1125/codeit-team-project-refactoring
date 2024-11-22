#!/bin/bash

echo "--------------- start deploy -----------------"

docker network create --driver bridge dev_codeit_network || true

docker stop dev-codeit-nginx || true
docker rm dev-codeit-nginx || true
docker pull 535002866978.dkr.ecr.ap-northeast-2.amazonaws.com/dev-codeit-nginx:latest
docker run -d \
  --name dev-codeit-nginx \
  -p 80:80 \
  --network dev_codeit_network \
  -v ./nginx/nginx.conf:/etc/nginx/conf.d/nginx.conf \
  535002866978.dkr.ecr.ap-northeast-2.amazonaws.com/dev-codeit-nginx:latest

echo "--------------- end deploy -----------------"