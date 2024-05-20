package org.news.api.controller;

import org.news.api.service.ArticleService;
import org.news.api.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/semantic-search")
    public Mono<List<Article>> performSemanticSearch(@RequestParam("description") String description) {
        return articleService.getArticleSemanticSearch(description);
    }
}
