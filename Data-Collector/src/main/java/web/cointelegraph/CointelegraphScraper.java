package web.cointelegraph;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.reflect.TypeToken;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import web.template.Article;
import web.template.WebScraper;

/**
 * Class CointelegraphScraper that extends WebScraper (which implements Runnable),
 * designed to scrape articles from <a href="https://cointelegraph.com/">cointelegraph.com</a>
 */

public class CointelegraphScraper extends WebScraper {
    public CointelegraphScraper(String id, List<String> urlList, List<Article> articleArchive) {
        super(id, urlList, articleArchive);
        System.out.println("Cointelegraph scraper no." + id + " initialized. Thread started upon initialization.");
    }

    @Override
    protected List<Article> scrape(List<String> urlList) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--blink-settings=imagesEnabled=false");
        options.addArguments("--disable-javascript");
        WebDriver driver = new ChromeDriver(options);

        List<Article> articleList = new ArrayList<>();
        int dodged = 0;
        
        try {
            for (String url : urlList) {
                try {
                    driver.get(url);
                    WebElement element = driver.findElement(By.cssSelector("[data-gtm-locator='a1']"));
                    Article article = new Article();
                    article.setLink(url);
                    article.setType("news article");

                    // TITLE
                    WebElement title = element.findElement(By.cssSelector("[class='post__title']"));
                    String titleString = title.getText();
                    article.setTitle(titleString);

                    // SUMMARY
                    WebElement summary = element.findElement(By.cssSelector("[class='post__lead']"));
                    String summaryString = summary.getText();
                    article.setSummary(summaryString);

                    // CONTENT (remove live price)
                    List<WebElement> contents = element.findElements(
                            By.xpath("//div[@class='post-content relative']/*[self::p or starts-with(name(), 'h')]"));

                    List<String> contentList = new ArrayList<>(contents.stream()
                            .map(WebElement::getText)
                            .filter(s -> !(s.isEmpty()))
                            .toList());

                    Pattern pattern = Pattern.compile("\\n[A-Z]+\\n\\$\\d{1,3}(,\\d{3})*(\\.\\d{2})?\\n");
                    for (int i = 0; i < contentList.size(); i++) {
                        Matcher matcher = pattern.matcher(contentList.get(i));
                        if (matcher.find()) {
                            contentList.set(i, matcher.replaceAll(" "));
                        }
                    }

                    String contentString = String.join("\n", contentList);
                    article.setContent(contentString);

                    // CREATION DATE
                    WebElement date = element.findElement(By.cssSelector("[class='post-meta__publish-date'] > time"));
                    String dateString = date.getAttribute("datetime");
                    article.setDate(dateString);

                    // AUTHOR
                    WebElement author = element.findElement(By.cssSelector("[class='post-meta__author']"));
                    String authorString = author.getText().toLowerCase();
                    article.setAuthor(authorString);

                    // TAGS
                    List<WebElement> tags = element.findElements(By.cssSelector("ul.tags-list__list > li"));
                    List<String> tagList = tags.stream()
                            .map(e -> e.getText().substring(1).toLowerCase())
                            .toList();
                    article.setTagList(tagList);

                    articleList.add(article);
                } catch (Exception e) {
                    System.out.println("Unable to recognize structure of " + url);
                    dodged++;
                }
            }
        } finally {
            driver.quit();
            System.out.println("Scraper no." + id + " scanned " + (urlList.size() - dodged) + " articles, unrecognized " + dodged + " articles. Dismissed.");
        }
        return articleList;
    }

    public static void main(String[] args) {
        List<String> urlList = new ArrayList<>();

//        urlList.add("https://cointelegraph.com/news/trader-gains-6-million-memecoin-frenzy-ethereum");
        urlList.add("https://cointelegraph.com/news/ether-hiding-why-hackers-prefer-binance-bnb-smart-chain");

        List<Article> articleList = new ArrayList<>();

        CointelegraphScraper scraper = new CointelegraphScraper("1", urlList, articleList);
        try {
            scraper.getThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(articleList);
        System.out.println(json);

        Type articleListType = new TypeToken<List<Article>>(){}.getType();
        List<Article> revertedList = gson.fromJson(json, articleListType);
        System.out.println(revertedList.getFirst().getContent());
    }
}

