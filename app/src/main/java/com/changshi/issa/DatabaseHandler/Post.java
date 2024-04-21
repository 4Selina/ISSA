package com.changshi.issa.DatabaseHandler;

public class Post {

    private int id;
    private String title;
    private String bannerUrl;
    private String description;
    private String heading;
    private String details;
    private String category;
    private String conclusion;

    public Post(String title, String description, String bannerUrl, String category, String section, String heading, String details) {
        this.title = title;
        this.bannerUrl = bannerUrl;
        this.category = category;
        this.description = description;
        this.heading = heading;
        this.details = details;
        this.conclusion = conclusion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public String getCategory() {
        return category;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }
}
