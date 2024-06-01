package com.changshi.issa.DatabaseHandler;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Supports implements Serializable {

    private Long id;
    private String DocumentID;
    private String title;
    private String bannerUrl;
    private String parentCategory;
    private String description;
    private ArrayList<SectionDetails> sections;
    private String conclusion;

    // Default constructor required for Firestore
    public Supports() { }

    // Parameterized constructor to initialize the object
    public Supports(Long id, String title, String bannerUrl, String parentCategory, String description, ArrayList<SectionDetails> sections, String conclusion) {
        this.id = id;
        this.title = title;
        this.bannerUrl = bannerUrl;
        this.parentCategory = parentCategory;
        this.description = description;
        this.sections = sections;
        this.conclusion = conclusion;
    }

    // Getter and setter methods for the fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentID() {
        return DocumentID;
    }

    public void setDocumentID(String docID) {
        this.DocumentID = docID;
    }

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

    // Method to update the Supports item
    public void update(Context context, String newTitle, String newBannerUrl, String newParentCategory, String newDescription, ArrayList<SectionDetails> newSections, String newConclusion) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Update the properties of this Supports object
        this.title = newTitle;
        this.bannerUrl = newBannerUrl;
        this.parentCategory = newParentCategory;
        this.description = newDescription;
        this.sections = newSections;
        this.conclusion = newConclusion;

        // Create a new map with the updated properties
        Map<String, Object> supportsMap = new HashMap<>();
        supportsMap.put("title", this.title);
        supportsMap.put("bannerUrl", this.bannerUrl);
        supportsMap.put("parentCategory", this.parentCategory);
        supportsMap.put("description", this.description);
        supportsMap.put("conclusion", this.conclusion);

        // Update the document in the Firestore database
        db.collection("Support_Contents")
                .document(this.DocumentID)
                .set(supportsMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Notify the user that the supports item was updated successfully
                            Toast.makeText(context, "Supports item updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Notify the user that there was an error updating the supports item
                            Toast.makeText(context, "Error updating supports item", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Update each Section and its Details
        for (SectionDetails section : this.sections) {
            Map<String, Object> sectionMap = new HashMap<>();
            sectionMap.put("id", section.getID());  // Assuming SectionDetails has a getID method
            sectionMap.put("heading", section.getSectionHeading());  // Assuming SectionDetails has a getSectionHeading method
            sectionMap.put("details", section.getSectionDetails());  // Assuming SectionDetails has a getSectionDetails method

            db.collection("Sections")
                    .document(section.getDocumentID())  // Assuming SectionDetails has a getDocumentID method
                    .set(sectionMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Notify the user that the section was updated successfully
                                Toast.makeText(context, "Section updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                // Notify the user that there was an error updating the section
                                Toast.makeText(context, "Error updating section", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            for (Details detail : section.getSectionDetails()) {
                Map<String, Object> detailMap = new HashMap<>();
                detailMap.put("id", detail.getID());  // Assuming Details has a getID method
                detailMap.put("detail", detail.getDetail());  // Assuming Details has a getDetail method
                detailMap.put("link", detail.getLink());  // Assuming Details has a getLink method

                db.collection("Details")
                        .document(detail.getDocumentID())  // Assuming Details has a getDocumentID method
                        .set(detailMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Notify the user that the detail was updated successfully
                                    Toast.makeText(context, "Detail updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Notify the user that there was an error updating the detail
                                    Toast.makeText(context, "Error updating detail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }

    // Method to delete the support item
    public void delete(Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Deleting all Sections Attached to this Support
        for (SectionDetails section : sections) {
            // Deleting all Details attached to This Section
            for (Details detail : section.getSectionDetails()) {
                db.collection("Details")
                        .document(detail.getDocumentID())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Handle completion of detail deletion
                            }
                        });
            }

            db.collection("Sections")
                    .document(section.getDocumentID())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Handle completion of section deletion
                        }
                    });
        }

        // Deleting the support content
        db.collection("Support_Contents")
                .document(DocumentID)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Handle completion of support content deletion
                    }
                });
    }
}
