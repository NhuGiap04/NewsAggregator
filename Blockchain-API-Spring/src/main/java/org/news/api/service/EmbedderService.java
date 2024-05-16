package org.news.api.service;

import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmbedderService {

    @Value("${embedder.url}")
    private String LOCAL_EMBEDDER_URL;

    private WebClient webClient;

    @PostConstruct
    void init() {
        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<List<Double>> createEmbedding(String text) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", text);
        return webClient.post()
                .uri(LOCAL_EMBEDDER_URL)
                .body(BodyInserters.fromValue(jsonObject.toString()))
                .retrieve()
                .bodyToMono(String.class)
                .map(jsonData -> {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    List<Double> embeddings = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        embeddings.add(jsonArray.getDouble(i));
                    }
                    return embeddings;
                });
    }

    public static void main(String[] args) {
        EmbedderService embedderService = new EmbedderService();
        embedderService.LOCAL_EMBEDDER_URL = "http://localhost:5000/embedding-post";

        embedderService.init();
        Mono<List<Double>> embeddings = embedderService.createEmbedding("An apple a day keeps the doctor away.");

        List<Double> embeddingsList = embeddings.block();
        System.out.println(embeddingsList);
    }
}