package web.blockchainnews;

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

public class BlockchainnewsCrawler extends WebCrawler {
    /**
     * Constructor for Blockchain News crawler.
     * Starts the crawler thread upon initialization.
     *
     * @param id         The unique identifier for this crawler.
     * @param baseUrl    The starting point URL to be crawled.
     * @param urlArchive The list for found URLs, shared between crawler threads.
     */
    public BlockchainnewsCrawler(String id, String baseUrl, List<String> urlArchive) {
        super(id, baseUrl, urlArchive);
        System.out.println("Blockchain News crawler no." + id + " initialized. Thread started upon initialization.");
    }

    @Override
    protected List<String> crawl(String url) {
        HashSet<String> urlSet = new HashSet<>();

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
                    .get();
            Elements linksOnPage = doc.select("section.position-relative.pt-0 div.col-lg-9 a");
            Pattern pattern = Pattern.compile("/(news)/");
            String link;
            for (Element ele : linksOnPage) {
                link = ele.attr("abs:href").replace("%", "%25");
                if (pattern.matcher(link).find()) {
                    urlSet.add(link);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return urlSet.stream().toList();
    }

    public static void main(String[] args) {
        List<String> urlArchive = new ArrayList<>();
        BlockchainnewsCrawler crawler = new BlockchainnewsCrawler(
                "1",
                "https://blockchain.news/search/blockchain",
                urlArchive);
        try {
            crawler.getThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(urlArchive.size() + " URLs found.");

        for (String url : urlArchive) {
            if (url.contains("%")) {
                System.out.println(url);
            }
        }
    }
}
