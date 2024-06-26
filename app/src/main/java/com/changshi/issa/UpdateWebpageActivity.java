package com.changshi.issa;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpdateWebpageActivity extends AppCompatActivity {

    private EditText mDepartmentEt;
    private EditText mEmailEt;
    private EditText mContactEt;
    private EditText mAddressEt;
    private Button mSaveWebInfoBTN;
    private Button mCancelWebBTN;
    private ProgressDialog pd;
    private FirebaseFirestore db;
    private String DocumentID;

    String pId, pDepartment, pEmail, pContact, pAddress;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_webpage);

        // Initialize UI elements
        mDepartmentEt = findViewById(R.id.departmentEt);
        mEmailEt = findViewById(R.id.emailEt);
        mContactEt = findViewById(R.id.contactEt);
        mAddressEt = findViewById(R.id.addressEt);
        mSaveWebInfoBTN = findViewById(R.id.saveWebInfoBTN);
        mCancelWebBTN = findViewById(R.id.cancelWebBTN);
        pd = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();

        // Get data from previous activity
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Update data
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Update Data");
            }
            mSaveWebInfoBTN.setText("Save");
            pId = bundle.getString("pId");
            DocumentID = bundle.getString("pDocID");
            pDepartment = bundle.getString("pDepartment");
            pEmail = bundle.getString("pEmail");
            pContact = bundle.getString("pContact");
            pAddress = bundle.getString("pAddress");
            // Set data to EditText
            mDepartmentEt.setText(pDepartment);
            mEmailEt.setText(pEmail);
            mContactEt.setText(pContact);
            mAddressEt.setText(pAddress);
        } else {
            // New data
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Add Data");
            }
            mSaveWebInfoBTN.setText("Save");
        }

        // Save button click listener
        mSaveWebInfoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle != null) {
                    // Updating existing data
                    String id = pId;
                    String department = mDepartmentEt.getText().toString().trim();
                    String email = mEmailEt.getText().toString().trim();
                    String contact = mContactEt.getText().toString().trim();
                    String address = mAddressEt.getText().toString().trim();
                    updateAddingData(id, department, email, contact, address);
                } else {
                    // Adding new data
                    String department = mDepartmentEt.getText().toString().trim();
                    String email = mEmailEt.getText().toString().trim();
                    String contact = mContactEt.getText().toString().trim();
                    String address = mAddressEt.getText().toString().trim();
                    uploadData(department, email, contact, address);
                }
                // Navigate back to HomeActivity
                Intent intent = new Intent(UpdateWebpageActivity.this, HomeActivity.class);
                intent.putExtra("fragment", "webpage");
                startActivity(intent);
                finish();
            }
        });

        // Cancel button click listener
        mCancelWebBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to HomeActivity
                Intent intent = new Intent(UpdateWebpageActivity.this, HomeActivity.class);
                intent.putExtra("fragment", "webpage");
                startActivity(intent);
                finish();
            }
        });
    }

    // Method to update existing data
    private void updateAddingData(String id, String department, String email, String contact, String address) {
        pd.setTitle("Updating Data...");
        pd.show();
        db.collection("Documents")
                .document(DocumentID)
                .update("department", department, "email", email, "contact", contact, "address", address)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(UpdateWebpageActivity.this, "Updated...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(UpdateWebpageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to upload new data
    private void uploadData(String department, String email, String contact, String address) {
        pd.setTitle("Adding Data to Firestore");
        pd.show();

        db.collection("Settings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot SelectedDocument : task.getResult().getDocuments()) {
                            if (SelectedDocument.contains("DocumentsID")) {
                                Long DocumentID = (Long) SelectedDocument.get("DocumentsID");
                                DocumentID++;

                                String id = UUID.randomUUID().toString();

                                Map<String, Object> doc = new HashMap<>();
                                doc.put("id", DocumentID);
                                doc.put("department", department);
                                doc.put("email", email);
                                doc.put("contact", contact);
                                doc.put("address", address);

                                Long finalDocumentID = DocumentID;
                                db.collection("Documents")
                                        .document(id)
                                        .set(doc)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                pd.dismiss();
                                                Toast.makeText(UpdateWebpageActivity.this, "Uploaded...", Toast.LENGTH_SHORT).show();

                                                Map<String, Object> IDData = new HashMap<>();
                                                IDData.put("DocumentsID", finalDocumentID);

                                                db.collection("Settings")
                                                        .document(SelectedDocument.getReference().getId())
                                                        .update(IDData)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                pd.dismiss();
                                                Toast.makeText(UpdateWebpageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}
