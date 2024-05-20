package web.cointelegraph;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import web.template.WebCrawler;


public class CointelegraphCrawler extends WebCrawler {
    private int numOfLoadMore = -1;
    private LocalDate dateLimit = LocalDate.now().plusDays(1);

    /**
     * Constructor for the CointelegraphCrawler class.
     * Starts the crawler thread upon initialization.
     * To make main thread wait for this crawler thread to finish, use <code>.getThread().join()</code>.
     *
     * @param id            The unique identifier for this crawler.
     * @param baseUrl       The starting point URL to be crawled.
     * @param urlArchive    The list for found URLs, shared between scraper threads.
     * @param numOfLoadMore The number of times for "load more" scroll. Each scroll loads 15 URLs.
     */
    public CointelegraphCrawler(String id, String baseUrl, List<String> urlArchive, int numOfLoadMore) {
        super(id, baseUrl, urlArchive);
        this.numOfLoadMore = numOfLoadMore;
        System.out.println("Cointelegraph crawler no." + id + " initialized. Thread started upon initialization.");
    }

    public CointelegraphCrawler(String id, String baseUrl, List<String> urlArchive, String dateLimit) {
        super(id, baseUrl, urlArchive);
        try {
            this.dateLimit = LocalDate.parse(dateLimit, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date or date format.");
        }
        System.out.println("Cointelegraph crawler no." + id + " initialized. Thread started upon initialization.");
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
            LocalDate furthestDate;

            do {
                column = driver.findElement(By.cssSelector(".tag-page__posts-col"));
                rows = column.findElements(By.cssSelector("li:nth-child(n+" + startIndex + ")"));
                for (WebElement e : rows) {
                    String newUrl = e.findElement(By.cssSelector(".post-card-inline__title-link")).getAttribute("href");
                    urlSet.add(newUrl);
                }

                WebElement end = rows.getLast();
                String furthestDateString = end.findElement(By.cssSelector(".post-card-inline__date")).getAttribute("datetime");
                furthestDate = LocalDate.parse(furthestDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                js.executeScript("arguments[0].scrollIntoView();", end);
                startIndex += rows.size();

                try {
                    Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector(".tag-page__posts-col li:nth-child(" + startIndex + ")")
                    ));
                } catch (TimeoutException e) {
                    System.out.println("Went wrong: " + e.getMessage());
                    break;
                }
                numOfLoadMore--;
            } while (numOfLoadMore >= 0 || dateLimit.isBefore(furthestDate));
        } finally {
            driver.quit();
            System.out.println("Cointelegraph article link crawler dismissed.");
        }
        return urlSet.stream().toList();
    }

    public static void main(String[] args) {
        List<String> urlArchive = new ArrayList<>();
//        CointelegraphCrawler crawler = new CointelegraphCrawler(
//                "1",
//                "https://cointelegraph.com/tags/blockchain",
//                urlArchive,
//                0);
        CointelegraphCrawler crawler = new CointelegraphCrawler(
                "1",
                "https://cointelegraph.com/tags/blockchain",
                urlArchive,
                "2024-05-04");
        try {
            crawler.getThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(urlArchive.size() + " URLs found.");
    }
}
