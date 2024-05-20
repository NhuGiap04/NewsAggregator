package web.coindesk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import web.template.Article;
import web.template.WebScraper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class CoindeskScraper extends WebScraper {
    /**
     * Constructor for the WebScraper abstract class.
     * Starts the scraper thread upon initialization.
     * Add any additional parameter as needed.
     *
     * @param id             The unique identifier for this scraper.
     * @param urlList        The list of URLs to be scraped.
     * @param articleArchive The list for scraped Article objects, shared between scraper threads.
     */
    public CoindeskScraper(String id, List<String> urlList, List<Article> articleArchive) {
        super(id, urlList, articleArchive);
        System.out.println("Coindesk scraper no." + id + " initialized. Thread started upon initialization.");
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
                Element header = doc.select("#article-header > div > header").first();
                String headerClass = header.attr("class");
                String titleString = header.select("h1").text();
                article.setTitle(titleString);

                // SUMMARY
                String summaryString = header.select("h2").text();
                article.setSummary(summaryString);

                // CONTENT
                Elements contents = doc.selectXpath("//section[@class='at-body']//*[self::p or starts-with(name(), 'h')]");
                List<String> contentList = contents.stream()
                        .map(Element::text)
                        .filter(s -> !(s.isEmpty() || s.contains("Edited by")))
                        .toList();
                String contentString = String.join("\n", contentList);
                article.setContent(contentString);

                // GET DATE & AUTHOR
                Element date = null;
                Element author = null;
                switch (headerClass) {
                    case "at-interview-header":
                        date = doc.select("#article-header header div.at-created > div > span").first();
                        author = doc.select("#article-header header div.at-authors > div > div:nth-child(2)").first();
                        break;
                    case "at-opinion-header":
                        date = doc.select("#article-header header span.label > span").first();
                        author = doc.select("#article-header header span.label > a").first();
                        break;
                    default:
                        date = doc.select("#article-header header div.at-created > div > span").first();
                        author = doc.select("#article-header header div.at-authors > span > a").first();
                        break;
                }

                // PARSE DATE
                SimpleDateFormat oldDateFormat = new SimpleDateFormat("MMM dd, yyyy");
                SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = date.text();
                int endIndex = dateString.indexOf(" at");
                if (endIndex != -1) {
                    dateString = dateString.substring(0, endIndex);
                }
                try {
                    String newDateString = newDateFormat.format(oldDateFormat.parse(dateString));
                    article.setDate(newDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // PARSE AUTHOR
                String authorString = author.text().toLowerCase();
                article.setAuthor(authorString);

                // TAGS
                Elements tags = doc.select("[data-module-name='article-tags'] a");
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
//        urlList.add("https://www.coindesk.com/tech/2024/04/11/protocol-village/");
//        urlList.add("https://www.coindesk.com/policy/2023/12/18/belgium-to-push-european-blockchain-network-during-eu-council-presidency-digital-minister-says/");
        urlList.add("https://www.coindesk.com/markets/2023/02/09/the-future-of-financial-planning-lies-in-ai-and-blockchain/");
        CoindeskScraper scraper = new CoindeskScraper(
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
