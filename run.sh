#!/bin/bash

cd Aggregator-API-Embed
if [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "cygwin" ]] || [[ "$OSTYPE" == "win32" ]]; then
    source venv/Scripts/activate
else
    source venv/bin/activate
fi
python app.py &

cd ../Aggregator-API-Search
mvn spring-boot:run &

cd ../Aggregator-GUI
mvn exec:java -D"exec.mainClass"="com.example.aggregator.AggregatorMainClass"