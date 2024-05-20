package web.cryptonews;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import web.template.Article;
import web.template.WebScraper;

import java.util.ArrayList;
import java.util.List;

public class CryptonewsScraper extends WebScraper {
    /**
     * Constructor for the WebScraper abstract class.
     * Starts the scraper thread upon initialization.
     * Add any additional parameter as needed.
     *
     * @param id             The unique identifier for this scraper.
     * @param urlList        The list of URLs to be scraped.
     * @param articleArchive The list for scraped Article objects, shared between scraper threads.
     */
    public CryptonewsScraper(String id, List<String> urlList, List<Article> articleArchive) {
        super(id, urlList, articleArchive);
        System.out.println("Crypto.news scraper no." + id + " initialized. Thread started upon initialization.");
    }

    @Override
    protected List<Article> scrape(List<String> urlList) {
        List<Article> articleList = new ArrayList<>();
        int dodged = 0;
        for (String url : urlList) {
            try {
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
                        .get();

                Article article = new Article();
                article.setLink(url);
                article.setType("news article");

                // TITLE
                Element title = doc.select("article > header > h1").first();
                String titleString = title.text();
                article.setTitle(titleString);

                // SUMMARY (can add fallback but author is also in another position so not worth splitting cases)
                Element summary = doc.select("div.post-detail__content > p:nth-child(1)").first();
                String summaryString = summary.text();
                article.setSummary(summaryString);

                // CONTENT
                Elements contents = doc.selectXpath("//div[@class='post-detail__content blocks']/*[(self::p or starts-with(name(), 'h')) and position() > 1]");
                List<String> contentList = contents.stream()
                        .map(Element::text)
                        .filter(s -> !(s.isEmpty()))
                        .toList();
                String contentString = String.join("\n", contentList);
                article.setContent(contentString);

                // DATE
                Element date = doc.select("article > header > div > time").first();
                String dateString = date.attr("datetime").substring(0, 10);
                article.setDate(dateString);

                // AUTHOR
                Element author = doc.select("article div.author-list__name > a").first();
                String authorString = author.text().toLowerCase();
                article.setAuthor(authorString);

                // TAGS
                Elements tags = doc.select("div.post-detail__tags > div > div > a");
                List<String> tagList = tags.stream()
                        .map(e -> e.text().toLowerCase())
                        .toList();
                article.setTagList(tagList);

                articleList.add(article);
            } catch (Exception e) {
                System.out.println("Error parsing " + url + ": " + e.getMessage());
                dodged++;
            }
        }
        System.out.println("Scraper no." + id + " scanned " + (urlList.size() - dodged) + " articles, unrecognized " + dodged + " articles. Dismissed.");
        return articleList;
    }

    public static void main(String[] args) {
        List<String> urlList = new ArrayList<>();
        List<Article> articleArchive = new ArrayList<>();

        // news, no headings in content
//        urlList.add("https://crypto.news/japan-based-akita-dog-society-adopts-blockchain-to-combat-forgery/");
        // features, small headings in content
//        urlList.add("https://crypto.news/ways-to-fund-your-blockchain-project/");
        // opinion article, no summary
//        urlList.add("https://crypto.news/european-unions-data-act-effectively-outlaws-true-smart-contracts-opinion/");
        // has old summary card without style attribute
        urlList.add("https://crypto.news/mining-firms-building-electrical-grids-to-circumvent-government-apprehensions/");
        // markets article
//        urlList.add("https://crypto.news/pups-meme-coin-surges/");


        CryptonewsScraper scraper = new CryptonewsScraper(
                "1",
                urlList,
                articleArchive);
        try {
            scraper.getThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        String json = gson.toJson(articleArchive);
        System.out.println(json);
    }
}
