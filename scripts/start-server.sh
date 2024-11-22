#!/bin/bash

echo "--------------- start deploy -----------------"
docker stop dev-codeit-server || true
docker rm dev-codeit-server || true
docker pull 535002866978.dkr.ecr.ap-northeast-2.amazonaws.com/dev-codeit-server:latest
docker run -d --name instagram-server -p 8080:8080 535002866978.dkr.ecr.ap-northeast-2.amazonaws.com/dev-codeit-server:latest
echo "--------------- end deploy -----------------""