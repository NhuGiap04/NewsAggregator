# News Aggregator

A news aggregator application providing interesting ways to interact with
prefetched news articles about Blockchain.

App structure:
```markdown
News Aggregator
|
├── Aggregator-GUI :
|       A GUI application, using JavaFX.
|
├── Aggregator-API-Embed :
|       An API application for embedding queries, using Python Flask, running on port 9999.
|
├── Aggregator-API-Search :
|       An API application for interacting with MongoDB, using Java Spring Boot, running on port 9696.

```

Individual modules' guides to setup and run are provided in their respective
directories. Yet for peace of mind, you can open Bash (native Terminal on
MacOS/Linux, or Git Bash on Windows) in this main folder and run the following
commands:

```bash
./setup.sh
```
```bash
./run.sh
```

Don't hesitate to adjust the 2 scripts to your needs (or your environment
variables).