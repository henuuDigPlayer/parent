#!/bin/sh

application_name=$1

if [ -z $application_name ]
  then echo "application_name  is null" &&  exit 1
fi

echo "application=${application_name}"

echo "image is building"
cd ..
mvn clean install
cd ${application_name}-server

mvn clean package -Dmaven.test.skip=true docker:build

