#!/bin/bash

cd API-Embed
python app.py &

cd ../API-Search
mvn spring-boot:run &

cd ../Aggregator-GUI
mvn exec:java -D"exec.mainClass"="com.example.aggregator.AggregatorMainClass"