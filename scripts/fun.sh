#!/bin/sh

function rand(){   
    min=$1   
    max=$(($2-$min+1))   
    num=$(date +%s%N)   
    echo $(($num%$max+$min))   
} 

function getServerPort(){
    port=$(rand $1 $2)   
    test_port=$(netstat -lnp | grep $port | awk '{print $7}' | awk -F '/' '{print $1}')

    while [ ! -z $test_port ];do
         port=$(rand $1 $2)
         test_port=$(netstat -lnp | grep $port | awk '{print $7}' | awk -F '/' '{print $1}')

    done

    echo ${port}
} 

