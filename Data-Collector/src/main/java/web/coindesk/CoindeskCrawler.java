package web.coindesk;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import web.template.WebCrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class CoindeskCrawler extends WebCrawler {
    private int startPageNum;
    private int endPageNum;

    /**
     * Constructor for the WebCrawler abstract class.
     * Starts the crawler thread upon initialization.
     * Add any additional parameter as needed.
     *
     * @param id            The unique identifier for this crawler.
     * @param baseUrl       The starting point URL to be crawled.
     * @param urlArchive    The list for found URLs, shared between crawler threads.
     * @param startPageNum  The starting page number to be crawled.
     * @param endPageNum    The ending page number to be crawled.
     */
    public CoindeskCrawler(String id, String baseUrl, List<String> urlArchive, int startPageNum, int endPageNum) {
        super(id, baseUrl, urlArchive);
        this.startPageNum = startPageNum;
        this.endPageNum = endPageNum;
        System.out.println("Coindesk crawler no." + id + " initialized. Thread started upon initialization.");
    }

    @Override
    protected List<String> crawl(String url) {
        HashSet<String> urlSet = new HashSet<>();

        while (startPageNum <= endPageNum) {
            String currentUrl = url + startPageNum + "/";
            try {
                Document doc = Jsoup.connect(currentUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
                        .get();
                Elements linksOnPage = doc.select(".article-card.default .articleTextSection > h6 > a");
                Pattern avoidedPattern = Pattern.compile("/(video|podcasts)/");
                for (Element ele : linksOnPage) {
                    String link = ele.attr("abs:href");
                    if (!avoidedPattern.matcher(link).find()) {
                        urlSet.add(link);
                    }
                }
            } catch (IOException e) {
                System.err.println("For '" + url + "': " + e.getMessage());
            }
            startPageNum++;
        }
        return urlSet.stream().toList();
    }

    public static void main(String[] args) {
        String baseUrl = "https://www.coindesk.com/tag/blockchain/";
        int numOfThread = 2;
        int numOfPage = 5;

        List<String> urlArchive = new ArrayList<>();
        List<CoindeskCrawler> crawlerList = new ArrayList<>();
        for (int id = 0; id < numOfThread; id++) {
            crawlerList.add(
                    new CoindeskCrawler(
                            String.valueOf(id + 1),
                            baseUrl,
                            urlArchive,
                            (id * numOfPage + 1),
                            (id * numOfPage + numOfPage)
                    )
            );
        }
        for (CoindeskCrawler c : crawlerList) {
            try {
                c.getThread().join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(urlArchive.size() + " URLs found.");
    }
}
