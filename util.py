import pandas as pd
import numpy as np


# Data Preprocessing
def create_database():
    # Create a DataFrame
    df = pd.DataFrame(
        {'link': [], 'type': [], 'title': [], 'summary': [], 'content': [], 'date': [], 'author': [], 'tagList': []})
    files = ['blockchainnews.json', 'coindesk.json', 'cointelegraph.json', 'cryptonews.json']
    for file in files:
        path = 'Datasets/' + file
        data = pd.read_json(path)
        df = pd.concat([df, data], ignore_index=True, sort=False)
    return df


def get_content():
    data = pd.read_csv('../Database.csv')
    data = data.dropna(subset=['summary'])
    return data['summary'].to_numpy()


def replace_delimiter():
    with open('../Pre-trained Model/embed.txt', 'r') as data:
        plaintext = data.read()
    plaintext = plaintext.replace(',', ' ')
    with open('../Pre-trained Model/embed.txt', 'w') as data:
        data.write(plaintext)


def load_array():
    return np.loadtxt('../Pre-trained Model/embed.txt')


if __name__ == '__main__':
    X = load_array()
    print(X[0])
