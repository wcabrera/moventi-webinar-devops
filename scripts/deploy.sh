#!/bin/bash
set -e

APP_NAME="devops-webinar"
IMAGE="$1"
IMAGE_TAG="$2"

echo "=========================="
echo " DEVOPS WEBINAR DEPLOY"
echo "=========================="
echo "Image: ${IMAGE}:${IMAGE_TAG}"

echo "Pulling image from registry..."
docker pull ${IMAGE}:${IMAGE_TAG}

echo "Stopping previous container..."
docker stop ${APP_NAME} || true

echo "Removing previous container..."
docker rm ${APP_NAME} || true

echo "Starting new container..."
docker run -d \
  --name ${APP_NAME} \
  --restart unless-stopped \
  -p 80:8080 \
  ${IMAGE}:${IMAGE_TAG}

echo "Deployment completed"
docker ps
