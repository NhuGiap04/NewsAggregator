package com.example.demoproject;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;


public class FilterEngine {
    static boolean mostRecentSelected = false;
    static LocalDate beginDate;
    static LocalDate endDate;
    static List<String> sources;
    static List<Article> searchResults;

    public static List<Article> searchByQuery(String query) {
        if (query == null || query.isEmpty()) {
            searchResults = Converter.convertFromFile("src/main/resources/com/example/demoproject/testarticles.json");
            return searchResults;
        }

        // Define your base URL
        String baseUrl = "http://localhost:9696/semantic-search?description=";

        // Encode your query string
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        // Append your encoded query to the base URL
        String apiUrl = baseUrl + encodedQuery;

        // Pass the URL to the convertFromApi method
        searchResults = Converter.convertFromRequest(apiUrl);
        return searchResults;
    }

    public static boolean checkDate(Article article) {
        LocalDate date = LocalDate.parse(article.getDate());
        return (endDate == null || date.isBefore(endDate)) && (beginDate == null || date.isAfter(beginDate));
    };

    public static boolean checkSource(Article article) {
        if (sources == null) {return true;}
        if (article != null) {
            for (String source : sources) {
                if (article.getLink().contains(source.toLowerCase())) {return true;}
            }
        }
        return false;
    }

    public static List<Article> applyFilter() {
        List<Article> output = new ArrayList<Article>();
        for (Article article : searchResults) {
            if (checkDate(article) && checkSource(article)) { output.add(article); }
        }
        if (mostRecentSelected) {
            List<Article> output2 = output;
            output = recentSort(output2);
        }
        return output;
    }

    public static List<Article> recentSort(List<Article> articles) {
        List<Article> filter = new ArrayList<Article>(articles.size());
        LocalDate[] dates = new LocalDate[articles.size()];
        for (int i = 0; i < articles.size(); i++) {
            dates[i] = articles.get(i).getLocalDate();
        }

        Arrays.sort(dates, Comparator.reverseOrder());

        for (int i = 0; i < articles.size(); i++) {
            for (Article article : articles) {
                if (article.getLocalDate().isEqual(dates[i])) {
                    filter.add(article);
                }
            }
        }
        return filter;
    }

    public static List<Article> recentSort(List<Article> articles, int max_articles) {
        List<Article> filter1 = recentSort(articles);
        List<Article> filter  = new ArrayList<Article>(articles.size());
        System.arraycopy(filter1, 0, filter, 0, max_articles);
        return filter;
    }

    public static LocalDate getBeginDate() {
        return beginDate;
    }

    public static void setBeginDate(LocalDate beginDate) {
        FilterEngine.beginDate = beginDate;
    }

    public static LocalDate getEndDate() {
        return endDate;
    }

    public static void setEndDate(LocalDate endDate) {
        FilterEngine.endDate = endDate;
    }

    public static List<String> getSources() {
        return sources;
    }

    public static void setSources(List<String> sources) {
        FilterEngine.sources = sources;
    }
}