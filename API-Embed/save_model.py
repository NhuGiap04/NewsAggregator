# Load model directly
from transformers import AutoTokenizer, AutoModel

tokenizer = AutoTokenizer.from_pretrained("sentence-transformers/all-MiniLM-L6-v2")
model = AutoModel.from_pretrained("sentence-transformers/all-MiniLM-L6-v2")

tokenizer.save_pretrained("all-MiniLM-L6-v2")
model.save_pretrained("all-MiniLM-L6-v2")
