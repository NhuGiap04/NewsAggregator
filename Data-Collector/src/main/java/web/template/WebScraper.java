package web.template;

import java.util.List;

/**
 * Abstract class WebScraper that implements Runnable,
 * designed to be a base for specific web scrapers.
 */
public abstract class WebScraper implements Runnable {
    protected final String id;
    protected final List<String> urlList;
    protected final List<Article> articleArchive;
    protected final Thread thread;

    /**
     * Constructor for the WebScraper abstract class.
     * Starts the scraper thread upon initialization.
     * Add any additional parameter as needed.
     *
     * @param id             The unique identifier for this scraper.
     * @param urlList        The list of URLs to be scraped.
     * @param articleArchive The list for scraped Article objects, shared between scraper threads.
     */
    public WebScraper(String id, List<String> urlList, List<Article> articleArchive) {
        this.id = id;
        this.urlList = urlList;
        this.articleArchive = articleArchive;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Abstract method to scrape the given list of URLs.
     * List of what to scrape includes link, article type, title, summary, content, date, author, and tags.
     *
     * @param urlList The list of URLs to be scraped.
     * @return The list of Article objects scraped from the URLs.
     */
    protected abstract List<Article> scrape(List<String> urlList);

    @Override
    public void run() {
        articleArchive.addAll(this.scrape(this.urlList));
    }

    /**
     * Get the thread in which this scraper is running.
     * As the thread is started upon initialization, use this in tandem with
     * <code>join()</code> to wait for the thread to finish.
     *
     * @return The thread in which this scraper is running.
     */
    public Thread getThread() {
        return thread;
    }
}
