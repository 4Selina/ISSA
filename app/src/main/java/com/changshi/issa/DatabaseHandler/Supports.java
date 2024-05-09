package com.changshi.issa.DatabaseHandler;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;

public class Supports implements Serializable
{
    private Long id;

    private String DocumentID;
    private String title;
    private String bannerUrl;
    private String parentCategory;
    private String description;
    private ArrayList<SectionDetails> sections;

    private String conclusion;

    public Supports() {
        // Default constructor required for Firestore
    }

    public Supports(Long id, String title, String bannerUrl, String parentCategory, String description, ArrayList<SectionDetails> sections, String conclusion) {
        this.id = id;
        this.title = title;
        this.bannerUrl = bannerUrl;
        this.parentCategory = parentCategory;
        this.description = description;
        this.sections = sections;
        this.conclusion = conclusion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentID(){return this.DocumentID;}
    public void setDocumentID(String docID){this.DocumentID = docID;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<SectionDetails> getSections() {
        return sections;
    }

    public void setSections(ArrayList<SectionDetails> sections) {
        this.sections = sections;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    // Method to update the supports item
    public void update(Context context, String newTitle, String newBannerUrl, String newParentCategory, String newDescription, ArrayList<SectionDetails> newSections, String newConclusion)
    {

    }

    // Method to delete the support item
    public void delete(Context context)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Deleting all Sections Attached to this Support.
        for(int i = 0; i < sections.size(); i++)
        {
            // Deleting all Details attached to This Section.
            for(int j = 0; j < sections.get(i).getSectionDetails().size(); j++)
            {
                db.collection("Details")
                        .document(sections.get(i).getSectionDetails().get(j).getDocumentID())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {

                            }
                        });
            }

            db.collection("Sections")
                    .document(sections.get(i).DocumentID)
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                        }
                    });
        }

        db.collection("Support_Contents")
                .document(DocumentID)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {

                    }
                });
    }
}
