#!/bin/sh
source ./config.sh
application_name=$1
port=$2
service_name="${application_name}-server"


echo "container is stoping and removing"

containerId=$(docker ps -a | grep -E "${service_name}" | awk '{print $1}')

if [ ! -z $containerId ]
  then docker stop $containerId && docker rm $containerId
fi

imageId=$(docker images | grep -E "${service_name}" | awk '{print $1}')
if [ ! -z $imageId ]
  then docker rmi $imageId
fi

echo "image and container ware removed and image is building"
cd ..
mvn clean compile
cd ${service_name}
mvn clean compile
mvn package -Dmaven.test.skip=true docker:build

echo "build success and container is starting"

docker run -p ${port}:${port} \
       --env SERVER_HOSTNAME=${server_hostname} \
       --env PROFILE=${profile} \
       --env SERVER_PORT=${port} \
       --env EUREKA_URL=${eureka_url} \
       --env EUREKA_PORT=${eureka_port} \
       --env KAFKA_SERVERS=${kafka_servers} \
       --env ZK_SERVERS=${zk_servers} \
       --env GIT_URL=${git_url} \
       --env GIT_USERNAME=${git_username} \
       --env GIT_PWD=${git_pwd} \
       --name ${service_name} \
       -v /data/servers/logs/${service_name}/:/data/servers/logs/${service_name} \
       -t ${service_name}:1.0-SNAPSHOT
