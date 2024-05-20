package web.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import web.blockchainnews.BlockchainnewsCrawler;
import web.blockchainnews.BlockchainnewsScraper;
import web.coindesk.CoindeskCrawler;
import web.coindesk.CoindeskScraper;
import web.cointelegraph.CointelegraphCrawler;
import web.cointelegraph.CointelegraphScraper;
import web.cryptonews.CryptonewsCrawler;
import web.cryptonews.CryptonewsScraper;
import web.template.Article;
import web.template.WebCrawler;
import web.template.WebScraper;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public void writeToJson(List<Article> articleArchive, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            gson.toJson(articleArchive, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Article> getArticles(List<String> urlArchive, int numOfScraperThread, Class<?> scraperClass) {
        // SPLIT
        List<List<String>> urlSubsets = new ArrayList<>();
        int subsetSize = Math.ceilDiv(urlArchive.size(), numOfScraperThread);
        for (int i = 0; i < urlArchive.size(); i += subsetSize) {
            urlSubsets.add(urlArchive.subList(i, Math.min(i + subsetSize, urlArchive.size())));
        }

        // SCRAPE
        List<Article> articleArchive = Collections.synchronizedList(new ArrayList<>());
        List<WebScraper> scraperList = new ArrayList<>();
        for (int id = 0; id < numOfScraperThread; id++) {
            try {
                Constructor<WebScraper> constructor = (Constructor<WebScraper>) scraperClass.getConstructor(String.class, List.class, List.class);
                WebScraper scraper = constructor.newInstance(String.valueOf(id + 1), urlSubsets.get(id), articleArchive);
                scraperList.add(scraper);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Subset is " + urlSubsets.size() + " and whole is " + urlArchive.size());
                throw new RuntimeException(e);
            }
        }
        for (WebScraper scraper : scraperList) {
            try {
                scraper.getThread().join();
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return articleArchive;
    }

    private List<Article> getCointelegraphArticles(int numOfScraperThread, Object parameter) {
        String baseUrl = "https://cointelegraph.com/tags/blockchain";

        List<String> urlArchive = new ArrayList<>();
        WebCrawler crawler = null;
        if (parameter instanceof Integer) { // if parameter is integer
            crawler = new CointelegraphCrawler("1", baseUrl, urlArchive, (int) parameter);
        } else if (parameter instanceof String) { // if parameter is date string
            crawler = new CointelegraphCrawler("1", baseUrl, urlArchive, (String) parameter);
        }
        try {
            crawler.getThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return getArticles(urlArchive, numOfScraperThread, CointelegraphScraper.class);
    }

    public List<Article> getCointelegraphArticles(int numOfScraperThread, String furthestDate) {
        return getCointelegraphArticles(numOfScraperThread, (Object) furthestDate);
    }

    public List<Article> getCointelegraphArticles(int numOfScraperThread, int numOfLoadMore) {
        return getCointelegraphArticles(numOfScraperThread, (Object) numOfLoadMore);
    }

    public List<Article> getBlockchainnewsArticles(int numOfScraperThread) {
        String baseUrl = "https://blockchain.news/search/blockchain";

        List<String> urlArchive = new ArrayList<>();
        WebCrawler crawler = new BlockchainnewsCrawler("1", baseUrl, urlArchive);
        try {
            crawler.getThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return getArticles(urlArchive, numOfScraperThread, BlockchainnewsScraper.class);
    }

    public List<Article> getCoindeskArticle(int numOfScraperThread) {
        String baseUrl = "https://www.coindesk.com/tag/blockchain/";
        int numOfCrawlerThread = 8;
        int numOfPage = 15;

        List<String> urlArchive = new ArrayList<>();
        List<CoindeskCrawler> crawlerList = new ArrayList<>();
        for (int id = 0; id < numOfCrawlerThread; id++) {
            crawlerList.add(new CoindeskCrawler(
                    String.valueOf(id + 1),
                    baseUrl,
                    urlArchive,
                    (id * numOfPage + 1),
                    (Math.min(id * numOfPage + numOfPage, 119))
            ));
        }
        for (CoindeskCrawler c : crawlerList) {
            try {
                c.getThread().join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return getArticles(urlArchive, numOfScraperThread, CoindeskScraper.class);
    }

    private List<Article> getCryptonewsArticles(int numOfScraperThread, Object parameter) {
        String baseUrl = "https://crypto.news/tag/blockchain/";

        List<String> urlArchive = new ArrayList<>();
        WebCrawler crawler = null;
        if (parameter instanceof Integer) { // if parameter is integer
            crawler = new CryptonewsCrawler("1", baseUrl, urlArchive, (int) parameter);
        } else if (parameter instanceof String) { // if parameter is date string
            crawler = new CryptonewsCrawler("1", baseUrl, urlArchive, (String) parameter);
        }
        try {
            crawler.getThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return getArticles(urlArchive, numOfScraperThread, CryptonewsScraper.class);
    }

    public List<Article> getCryptonewsArticles(int numOfScraperThread, String furthestDate) {
        return getCryptonewsArticles(numOfScraperThread, (Object) furthestDate);
    }

    public List<Article> getCryptonewsArticles(int numOfScraperThread, int numOfLoadMore) {
        return getCryptonewsArticles(numOfScraperThread, (Object) numOfLoadMore);
    }

    public static void main(String[] args) {
        Main main = new Main();
        String startTime = LocalTime.now().toString();

//        // Cointelegraph
//        List<Article> articleArchive1 = main.getCointelegraphArticles(4, "2018-01-01");
//        main.writeToJson(articleArchive1, "data-json/cointelegraph-testicle.json");

//        // Blockchain news
//        List<Article> articleArchive2 = main.getBlockchainnewsArticles(8);
//        main.writeToJson(articleArchive2, "data-json/blockchainnews-testify.json");

//        // Coindesk
//        List<Article> articleArchive3 = main.getCoindeskArticle(8);
//        main.writeToJson(articleArchive3, "data-json/coindesk-testimony.json");

//        // Crypto.news
//        List<Article> articleArchive4 = main.getCryptonewsArticles(8, "2018-01-01");
//        main.writeToJson(articleArchive4, "data-json/cryptonews-test.json");

        String endTime = LocalTime.now().toString();
        Analytics analytics = new Analytics("data-json-verified");
//        analytics.aggregateArticles("verified.json");

        System.out.println("*******************REPORT*******************");
        System.out.println("Start time: " + startTime);
        System.out.println("End time: " + endTime);
        analytics.countArticles();
        analytics.getDateInfo();
        analytics.getContentLengths();
        System.out.println("********************************************");
    }
}
