package com.changshi.issa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.changshi.issa.Adapter.Details;
import com.changshi.issa.Adapter.SectionAdapter;
import com.changshi.issa.Adapter.SectionDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

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

    private DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("support_contents");

        mImageBanner = findViewById(R.id.image_support_banner);
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
                // Handle image banner click
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
                //submitContent();
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

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

