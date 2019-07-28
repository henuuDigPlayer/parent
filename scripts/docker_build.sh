#!/bin/sh

application_name=$1

if [ -z $application_name ]
  then echo "application_name  is null" &&  exit 1
fi

echo "application=${application_name}"

echo "image is building"
cd ..
#install common module first
cd commons
mvn clean install
cd ../services/client
mvn clean install
#mvn clean install
cd ../../services/${application_name}

mvn clean package -Dmaven.test.skip=true docker:build

