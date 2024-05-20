package web.cryptonews;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import web.template.WebCrawler;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CryptonewsCrawler extends WebCrawler {
    private int numOfLoadMore = -1;
    private LocalDate dateLimit = LocalDate.now().plusDays(1);

    /**
     * Constructor for the Crypto.news crawler class.
     * Starts the crawler thread upon initialization.
     * To make main thread wait for this crawler thread to finish, use <code>.getThread().join()</code>.
     *
     * @param id            The unique identifier for this crawler.
     * @param baseUrl       The starting point URL to be crawled.
     * @param urlArchive    The list for found URLs, shared between scraper threads.
     * @param numOfLoadMore The number of times for "load more" scroll. Each scroll loads 10-30 articles randomly.
     */
    public CryptonewsCrawler(String id, String baseUrl, List<String> urlArchive, int numOfLoadMore) {
        super(id, baseUrl, urlArchive);
        this.numOfLoadMore = numOfLoadMore;
        System.out.println("Crypto.news crawler no." + id + " initialized. Thread started upon initialization.");
    }

    public CryptonewsCrawler(String id, String baseUrl, List<String> urlArchive, String dateLimit) {
        super(id, baseUrl, urlArchive);
        try {
            this.dateLimit = LocalDate.parse(dateLimit, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date or date format.");
        }
        System.out.println("Crypto.news crawler no." + id + " initialized. Thread started upon initialization.");
    }

    protected List<String> crawl(String baseUrl) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--blink-settings=imagesEnabled=false");
        WebDriver driver = new ChromeDriver(options);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        HashSet<String> urlSet = new HashSet<>();

        try {
            driver.get(baseUrl);
            int startIndex = 1;
            WebElement column;
            List<WebElement> rows;
            boolean scrollFailed = false;
            int waitSeconds = 3;
            LocalDate furthestDate;

            do {
                column = driver.findElement(By.cssSelector("#ajax-load-more"));
                rows = column.findElements(By.cssSelector("div.tag-archive__items > div:nth-child(n+" + startIndex + ")"));
                for (WebElement e : rows) {
                    String newUrl = e.findElement(By.cssSelector("div > div > p > a")).getAttribute("href");
                    urlSet.add(newUrl);

                }

                // Scroll to last link, if not working then click button
                WebElement end = rows.getLast();
                String furthestDateString = end.findElement(By.cssSelector("time.post-loop__date"))
                        .getAttribute("datetime")
                        .substring(0, 10);
                furthestDate = LocalDate.parse(furthestDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                js.executeScript("arguments[0].scrollIntoView();", end);
                startIndex += rows.size();
                if (scrollFailed) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("Scroll and click too quickly. Exception: " + e.getMessage());
                    }
                    WebElement loadMoreButton = column.findElement(By.cssSelector("div.alm-btn-wrap > button"));
                    loadMoreButton.click();
                }

                try {
                    Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
                    wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("div.tag-archive__items > div:nth-child(n+" + startIndex + ")")
                    ));
                } catch (TimeoutException te) {
                    if (!scrollFailed) {
                        scrollFailed = true;
                        waitSeconds = 10;
                        System.out.println("Load more by scroll failed. Attempting to click button.");
                        startIndex -= rows.size();
                    } else {
                        System.out.println("Load more by click timeout. Exception: " + te.getMessage());
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Weird exception: " + e.getMessage());
                    break;
                }

                numOfLoadMore--;
            } while (numOfLoadMore >= 0 || dateLimit.isBefore(furthestDate));
        } finally {
            driver.quit();
            System.out.println("Crypto.news article link crawler dismissed.");
        }
        return urlSet.stream().toList();
    }

    public static void main(String[] args) {
        List<String> urlArchive = new ArrayList<>();
        CryptonewsCrawler crawler = new CryptonewsCrawler(
                "1",
                "https://crypto.news/tag/blockchain/",
                urlArchive,
                "2024-03-20");
        try {
            crawler.getThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(urlArchive.size() + " URLs found.");
    }
}
