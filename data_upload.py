import json
from pymongo import MongoClient

# Define the path to the JSON file
file_path = 'updated_verified.json'

# Load the data from the JSON file
with open(file_path, 'r') as file:
    data = json.load(file)

# MongoDB Atlas connection string
uri = "mongodb+srv://<username>:<password>@cluster0.mongodb.net/<dbname>?retryWrites=true&w=majority"

# Establish a connection to the MongoDB server
client = MongoClient(uri)

# Select the database and collection
db = client['your_database']
collection = db['your_collection']

# Upload the data to the collection
collection.insert_many(data)