package org.news.api.service;

import org.news.api.model.Article;
import org.news.api.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final EmbedderService embedder;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, EmbedderService embedder) {
        this.articleRepository = articleRepository;
        this.embedder = embedder;
    }

    public Mono<List<Article>> getArticleSemanticSearch(String description) {
        Mono<List<Double>> embedderMono = embedder.createEmbedding(description);
        return embedderMono.flatMapMany(articleRepository::findArticleByVector)
                .collectList();
    }
}