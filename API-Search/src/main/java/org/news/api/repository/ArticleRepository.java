package org.news.api.repository;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;

import org.news.api.model.Article;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.mongodb.client.model.Aggregates.vectorSearch;
import static com.mongodb.client.model.search.SearchPath.fieldPath;
import static java.util.Arrays.asList;

@Repository
public class ArticleRepository {

    private final MongoDatabase mongoDatabase;

    @Value("${mongodb.collection}")
    private String MONGODB_COLLECTION;

    @Value("${mongodb.searchindex.name}")
    private String INDEX_NAME;

    @Value("${mongodb.searchindex.field}")
    private String INDEX_FIELD;

    public ArticleRepository(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    private MongoCollection<Article> getArticleCollection() {
        return mongoDatabase.getCollection(MONGODB_COLLECTION, Article.class);
    }

    public Flux<Article> findArticleByVector(List<Double> embedding) {
        String indexName = INDEX_NAME;
        int numCandidates = 100;
        int limit = 10;

        List<Bson> pipeline = asList(
                vectorSearch(
                        fieldPath(INDEX_FIELD),
                        embedding,
                        indexName,
                        numCandidates,
                        limit));

        return Flux.from(getArticleCollection().aggregate(pipeline, Article.class));
    }
}
