package com.changshi.issa.DatabaseHandler;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHandler {

    private FirebaseFirestore db;
    private CollectionReference supportsRef;

    public DBHandler(Context context) {
        db = FirebaseFirestore.getInstance();
        supportsRef = db.collection("support_Contents");
    }

    public interface OnImageUploadListener {
        void onImageUpload(String imageUrl);

        void onError(Exception exception);
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

    public void addSection(String supportId, String heading, List<String> details) {
        DocumentReference supportRef = supportsRef.document(supportId);
        supportRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> sections = (Map<String, Object>) documentSnapshot.get("sections");
                sections.put(heading, details);
                supportRef.update("sections", sections)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("FirestoreHandler", "Section added successfully");
                                // Handle success, if needed
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("FirestoreHandler", "Error adding section", e);
                                // Handle failure, if needed
                            }
                        });
            }
        });
    }

    public void deleteSection(String supportId, String heading) {
        DocumentReference supportRef = supportsRef.document(supportId);
        supportRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> sections = (Map<String, Object>) documentSnapshot.get("sections");
                sections.remove(heading);
                supportRef.update("sections", sections)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("FirestoreHandler", "Section deleted successfully");
                                // Handle success, if needed
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("FirestoreHandler", "Error deleting section", e);
                                // Handle failure, if needed
                            }
                        });
            }
        });
    }

    public void addDetail(String supportId, String heading, String detail) {
        DocumentReference supportRef = supportsRef.document(supportId);
        supportRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> sections = (Map<String, Object>) documentSnapshot.get("sections");
                List<String> details = (List<String>) sections.get(heading);
                details.add(detail);
                sections.put(heading, details);
                supportRef.update("sections", sections)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("FirestoreHandler", "Detail added successfully");
                                // Handle success, if needed
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("FirestoreHandler", "Error adding detail", e);
                                // Handle failure, if needed
                            }
                        });
            }
        });
    }

    public void deleteDetail(String supportId, String heading, String detail) {
        DocumentReference supportRef = supportsRef.document(supportId);
        supportRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> sections = (Map<String, Object>) documentSnapshot.get("sections");
                List<String> details = (List<String>) sections.get(heading);
                details.remove(detail);
                sections.put(heading, details);
                supportRef.update("sections", sections)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("FirestoreHandler", "Detail deleted successfully");
                                // Handle success, if needed
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("FirestoreHandler", "Error deleting detail", e);
                                // Handle failure, if needed
                            }
                        });
            }
        });
    }
}
