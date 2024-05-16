import sys

from flask import Flask, request, jsonify
from generate_embedding import generate_embedding

app = Flask(__name__)


@app.route('/embedding-get', methods=['GET'])
def embedding_get():
    text = request.args.get('text')
    embedding = generate_embedding(text)
    return jsonify(embedding[0])


@app.route('/embedding-post', methods=['POST'])
def embedding_post():
    data = request.get_json()
    text = data['text']
    embedding = generate_embedding(text)
    return jsonify(embedding[0])


if __name__ == '__main__':
    port = int(sys.argv[1]) if len(sys.argv) > 1 else 5000
    app.run(debug=True, port=port)
