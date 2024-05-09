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

    String pId, pDepartment,pEmail, pContact, pAddress;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_webpage);

        //actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add Data");
        }

        mDepartmentEt = findViewById(R.id.departmentEt);
        mEmailEt = findViewById(R.id.emailEt);
        mContactEt = findViewById(R.id.contactEt);
        mAddressEt = findViewById(R.id.addressEt);
        mSaveWebInfoBTN = findViewById(R.id.saveWebInfoBTN);
        mCancelWebBTN = findViewById(R.id.cancelWebBTN);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //Update data
            if (actionBar != null) {
                actionBar.setTitle("Update Data");
            }
            mSaveWebInfoBTN.setText("Save");
            //get data
            pId = bundle.getString("pId");
            pDepartment = bundle.getString("pDepartment");
            pEmail = bundle.getString("pEmail");
            pContact = bundle.getString("pContact");
            pAddress = bundle.getString("pAddress");

            //set data
            mDepartmentEt.setText(pDepartment);
            mEmailEt.setText(pEmail);
            mContactEt.setText(pContact);
            mAddressEt.setText(pAddress);
        }else{
            //New data
            if (actionBar != null) {
                actionBar.setTitle("Add Data");
            }
            mSaveWebInfoBTN.setText("Save");
        }


        //progress dialog
        pd = new ProgressDialog(this);

        //firestore
        db = FirebaseFirestore.getInstance();

        //click button to upload data
        mSaveWebInfoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = getIntent().getExtras();

                if (bundle != null){
                    //updating
                    //input data
                    String id = pId;
                    String department = mDepartmentEt.getText().toString().trim();
                    String email = mEmailEt.getText().toString().trim();
                    String contact = mContactEt.getText().toString().trim();
                    String address = mAddressEt.getText().toString().trim();
                    //function call to update data
                    uploadData(id, department, email, contact, address);


                }else {
                    //adding new
                    //input data
                    String department = mDepartmentEt.getText().toString().trim();
                    String email = mEmailEt.getText().toString().trim();
                    String contact = mContactEt.getText().toString().trim();
                    String address = mAddressEt.getText().toString().trim();

                    //function call to upload data
                    uploadData(department, email, contact, address, address);

                }


                //
                Intent intent = new Intent(UpdateWebpageActivity.this, HomeActivity.class);
                intent.putExtra("fragment", "webpage");
                startActivity(intent);
                finish();
            }
        });
        //click cancel go back to webpage
        mCancelWebBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateWebpageActivity.this, HomeActivity.class);
                intent.putExtra("fragment", "webpage");
                startActivity(intent);
                finish();
            }
        });
    }
    private void updateAddingData(String id, String department, String email, String contact, String address) {
        //Set title and show progress bar
        pd.setTitle("Updating Data...");
        pd.show();
        db.collection("Documents").document(id)
                .update("department", department, "email", email, "contact", contact, "address", address)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //this will be called when data is updated successfully
                        pd.dismiss();
                        Toast.makeText(UpdateWebpageActivity.this, "Updated...", Toast.LENGTH_SHORT).show();
//                        return false;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //this will be called if there is any error while updating
                        pd.dismiss();
                        Toast.makeText(UpdateWebpageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void uploadData(String department, String email, String contact, String address, String s)
    {
        //Set title and show progress bar
        pd.setTitle("Adding Data to Firestore");
        pd.show();

        //random id for each data to be stored
        db.collection("Settings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        for (DocumentSnapshot SelectedDocument : task.getResult().getDocuments())
                        {
                            if(SelectedDocument.contains("DocumentsID"))
                            {
                                Long DocumentID = (Long) SelectedDocument.get("DocumentsID");
                                DocumentID++;

                                String id = UUID.randomUUID().toString();

                                Map<String, Object> doc = new HashMap<>();

                                doc.put("id", DocumentID);
                                doc.put("department", department);

                                doc.put("email", email);
                                doc.put("contact", contact);
                                doc.put("address", address);

                                //add this data
                                Long finalDocumentID = DocumentID;
                                db.collection("Documents")
                                        .document(id)
                                        .set(doc)
                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                //this will be called when data is added successfully
                                                pd.dismiss();
                                                Toast.makeText(UpdateWebpageActivity.this, "Uploaded...", Toast.LENGTH_SHORT).show();
                                                // return false;

                                                Map<String, Object> IDData = new HashMap<>();
                                                IDData.put("DocumentsID", finalDocumentID);

                                                db.collection("Settings")
                                                        .document(SelectedDocument.getReference().getId())
                                                        .update(IDData)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                                        {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {

                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener()
                                        {
                                            @Override
                                            public void onFailure(@NonNull Exception e)
                                            {
                                                //this will be called if there is any error while uploading
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