#!/bin/bash
set -e

APP_NAME="devops-webinar"
IMAGE_NAME="devops-webinar"
IMAGE_TAG="$1"

echo "=========================="
echo " DEVOPS WEBINAR DEPLOY"
echo "=========================="
echo "Image tag: ${IMAGE_TAG}"

echo "Building Docker image..."
docker build \
  -t ${IMAGE_NAME}:${IMAGE_TAG} \
  -t ${IMAGE_NAME}:latest \
  .

echo "Stopping previous container..."
docker stop ${APP_NAME} || true

echo "Removing previous container..."
docker rm ${APP_NAME} || true

echo "Starting new container..."
docker run -d \
  --name ${APP_NAME} \
  --restart unless-stopped \
  -p 80:8080 \
  ${IMAGE_NAME}:${IMAGE_TAG}

echo "Deployment completed"
docker ps
