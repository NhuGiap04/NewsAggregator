package com.example.aggregator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Article {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");;
    private int id;
    private String title = "Default Article Title";
    private String date = "2024-01-01";
    private String imageSrc = null;
    private String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris gravida justo est, nec sollicitudin mi dignissim at. Curabitur euismod semper neque, at tincidunt risus volutpat ut. Aenean nisi ex, convallis sed ullamcorper sit amet, accumsan id ligula.";
    private String link;
    private String type;
    private String summary;
    private String author;
    private String[] tagList;
    private boolean bookmarked = false;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public LocalDate getLocalDate() {
        return LocalDate.parse(this.getDate(), formatter);
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public boolean getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(Boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public String getType() {
        return type;
    }

    public String getSummary() {
        return summary;
    }

    public String getAuthor() {
        return author;
    }

    public String[] getTagList() {
        return tagList;
    }
}
