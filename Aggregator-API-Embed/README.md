# To run Embedding API

This mini-project will set up an API on port 9999 by default to create
embeddings from text for Semantic Search functionality of the Searching API.

To run, open Terminal in this folder. Make sure to have Python 3.12 (older
should be fine idk it's just for creating venv) installed, as well as system
environment variables set.

Create a virtual environment
```commandline
python -m venv venv
```
Install dependencies
```commandline
pip install -r requirements.txt
```
Activate the virtual environment on Windows
```commandline
venv\Scripts\activate
```
On Unix or MacOS
```commandline
source venv/bin/activate
```
Run the API server
```commandline
python app.py
```

## Common problems

python not found? Install Python and set environment variables. Also on some
systems the command might be `python3` or `py` instead of `python`. Just run
`python --version` and check with different commands.

Whatever problem with HuggingFace? Run `save_model.py` and try again.
