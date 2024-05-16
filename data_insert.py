import json
import requests
import os

# Define the path to the verified.json file
file_path = os.path.join('scraped data', 'verified.json')

# Load the data from the JSON file
with open(file_path, 'r', encoding='utf-8') as file:
    data = json.load(file)

# Iterate over each object in the data
for index, obj in enumerate(data):
    # Extract the summary
    summary = obj['summary']

    # Determine the port based on the index
    # port = 5000 + (index % 4)
    port = 5000

    # Define the localhost URL with the determined port
    url = f'http://localhost:{port}/embedding-post'

    # Send the summary in a POST request to the localhost URL
    response = requests.post(url, json={'text': summary})

    # Parse the response to get the embedding
    embedding = response.json()

    # Add the embedding to the object
    obj['embedding'] = embedding

# Write the updated data back to a new JSON file
with open('updated_verified.json', 'w') as file:
    json.dump(data, file, indent=2)
