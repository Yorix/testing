#!/usr/bin/env bash

mvn clean package
echo "Copy..."

scp -i ~/.ssh/gc-testing target/testing-0.0.1-SNAPSHOT.jar yorix@34.72.23.189:/home/yorix/

echo "Restart..."

ssh -i ~/.ssh/gc-testing yorix@34.72.23.189 << EOF

pgrep java | xargs kill -9
nohup java -jar testing-0.0.1-SNAPSHOT.jar > log.txt &

EOF

echo "Complete!"
