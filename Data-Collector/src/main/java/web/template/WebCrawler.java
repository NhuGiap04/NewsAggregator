package web.template;

import java.util.List;

public abstract class WebCrawler implements Runnable {
    protected final String id;
    protected final String baseUrl;
    protected final List<String> urlArchive;
    protected final Thread thread;

    /**
     * Constructor for the WebCrawler abstract class.
     * Starts the crawler thread upon initialization.
     * Add any additional parameter as needed.
     *
     * @param id         The unique identifier for this crawler.
     * @param baseUrl    The starting point URL to be crawled.
     * @param urlArchive The list for found URLs, shared between crawler threads.
     */
    public WebCrawler(String id, String baseUrl, List<String> urlArchive) {
        this.id = id;
        this.baseUrl = baseUrl;
        this.urlArchive = urlArchive;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Abstract method to crawl from a base URL.
     *
     * @param url The URL of page to be crawled.
     * @return The list of found URLs.
     */
    protected abstract List<String> crawl(String url);

    @Override
    public void run() {
        urlArchive.addAll(this.crawl(this.baseUrl));
    }

    /**
     * Get the thread in which this crawler is running.
     * As the thread is started upon initialization, use this in tandem with
     * <code>join()</code> to wait for the thread to finish.
     * NOT ALL PAGES WILL BENEFIT FROM MULTI-THREAD CRAWLING.
     *
     * @return The thread in which this crawler is running.
     */
    public Thread getThread() {
        return thread;
    }
}
