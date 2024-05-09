The Search Engine I implemented was used MongoDB and Hugging Face API. The MongoDB Database Server was used to store the data and search by the article index by using K-nearest
neighbors on the embedded vector representation of each sentence. We get those embed vectors by using Hugging Face API model: sentence-transformers/all-MiniLM-L6-v2
