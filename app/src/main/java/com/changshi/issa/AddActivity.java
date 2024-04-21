package com.changshi.issa;

import static android.app.PendingIntent.getActivity;

import static java.security.AccessController.*;
import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.changshi.issa.DatabaseHandler.Details;
import com.changshi.issa.Adapter.SectionAdapter;
import com.changshi.issa.Adapter.SectionDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.security.AccessController;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    private ImageView mImageView;

    private ImageButton mImageBanner;
    private EditText mEditTitle;
    private EditText mEditDescription;

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private SectionAdapter adapter;
    private RecyclerView SectionsRV;

    private Button mButtonAddDetail;
    private Button mButtonAddSupportSection;
    private EditText mEditConclusion;
    private Button mButtonSubmit;
    private Button mButtonCancel;

    private DatabaseReference mDatabase;
    private int mSection = 1;
    private int mSupportDetail = 1;

    boolean IsEdit = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if(getIntent().hasExtra("IsEditMode"))
            IsEdit = getIntent().getBooleanExtra("IsEditMode", false);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("support_contents");

        mImageView = findViewById(R.id.image_support_banner);
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
                                        pickImageFromGallery();
                                        break;
                                    case 1:
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
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_CODE_PICK_IMAGE);
            }

            private void showUrlInputDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                builder.setTitle("Enter Image URL");

                final EditText input = new EditText(AddActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String imageUrl = input.getText().toString();
                        // Update the image using the URL
                        updateImageFromUrl(imageUrl);
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

            private void updateImageFromUrl(String imageUrl) {
                Picasso.get().load(imageUrl).into(mImageView);
            }


        });

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
                showConfirmationDialog();
            }

            private void showConfirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
            builder.setMessage("Are you sure you want to submit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            // 提交内容
//                            submitContent();
                            // 显示信息已添加成功
                            showSuccessDialog();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        private void showSuccessDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
            builder.setMessage("Information added successfully.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 返回到之前的页面
                            finish();
                        }
                    })
                    .show();


        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create an intent to return to HomeActivity
                Intent intent = new Intent(AddActivity.this, HomeActivity.class);
                // Add an extra indicating the fragment to return to
                intent.putExtra("currentFragment", "AddActivity");
                // Set the result to indicate that the action was canceled
                setResult(Activity.RESULT_CANCELED, intent);
                // Finish AddActivity to return to HomeActivity
                finish();

            }
        });




    /*
    private void submitContent() {

        if(IsEdit)
        {
            // Do Edit Code Here.
            // Find Document in the Database and Update it with the Old Stuff.
        }
        else
        {
            Supports NewItem = new Supports();

        String title = mEditTitle.getText().toString().trim();
        String description = mEditDescription.getText().toString().trim();
        String heading = mEditHeading.getText().toString().trim();
        String conclusion = mEditConclusion.getText().toString().trim();

        NewItem.setTitle(title)

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
    }

     */
    }

    });
    }
}