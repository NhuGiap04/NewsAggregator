package web.template;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Article {
    private String link;
    private String type;
    private String title;
    private String summary;
    private String content;
    private String date;
    private String author;
    private List<String> tagList;

    public String getLink() {
        return link;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getTagList() {
        return tagList;
    }

    /**
     * Sets the link of the article.
     *
     * @param link The link of the article.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Sets the type of the article.
     *
     * @param type The type of the article. Must be one of the following: "news article", "blog post", "tweet".
     * @throws IllegalArgumentException if the type is not one of the valid types.
     */
    public void setType(String type) {
        List<String> typeList = Arrays.asList(
                "news article",
                "blog post",
                "tweet"
        );
        if (typeList.contains(type)) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("Invalid article type.");
        }
    }

    /**
     * Sets the title of the article.
     *
     * @param title The title of the article.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the summary of the article.
     *
     * @param summary The summary of the article. Usually found right below the title.
     *                OPTIONAL. Remember to add fallbacks if not found.
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Sets the content of the article.
     *
     * @param content The content of the article, as one string.
     *                It's advisable that paragraphs should be separated by newline characters.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Sets the date when the article was published.
     *
     * @param date The date when the article was published. Must be in the format "yyyy-mm-dd".
     * @throws IllegalArgumentException if the date does not exist or is not in the correct format.
     */
    public void setDate(String date) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(date, f);
            this.date = date;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date or date format.");
        }
    }

    /**
     * Sets the author of the article.
     *
     * @param author The author of the article.
     *               Should be in lowercase
     */
    public void setAuthor(String author) {
        if (!author.equals(author.toLowerCase())) {
            throw new IllegalArgumentException("Author name should be in lowercase: " + author);
        }
        this.author = author;
    }

    /**
     * Sets the tags associated with the article.
     *
     * @param tagList The tags associated with the article.
     *                Must be in lowercase and not include the hashtag "#" symbol.
     */
    public void setTagList(List<String> tagList) {
        for (String tag : tagList) {
            if (!tag.equals(tag.toLowerCase())) {
                throw new IllegalArgumentException("Tag should be in lowercase: " + tag);
            }
            if (tag.startsWith("#")) {
                throw new IllegalArgumentException("Tag should not include the beginning '#': " + tag);
            }
        }
        this.tagList = tagList;
    }

    public static void main(String[] args) {
        Article article = new Article();
        article.setLink("https://cointelegraph.com/");
        article.setType("news article");
        article.setTitle("Title");
        article.setContent("Content");
        article.setDate("2021-01-01");
        article.setAuthor("author");
        article.setTagList(List.of("tag1", "tag2"));

        List<Article> articleList = new ArrayList<>();
        articleList.add(article);

//        try (FileWriter writer = new FileWriter("cointelegraph-test.json")) {
//            Gson gson = new GsonBuilder()
//                    .setPrettyPrinting()
//                    .create();
//            gson.toJson(articleList, writer);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}

