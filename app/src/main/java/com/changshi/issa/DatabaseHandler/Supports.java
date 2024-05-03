package com.changshi.issa.DatabaseHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import static android.content.ContentValues.TAG;

public class Supports {
    private String id;
    private String title;
    private String bannerUrl;
    private String parentCategory;
    private String description;
    private List<Section> sections;

    private String conclusion;

    public Supports() {
        // Default constructor required for Firestore
    }

    public Supports(String id, String title, String bannerUrl, String parentCategory, String description, List<Section> sections, String conclusion) {
        this.id = id;
        this.title = title;
        this.bannerUrl = bannerUrl;
        this.parentCategory = parentCategory;
        this.description = description;
        this.sections = sections;
        this.conclusion = conclusion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    // Method to create a new support item
    public void create(Context context, String title, String bannerUrl, String parentCategory, String description, List<Section> sections, String conclusion) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newSupportRef = db.collection("supports").document();
        Supports newSupport = new Supports(newSupportRef.getId(), title, bannerUrl, parentCategory, description, sections, conclusion);

        newSupportRef.set(newSupport)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Show a dialog to inform the user that the item was created successfully
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Support item created successfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Close the dialog
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error creating support item", e);
                    }
                });
    }

    // Method to update the supports item
    public void update(Context context, String newTitle, String newBannerUrl, String newParentCategory, String newDescription, List<Section> newSections, String newConclusion) {

        if (newSections.isEmpty()) {
            // 用户尝试删除所有的 section，可以在这里添加逻辑来提醒用户或者执行其他操作
            // 这里仅作为示例，你可以根据需求进行更改
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("At least one section must be present.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Close the dialog
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
            return;
        }

        // 检查标题和分类部分是否为空
        if (newTitle.isEmpty() || newParentCategory.isEmpty()) {
            // 提示用户标题和分类部分必须填写信息
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Title and category must be filled.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Close the dialog
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
            return;
        }
        this.title = newTitle;
        this.bannerUrl = newBannerUrl;
        this.parentCategory = newParentCategory;
        this.description = newDescription;
        this.sections = newSections;
        this.conclusion = newConclusion;

        // Implement logic to update the support item in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("supports").document(id).update(
                "title", newTitle,
                "bannerUrl", newBannerUrl,
                "parentCategory", newParentCategory,
                "description", newDescription,
                "sections", newSections,
                "conclusion", newConclusion
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Show a dialog to inform the user that the item was updated successfully
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Support item updated successfully")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Additional actions here if needed
                            }
                        });
                builder.create().show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating support item", e);
            }
        });
    }

    // Method to delete the support item
    public void delete(Context context) {
        // Show a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete this support item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Yes button, delete the support item
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("supports").document(String.valueOf(id)).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Support item deleted successfully");

                                        // Show a dialog to inform the user that the item was deleted
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage("Support item deleted successfully")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // You can perform additional actions here if needed
                                                    }
                                                });
                                        builder.create().show();

                                        // Implement logic to update the UI after deleting the support item
                                        // For example, remove the item from the RecyclerView in the Subfunction Fragment
                                        // and update the Support Contents Activity
                                        // Note: This part depends on how your UI is structured and updated
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting support item", e);
                                    }
                                });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked No button, do nothing
                    }
                });
        builder.create().show();
    }

    public static class Section {
        private String id;
        private String heading;
        private List<String> details;

        public Section() {
            // Default constructor required for Firestore
        }

        public Section(String id, String heading, List<String> details) {
            this.id = id;
            this.heading = heading;
            this.details = details;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHeading() {
            return heading;
        }

        public void setHeading(String heading) {
            this.heading = heading;
        }

        public List<String> getDetails() {
            return details;
        }

        public void setDetails(List<String> details) {
            this.details = details;
        }
    }
}
