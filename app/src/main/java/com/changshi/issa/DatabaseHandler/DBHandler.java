package com.changshi.issa.DatabaseHandler;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class DBHandler {

    private FirebaseFirestore db;
    private CollectionReference supportsRef;

    public DBHandler(Context context) {
        db = FirebaseFirestore.getInstance();
        supportsRef = db.collection("supports");
    }

    public void addSupport(String title, String bannerUrl, String parentCategory, String description,
                           Map<String, Object> sections, String conclusion) {
        Map<String, Object> support = new HashMap<>();
        support.put("title", title);
        support.put("bannerUrl", bannerUrl);
        support.put("parentCategory", parentCategory);
        support.put("description", description);
        support.put("sections", sections);
        support.put("conclusion", conclusion);

        supportsRef.add(support)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("FirestoreHandler", "Support added with ID: " + documentReference.getId());
                        // Handle success, if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FirestoreHandler", "Error adding support", e);
                        // Handle failure, if needed
                    }
                });
    }

    public void updateSupport(String supportId, String title, String bannerUrl, String parentCategory,
                              String description, Map<String, Object> sections, String conclusion) {
        DocumentReference supportRef = supportsRef.document(supportId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", title);
        updates.put("bannerUrl", bannerUrl);
        updates.put("parentCategory", parentCategory);
        updates.put("description", description);
        updates.put("sections", sections);
        updates.put("conclusion", conclusion);

        supportRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FirestoreHandler", "Support updated successfully");
                        // Handle success, if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FirestoreHandler", "Error updating support", e);
                        // Handle failure, if needed
                    }
                });
    }

    public void deleteSupport(String supportId) {
        supportsRef.document(supportId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FirestoreHandler", "Support deleted successfully");
                        // Handle success, if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FirestoreHandler", "Error deleting support", e);
                        // Handle failure, if needed
                    }
                });
    }
}
