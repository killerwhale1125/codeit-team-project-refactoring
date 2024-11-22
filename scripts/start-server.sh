#!/bin/bash

echo "--------------- start deploy -----------------"

docker network create --ipv6=false --driver bridge dev_codeit_network || true

docker stop dev-codeit-server || true
docker rm dev-codeit-server || true
docker pull 535002866978.dkr.ecr.ap-northeast-2.amazonaws.com/dev-codeit-server:latest
docker run -d \
  --name dev-codeit-server \
  -p 8080:8080 \
  --network dev_codeit_network \
  535002866978.dkr.ecr.ap-northeast-2.amazonaws.com/dev-codeit-server:latest

echo "--------------- end deploy -----------------"