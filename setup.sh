#!/bin/bash

cd Aggregator-API-Embed
python -m venv venv
if [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "cygwin" ]] || [[ "$OSTYPE" == "win32" ]]; then
    .venv/Scripts/activate
else
    source venv/bin/activate
fi
pip install --no-cache-dir -r requirements.txt

cd ../Aggregator-API-Search
mvn clean install

cd ../Aggregator-GUI
mvn compile