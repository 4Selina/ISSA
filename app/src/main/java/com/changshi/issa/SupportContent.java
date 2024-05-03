package com.changshi.issa;

import android.util.EventLogTags;

import com.changshi.issa.DatabaseHandler.Supports;

import java.util.ArrayList;
import java.util.List;

public class SupportContent
{
    private String title;
    private String description;
    private String BannerUrl;
    private int ParentCategory;
    private ArrayList<Supports.Section> AllSections;

    private String Conclusion;

    public SupportContent() {
        // Default constructor required for Firebase
    }

    public SupportContent(String title, String description, int parentCategory, String conclusion)
    {
        this.title = title;
        this.description = description;
        this.ParentCategory = parentCategory;
        this.Conclusion = conclusion;
        this.AllSections = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParentCategory() {
        return ParentCategory;
    }

    public void setParentCategory(int parentCategory) {
        this.ParentCategory = parentCategory;
    }

    public String getConclusion() { return Conclusion; }

    public void setConclusion(String conclusion) {this.Conclusion = conclusion;}
}

