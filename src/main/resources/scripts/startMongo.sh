#!/bin/sh

# Use --stop to destroy existing mongodb container if already running (including all data in its volume)
# Usage : startMongo.sh
#         startMongo.sh --stop


MONGO_IMAGE="mongodb/mongodb-community-server:latest"
CONTAINER_NAME="mongodb"
STOP_EXISTING=${1:-no}

# Stop the container and then remove it
if [ "$(docker ps -q -f name="$CONTAINER_NAME")" ]; then
  echo "$CONTAINER_NAME container is already running"
  if [ "$STOP_EXISTING" = "--stop" ]; then
    echo "Stopping existing container $CONTAINER_NAME..."
    docker stop "$CONTAINER_NAME" > /dev/null 2>&1
    echo "Removing existing container $CONTAINER_NAME..."
    docker rm "$CONTAINER_NAME" > /dev/null 2>&1
    echo "Existing $CONTAINER_NAME is now stopped!"
    echo "Starting new container for $CONTAINER_NAME..."
    docker run --name $CONTAINER_NAME -p 27017:27017 -d $MONGO_IMAGE 2>/dev/null
  fi
else
  echo "Starting new container for $CONTAINER_NAME..."
  docker run --name $CONTAINER_NAME -p 27017:27017 -d $MONGO_IMAGE 2>/dev/null
fi


