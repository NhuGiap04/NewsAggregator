from pymongo.mongo_client import MongoClient
from pymongo.server_api import ServerApi
import requests

uri = "mongodb+srv://NhuGiap:4C4KvNkypHWz02YY@cluster0.g7f8q5i.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"

# Create a new client and connect to the server
client = MongoClient(uri, server_api=ServerApi('1'))

# Send a ping to confirm a successful connection
try:
    client.admin.command('ping')
    print("Pinged your deployment. You successfully connected to MongoDB!")
except Exception as e:
    print(e)

db = client.Aggregator
collection = db.Blockchain

hf_token = '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'
embedding_url = "https://api-inference.huggingface.co/pipeline/feature-extraction/sentence-transformers/all-MiniLM-L6-v2"


def generate_embedding(text: str) -> list[float]:
    response = requests.post(
        embedding_url,
        headers={"Authorization": f"Bearer {hf_token}"},
        json={"inputs": text})

    if response.status_code != 200:
        raise ValueError(f"Request failed with status code {response.status_code}: {response.text}")

    return response.json()


# for doc in collection.find({'summary': {"$exists": True}}).limit(50):
#     doc['plot_embedding_hf'] = generate_embedding(doc['summary'])
#     collection.replace_one({'_id': doc['_id']}, doc)

query = "altcoin"

results = collection.aggregate([
  {"$vectorSearch": {
    "queryVector": generate_embedding(query),
    "path": "plot_embedding_hf",
    "numCandidates": 100,
    "limit": 4,
    "index": "PlotSemanticSearch",
      }}
])

for document in results:
    print(f'Article Name: {document["title"]},\nArticle Summary: {document["summary"]}\n')