package com.changshi.issa;

import java.util.ArrayList;
import java.util.List;

public class SupportContent {
    private String title;
    private String description;
    private String heading;
    private String conclusion;
    private List<String> details;

    public SupportContent() {
        // Default constructor required for Firebase
    }

    public SupportContent(String title, String description, String heading, String conclusion) {
        this.title = title;
        this.description = description;
        this.heading = heading;
        this.conclusion = conclusion;
        this.details = new ArrayList<>();
    }

    public void addDetail(String detail) {
        details.add(detail);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDetails() {
        return details;
    }
}

