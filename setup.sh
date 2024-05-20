#!/bin/bash

cd API-Embed
python -m venv venv
pip install --no-cache-dir -r requirements.txt
activate

cd ../API-Search
mvn clean install

cd ../Aggregator-GUI
mvn compile