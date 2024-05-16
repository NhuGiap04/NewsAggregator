#!/bin/bash

# Start 4 instances of the Flask application on ports 5000-5007
for port in {5000..5003}
do
    python app.py $port &
done