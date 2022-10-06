gnome-terminal\
    --tab\
        --title="Zookeeper" -- bash -c "cd /home/akdp/dev/kafka_2.12-3.2.0/; bin/zookeeper-server-start.sh config/zookeeper.properties; $SHELL"
sleep 10
gnome-terminal\
    --tab\
        --title="KafkaServer" -- bash -c "cd /home/akdp/dev/kafka_2.12-3.2.0/; bin/kafka-server-start.sh config/server.properties; $SHELL"\
