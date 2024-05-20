#!/bin/bash

cd Aggregator-API-Embed
python app.py &

cd ../Aggregator-API-Search
mvn spring-boot:run &

cd ../Aggregator-GUI
mvn exec:java -D"exec.mainClass"="com.example.aggregator.AggregatorMainClass"