package com.changshi.issa;

import static com.changshi.issa.Fragment.WebpageFragment.REQUEST_CODE_PICK_IMAGE;

import static java.security.AccessController.*;
import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.Adapter.SectionAdapter;
import com.changshi.issa.Adapter.SectionDetails;
import com.changshi.issa.DatabaseHandler.Details;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.security.AccessController;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {


    private ImageButton mImageBanner;
    private EditText mEditTitle;
    private EditText mEditDescription;

    private SectionAdapter adapter;
    private RecyclerView SectionsRV;

    private Button mButtonAddDetail;
    private Button mButtonAddSupportSection;
    private EditText mEditConclusion;
    private Button mButtonSubmit;
    private Button mButtonCancel;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUrl;

    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        mImageBanner = findViewById(R.id.update_support_banner);
        mEditTitle = findViewById(R.id.edit_support_title);
        mEditDescription = findViewById(R.id.edit_support_intro);
        mButtonAddDetail = findViewById(R.id.button_add_detail);
        mButtonAddSupportSection = findViewById(R.id.button_add_section);
        mEditConclusion = findViewById(R.id.edit_conclusion_content);
        mButtonSubmit = findViewById(R.id.button_save);
        mButtonCancel = findViewById(R.id.button_cancel);

        SectionsRV = findViewById(R.id.recycler_view_sections);

        mImageBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a dialog for picking an image
                showImagePickerDialog();
            }

            private void showImagePickerDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                builder.setTitle("Select Image Source")
                        .setItems(new CharSequence[]{"Gallery", "URL"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        // Pick image from gallery
                                        pickImageFromGallery();
                                        break;
                                    case 1:
                                        // Show dialog to enter URL
                                        showUrlInputDialog();
                                        break;
                                }
                            }


                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }

            private void pickImageFromGallery() {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_CODE_PICK_IMAGE);
            }

            private void showUrlInputDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);

                builder.setTitle("Enter Image URL");

                // Set up the input
                final EditText input = new EditText(AddActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String imageUrl = input.getText().toString();
                        // Update the image using the URL
                        updateImageFromUrl(imageUrl);
                    }

                    private void updateImageFromUrl(String imageUrl)
                    {
                        Picasso.get().load(imageUrl).into(mImageBanner);
                    }
                });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                builder.show();
                }
        });

        //Intent intent = new Intent(AddActivity.this, HomeActivity.class);
        //intent.putExtra("from", "HomeActivity");
        //startActivityForResult(intent, 1);

        ArrayList<SectionDetails> BaseArray = new ArrayList<>();
        ArrayList<Details> DetailsArray = new ArrayList<>();

        Details DetailOne = new Details();
        DetailsArray.add(DetailOne);

        SectionDetails FirstDetail = new SectionDetails();
        FirstDetail.setSectionDetails(DetailsArray);

        BaseArray.add(FirstDetail);

        adapter = new SectionAdapter(BaseArray, this);

        SectionsRV.setLayoutManager(new LinearLayoutManager(this));
        SectionsRV.setAdapter(adapter);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submitContent();

            }
        });


        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

    /*
    private void submitContent() {
        String title = mEditTitle.getText().toString().trim();
        String description = mEditDescription.getText().toString().trim();
        String heading = mEditHeading.getText().toString().trim();
        String conclusion = mEditConclusion.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(heading) || TextUtils.isEmpty(conclusion)) {
            Toast.makeText(this, "Title, Description, Heading, and Conclusion cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to submit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Save content to Firebase database
                        String key = mDatabase.push().getKey();
                        SupportContent supportContent = new SupportContent(title, description, heading, conclusion);
                        for (int i = 0; i < mLayoutSections.getChildCount(); i++) {
                            EditText editTextDetail = (EditText) mLayoutSections.getChildAt(i);
                            String detail = editTextDetail.getText().toString().trim();
                            supportContent.addDetail(detail);
                        }
                        mDatabase.child(key).setValue(supportContent);

                        Toast.makeText(AddActivity.this, "Content submitted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

     */
    }
}

