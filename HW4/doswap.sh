#/bin/bash
# This shell is to swap from outside the container
# parameter of image name to run and swap into
# For this swap to work properly in the general case, would need parameters: image name, container name, swapfile name, old container name
# Would also need parameter for network name

function killitif {
    docker ps -a  > /tmp/yy_xx$$
    if grep --quiet $1 /tmp/yy_xx$$
     then
     echo "killing older version of $1"
     docker rm -f `docker ps -a | grep $1  | sed -e 's: .*$::'`
   fi
}

#if $1=activity2
#if [ "$1" = 'activity2' ];
# then
# docker run -d --name=web2 --network=ecs189_default $1
# sleep 5 && docker exec ecs189_proxy_1 /bin/bash /bin/swap2.sh
# sleep 5 && docker kill web1
# docker rm web1
#fi

#if $1=activity
#if [ "$1" = 'activity' ];
# then
# docker run -d --name=web1 --network=ecs189_default $1
# sleep 5 && docker exec ecs189_proxy_1 /bin/bash /bin/swap1.sh
# sleep 5 && docker kill web2
# docker rm web2
#fi


docker run -d --name=$2 --network=ecs189_default $1
sleep 5 && docker exec ecs189_proxy_1 /bin/bash /bin/$3
sleep 5 && killitif $4