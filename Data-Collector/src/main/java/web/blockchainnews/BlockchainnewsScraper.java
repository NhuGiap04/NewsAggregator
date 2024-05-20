package web.blockchainnews;

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
//import java.util.Date;
import java.util.List;

public class BlockchainnewsScraper extends WebScraper {
    /**
     * Constructor for the WebScraper abstract class.
     * Starts the scraper thread upon initialization.
     * Add any additional parameter as needed.
     *
     * @param id             The unique identifier for this scraper.
     * @param urlList        The list of URLs to be scraped.
     * @param articleArchive The list for scraped Article objects, shared between scraper threads.
     */
    public BlockchainnewsScraper(String id, List<String> urlList, List<Article> articleArchive) {
        super(id, urlList, articleArchive);
        System.out.println("Blockchain News scraper no." + id + " initialized. Thread started upon initialization.");
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
                Element title = doc.select("section.pb-3.pb-lg-5 h1").first();
                String titleString = title.text();
                article.setTitle(titleString);

                // SUMMARY
                Element summary = doc.select("section.pb-3.pb-lg-5 p.lead").first();
                String summaryString = summary.text();
                article.setSummary(summaryString);

                // CONTENT
                Elements contents = doc.select("section.pt-0 div.col-lg-7.mb-5 > p");
                List<String> contentList = contents.stream()
                        .map(Element::text)
                        .filter(s -> !s.isEmpty())
                        .toList();
                String contentString = String.join("\n", contentList);
                article.setContent(contentString);

                // DATE
                Element date = doc.select("section.pt-0 div.col-lg-2 li").first();
                SimpleDateFormat oldDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    String newDateString = newDateFormat.format(oldDateFormat.parse(date.text()));
                    article.setDate(newDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // AUTHOR
                Element author = doc.select("section.pt-0 div.col-lg-2 a").first();
                String authorString = author.text().toLowerCase();
                article.setAuthor(authorString);

                // TAGS
                Elements tags = doc.select("section.pt-0 div.col-lg-7.mb-5 > h4 ~ ul li");
                List<String> tagList = tags.stream()
                        .map(Element::text)
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
        urlList.add("https://blockchain.news/news/ant-group-ipo-delay-bigger-concern-for-asia-tech-than-us-election");
        BlockchainnewsScraper scraper = new BlockchainnewsScraper(
                "1",
                urlList,
                articleArchive);
        try {
            scraper.getThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(articleArchive);
        System.out.println(json);
    }

    public static BlockchainnewsScraper create(String id, List<String> urlList, List<Article> articleArchive) {
        return null;
    }
}
