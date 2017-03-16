#/bin/bash
# This shell is to swap from outside the container
# parameter of image name to run and swap into

#if $1=activity2
if [ "$1" = 'activity2' ];
 then
 docker run -d --name=web2 --network=ecs189_default $1
 sleep 5 && docker exec ecs189_proxy_1 /bin/bash /bin/swap2.sh
 sleep 5 && docker kill web1
 docker rm web1
fi

#if $1=activity
if [ "$1" = 'activity' ];
 then
 docker run -d --name=web1 --network=ecs189_default $1
 sleep 5 && docker exec ecs189_proxy_1 /bin/bash /bin/swap1.sh
 sleep 5 && docker kill web2
 docker rm web2
fi