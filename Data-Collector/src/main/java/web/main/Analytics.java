package web.main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import web.template.Article;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class Analytics {
    String workingDir;
    Map<Path, List<Article>> articleMap;

    public Analytics(String workingDir) {
        this.workingDir = workingDir;

        Path folderPath = Paths.get(System.getProperty("user.dir"))
                .resolve(workingDir)
                .normalize();

        List<Path> pathList = null;
        try (Stream<Path> paths = Files.walk(folderPath)) {
            pathList = paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json"))
                    .toList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Type articleListType = new TypeToken<List<Article>>(){}.getType();
        Map<Path, List<Article>> articleMap = new HashMap<>();

        for (Path path : pathList) {
            try (FileReader reader = new FileReader(path.toFile())) {
                List<Article> articleList = gson.fromJson(reader, articleListType);
                articleMap.put(path, articleList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.articleMap = articleMap;
    }

    public void aggregateArticles(String newFilename) {
        List<Article> articleArchive = new ArrayList<>();

        for (List<Article> articleList : articleMap.values()) {
            List<Article> articleListRealContent = articleList.stream()
                    .filter(article -> article.getContent().split("\\s+|\\n").length > 100)
                    .filter(article -> article.getSummary().split("\\s+|\\n").length > 1)
                    .toList();
            articleArchive.addAll(articleListRealContent);
        }

        Main main = new Main();
        main.writeToJson(articleArchive, workingDir + "/" + newFilename);
    }

    public void countArticles() {
        int totalArticles = 0;

        for (Map.Entry<Path, List<Article>> entry : articleMap.entrySet()) {
            Path path = entry.getKey();
            List<Article> articleList = entry.getValue();

            System.out.println("File [" + path + "] contains " + articleList.size() + " articles.");
            totalArticles += articleList.size();

        }

        System.out.println("Total: " + totalArticles + " articles.");
    }

    public void getDateInfo() {
        List<LocalDate> dateList = new ArrayList<>();
        Map<String, Integer> yearCounts = new TreeMap<>();

        for (List<Article> articleList : articleMap.values()) {
            List<LocalDate> localDateList = articleList.stream()
                    .map(Article::getDate)
                    .map(date -> LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .toList();
            dateList.addAll(localDateList);
            for (LocalDate date : localDateList) {
                String year = String.valueOf(date.getYear());
                yearCounts.put(year, yearCounts.getOrDefault(year, 0) + 1);
            }
        }

        System.out.println("Latest date: " + Collections.max(dateList));
        System.out.println("Earliest date: " + Collections.min(dateList));
        System.out.println("Yearly article counts: " + yearCounts);
        }

    public void getContentLengths() {
        int maxContentLength = 0, maxSummaryLength = 0;
        int minContentLength = Integer.MAX_VALUE, minSummaryLength = Integer.MAX_VALUE;
        Map<Integer, Integer> contentWordCounts = new TreeMap<>(), summaryWordCounts = new TreeMap<>();

        for (List<Article> articleList : articleMap.values()) {
            for (Article article : articleList) {
                String content = article.getContent();
                String summary = article.getSummary();
                int contentLength = content.split("\\s+|\\n").length;
                int summaryLength = summary.split("\\s+|\\n").length;

                maxContentLength = Math.max(maxContentLength, contentLength);
                maxSummaryLength = Math.max(maxSummaryLength, summaryLength);
                minContentLength = Math.min(minContentLength, contentLength);
                minSummaryLength = Math.min(minSummaryLength, summaryLength);

                int contentLengthFloored = (int) Math.floor((double) contentLength / 100) * 100;
                int summaryLengthFloored = (int) Math.floor((double) summaryLength / 10) * 10;

                contentWordCounts.put(contentLengthFloored, contentWordCounts.getOrDefault(contentLengthFloored, 0) + 1);
                summaryWordCounts.put(summaryLengthFloored, summaryWordCounts.getOrDefault(summaryLengthFloored, 0) + 1);
            }
        }

        System.out.println("Content length: min=" + minContentLength + ", max=" + maxContentLength);
        System.out.println("Summary length: min=" + minSummaryLength + ", max=" + maxSummaryLength);
        System.out.println("Content word counts: " + contentWordCounts);
        System.out.println("Summary word counts: " + summaryWordCounts);
    }
}
