#!/bin/sh

source ./conf.sh

application_name=$1
service_name="${application_name}-server"

port=$2
echo "application=${application_name}"

if [ -z $application_name ]
  then echo "application_name  is null" &&  exit 1
fi

if [ -z $port ]
  then echo "port  is null" &&  exit 1
fi

name=${service_name}"_"${port}"_"${image_version}

echo "container ${name}  is starting"

docker run -p ${port}:${port} \
       --env PROFILE=${profile} \
       --env SERVER_PORT=${port} \
       --env EUREKA_URL=${eureka_url} \
       --env EUREKA_PORT=${eureka_port} \
       --env APP_NAME=${service_name} \
       --env KAFKA_SERVERS=${kafka_servers} \
       --env ZK_SERVERS=${zk_servers} \
       --env GIT_URL=${git_url} \
       --env GIT_USERNAME=${git_username} \
       --env GIT_PWD=${git_pwd} \
       --name ${name} \
       -v /data/servers/logs/${service_name}/:/data/servers/logs/${service_name} \
       -t ${service_name}:${image_version}
