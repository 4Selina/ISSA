package com.changshi.issa;

import static com.changshi.issa.Fragment.WebpageFragment.REQUEST_CODE_PICK_IMAGE;

import android.annotation.SuppressLint;
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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.Adapter.SectionAdapter;
import com.changshi.issa.DatabaseHandler.Details;
import com.changshi.issa.DatabaseHandler.SectionDetails;
import com.changshi.issa.DatabaseHandler.Supports;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class  AddActivity extends AppCompatActivity
{
    private ImageView mBannerImg;
    private ImageButton mImageBannerBtn;
    private EditText mEditTitle;

    private EditText mEditDescription;

    private Spinner category;

    private SectionAdapter adapter;
    private RecyclerView SectionsRV;
    private ArrayList<SectionDetails> sectionDetails;
    //    private Button mButtonAddDetail;
//    private Button mButtonAddSupportSection;
    private EditText mEditConclusion;
    private Button mButtonSubmit;
    private Button mButtonCancel;

    private String bannerUrl;

    private boolean IsEditMode;
    private Supports SelectedSupport;

//    private static final int PICK_IMAGE_REQUEST = 1;
//    private Uri mImageUrl;

    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        Intent RecievedIntent = getIntent();

        if(RecievedIntent.hasExtra("IsEditMode"))
        {
            IsEditMode = RecievedIntent.getBooleanExtra("IsEditMode", true);

            if(IsEditMode)
            {
                SelectedSupport = (Supports)RecievedIntent.getSerializableExtra("SelectedSupport");
            }
        }

        mBannerImg = findViewById(R.id.imageView_support_banner);
        mImageBannerBtn = findViewById(R.id.update_support_banner);
        mEditTitle = findViewById(R.id.edit_support_title);

//        heading = findViewById(R.id.edit_heading);
//        details = findViewById(R.id.edit_detail_text);

        mEditDescription = findViewById(R.id.edit_support_intro);
//        mButtonAddDetail = findViewById(R.id.button_add_detail);
//        mButtonAddSupportSection = findViewById(R.id.button_add_section);
        mEditConclusion = findViewById(R.id.edit_conclusion_content);
        mButtonSubmit = findViewById(R.id.button_save);
        mButtonCancel = findViewById(R.id.button_cancel);

        category = findViewById(R.id.spinner_function);

        SectionsRV = findViewById(R.id.recycler_view_sections);

        mImageBannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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

            private void pickImageFromGallery()
            {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_CODE_PICK_IMAGE);
            }

            private void showUrlInputDialog()
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);

                builder.setTitle("Enter Image URL");

                // Set up the input
                final EditText input = new EditText(AddActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        bannerUrl = input.getText().toString();
                        Picasso.get().load(bannerUrl).into(mBannerImg);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        //init and set Section recyclerView
        if(!IsEditMode)
        {
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
        }

        mButtonSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean AddItem = true;
                String FailedReason = "";

                if(!IsEditMode)
                {
                    Supports NewSupport = new Supports();

                    //input data
                    NewSupport.setTitle(mEditTitle.getText().toString().trim());

                    if(!Strings.isNullOrEmpty(bannerUrl))
                    {
                        NewSupport.setBannerUrl(bannerUrl);
                    }

                    NewSupport.setDescription(mEditDescription.getText().toString().trim());
                    NewSupport.setParentCategory(category.getSelectedItem().toString());

                    ArrayList<SectionDetails> AllSections = new ArrayList<>();

                    if(SectionsRV.getAdapter().getItemCount() > 0)
                    {
                        for(int i = 0; i < SectionsRV.getAdapter().getItemCount(); i++)
                        {
                            SectionDetails NewSection = new SectionDetails();

                            if(Strings.isNullOrEmpty(((SectionAdapter)SectionsRV.getAdapter()).getItem(i).getSectionHeading()))
                            {
                                AddItem = false;
                                FailedReason = "A Section Heading is Empty.";
                            }

                            NewSection.setSectionHeading(((SectionAdapter)SectionsRV.getAdapter()).getItem(i).getSectionHeading());
                            NewSection.setSectionDetails(((SectionAdapter)SectionsRV.getAdapter()).getItem(i).getSectionDetails());

                            for(Details SelectedDetails : ((SectionAdapter)SectionsRV.getAdapter()).getItem(i).getSectionDetails())
                            {
                                if(Strings.isNullOrEmpty(SelectedDetails.getDetail()))
                                {
                                    AddItem = false;
                                    FailedReason = "A Detail is Empty.";
                                }

                                if(!Strings.isNullOrEmpty(SelectedDetails.getLink()))
                                {
                                    if(!SelectedDetails.getLink().contains("http") || !SelectedDetails.getLink().contains("://"))
                                    {
                                        AddItem = false;
                                        FailedReason = "Link not in Correct Format.";
                                    }
                                }
                            }

                            AllSections.add(NewSection);
                        }
                    }

                    NewSupport.setSections(AllSections);
                    NewSupport.setConclusion(mEditConclusion.getText().toString().trim());

                    if(AddItem)
                        submitContent(NewSupport);
                    else
                        Toast.makeText(AddActivity.this, FailedReason, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SelectedSupport.setTitle(mEditTitle.getText().toString().trim());

                    if(!Strings.isNullOrEmpty(bannerUrl))
                    {
                        SelectedSupport.setBannerUrl(bannerUrl);
                    }

                    SelectedSupport.setDescription(mEditDescription.getText().toString().trim());
                    SelectedSupport.setParentCategory(category.getSelectedItem().toString());

                    ArrayList<SectionDetails> OldSections = SelectedSupport.getSections();
                    ArrayList<SectionDetails> AllSections = new ArrayList<>();

                    if(SectionsRV.getAdapter().getItemCount() > 0)
                    {
                        for(int i = 0; i < SectionsRV.getAdapter().getItemCount(); i++)
                        {
                            SectionDetails NewSection = new SectionDetails();

                            if(Strings.isNullOrEmpty(((SectionAdapter)SectionsRV.getAdapter()).getItem(i).getSectionHeading()))
                            {
                                AddItem = false;
                                FailedReason = "A Section Heading is Empty.";
                            }

                            NewSection.setSectionHeading(((SectionAdapter)SectionsRV.getAdapter()).getItem(i).getSectionHeading());
                            NewSection.setSectionDetails(((SectionAdapter)SectionsRV.getAdapter()).getItem(i).getSectionDetails());

                            for(Details SelectedDetails : ((SectionAdapter)SectionsRV.getAdapter()).getItem(i).getSectionDetails())
                            {
                                if(Strings.isNullOrEmpty(SelectedDetails.getDetail()))
                                {
                                    AddItem = false;
                                    FailedReason = "A Detail is Empty.";
                                }

                                if(!Strings.isNullOrEmpty(SelectedDetails.getLink()))
                                {
                                    if(!SelectedDetails.getLink().contains("http") || !SelectedDetails.getLink().contains("://") || !SelectedDetails.getLink().contains("www."))
                                    {
                                        AddItem = false;
                                        FailedReason = "Link not in Correct Format.";
                                    }
                                }
                            }

                            AllSections.add(NewSection);
                        }
                    }

                    SelectedSupport.setSections(AllSections);
                    SelectedSupport.setConclusion(mEditConclusion.getText().toString().trim());

                    if(AddItem)
                        editContent(SelectedSupport, OldSections);
                    else
                        Toast.makeText(AddActivity.this, FailedReason, Toast.LENGTH_SHORT).show();
                }
            }
        });


        mButtonCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Open URL in Browser.
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link.getText().toString()));
                //startActivity(browserIntent);

                finish();
            }
        });

        // retrieve data
        if(IsEditMode)
        {
            showData();
        }
    }

    private void showData()
    {
        mEditTitle.setText(SelectedSupport.getTitle());

        if(!Strings.isNullOrEmpty(SelectedSupport.getBannerUrl()))
        {
            Picasso.get().load(SelectedSupport.getBannerUrl()).into(mBannerImg);
        }

        mEditDescription.setText(SelectedSupport.getDescription());

        String ParentCategory = SelectedSupport.getParentCategory();
        int CategoryID = 0;

        if(ParentCategory.equals("Learning Support"))
            CategoryID = 0;
        else if(ParentCategory.equals("Social Activities"))
            CategoryID = 1;
        else if(ParentCategory.equals("Accommodations"))
            CategoryID = 2;
        else if(ParentCategory.equals("Transport"))
            CategoryID = 3;
        else if(ParentCategory.equals("Job Supports"))
            CategoryID = 4;

        category.setSelection(CategoryID);

        adapter = new SectionAdapter(SelectedSupport.getSections(), this);

        SectionsRV.setLayoutManager(new LinearLayoutManager(this));
        SectionsRV.setAdapter(adapter);

        mEditConclusion.setText(SelectedSupport.getConclusion());
    }

    private void submitContent(Supports SavedItem)
    {
        db.collection("Settings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        // Create a new document data
                        Map<String, Object> SupportData = new HashMap<>();
                        Map<String, Object> SectionsData = new HashMap<>();
                        Map<String, Object> DetailsData = new HashMap<>();

                        String SupportDocumentID = UUID.randomUUID().toString();

                        Long SupportID = (Long) task.getResult().getDocuments().get(0).get("SupportID");
                        Long SectionsID = (Long) task.getResult().getDocuments().get(0).get("SectionsID");
                        Long DetailsID = (Long) task.getResult().getDocuments().get(0).get("DetailsID");

                        SupportID++;

                        SupportData.put("id", SupportID);

                        if(!Strings.isNullOrEmpty(SavedItem.getBannerUrl()))
                        {
                            SupportData.put("bannerUrl", SavedItem.getBannerUrl());
                        }

                        SupportData.put("title", SavedItem.getTitle());
                        SupportData.put("description", SavedItem.getDescription());
                        SupportData.put("parentCategory", SavedItem.getParentCategory());
                        SupportData.put("conclusion", SavedItem.getConclusion());

                        if(SavedItem.getSections().size() > 0)
                        {
                            ArrayList<Long> SectionsIDs = new ArrayList<>();

                            for (int i = 0; i < SavedItem.getSections().size(); i++)
                            {
                                String SectionsDocumentID = UUID.randomUUID().toString();

                                SectionsID++;

                                SectionsData.put("id", SectionsID);
                                SectionsIDs.add(SectionsID);

                                SectionsData.put("heading", SavedItem.getSections().get(i).getSectionHeading());

                                if(SavedItem.getSections().get(i).getSectionDetails() != null)
                                {
                                    if(SavedItem.getSections().get(i).getSectionDetails().size() > 0)
                                    {
                                        ArrayList<Long> DetailsList = new ArrayList<>();

                                        for (int j = 0; j < SavedItem.getSections().get(i).getSectionDetails().size(); j++)
                                        {
                                            String DetailsDocumentID = UUID.randomUUID().toString();

                                            DetailsID++;

                                            DetailsData.put("id", DetailsID);
                                            DetailsList.add(DetailsID);

                                            DetailsData.put("detail", SavedItem.getSections().get(i).getSectionDetails().get(j).getDetail());

                                            if(!Strings.isNullOrEmpty(SavedItem.getSections().get(i).getSectionDetails().get(j).getLink()))
                                                DetailsData.put("link", SavedItem.getSections().get(i).getSectionDetails().get(j).getLink());

                                            db.collection("Details")
                                                    .document(DetailsDocumentID)
                                                    .set(DetailsData)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                                    {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            // Leave Empty for now.
                                                        }
                                                    });
                                        }

                                        SectionsData.put("details", DetailsList);
                                    }
                                }

                                db.collection("Sections")
                                        .document(SectionsDocumentID)
                                        .set(SectionsData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                // Leave Empty for now.
                                            }
                                        });
                            }

                            SupportData.put("sections", SectionsIDs);
                        }

                        SupportData.put("conclusion", SavedItem.getConclusion());

                        Long finalDetailsID = DetailsID;
                        Long finalSectionsID = SectionsID;
                        Long finalSupportID = SupportID;

                        db.collection("Support_Contents")
                                .document(SupportDocumentID)
                                .set(SupportData)
                                .addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        db.collection("Settings")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                                {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                                                    {
                                                        Map<String, Object> IDData = new HashMap<>();
                                                        IDData.put("SupportID", finalSupportID);
                                                        IDData.put("SectionsID", finalSectionsID);
                                                        IDData.put("DetailsID", finalDetailsID);

                                                        String Path = task.getResult().getDocuments().get(0).getReference().getId();

                                                        db.collection("Settings")
                                                                .document(Path)
                                                                .update(IDData)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>()
                                                                {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                    {

                                                                    }
                                                                });
                                                    }
                                                });

                                        Intent SendData = new Intent(getApplicationContext(), HomeActivity.class);
                                        SendData.putExtra("AddData", SavedItem);
                                        startActivityForResult(SendData, 1000);

                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        Toast.makeText(AddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });


    }

    private void editContent(Supports EditedItem, ArrayList<SectionDetails> OldSections)
    {
        // Run through all the Old Sections and Details and Delete Them.
        for(int i = 0; i < OldSections.size(); i++)
        {
            // Deleting all Details attached to This Section.
            for(int j = 0; j < OldSections.get(i).getSectionDetails().size(); j++)
            {
                db.collection("Details")
                        .document(OldSections.get(i).getSectionDetails().get(j).getDocumentID())
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
                    .document(OldSections.get(i).getDocumentID())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                        }
                    });

            db.collection("Settings")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            // Create a new document data
                            Map<String, Object> SupportData = new HashMap<>();
                            Map<String, Object> SectionsData = new HashMap<>();
                            Map<String, Object> DetailsData = new HashMap<>();

                            String SupportDocumentID = EditedItem.getDocumentID();

                            Long SectionsID = (Long) task.getResult().getDocuments().get(0).get("SectionsID");
                            Long DetailsID = (Long) task.getResult().getDocuments().get(0).get("DetailsID");

                            if(!Strings.isNullOrEmpty(EditedItem.getBannerUrl()))
                            {
                                SupportData.put("bannerUrl", EditedItem.getBannerUrl());
                            }

                            SupportData.put("title", EditedItem.getTitle());
                            SupportData.put("description", EditedItem.getDescription());
                            SupportData.put("parentCategory", EditedItem.getParentCategory());
                            SupportData.put("conclusion", EditedItem.getConclusion());

                            if(EditedItem.getSections().size() > 0)
                            {
                                ArrayList<Long> SectionsIDs = new ArrayList<>();

                                for (int i = 0; i < EditedItem.getSections().size(); i++)
                                {
                                    String SectionsDocumentID = UUID.randomUUID().toString();

                                    SectionsID++;

                                    SectionsData.put("id", SectionsID);
                                    SectionsIDs.add(SectionsID);

                                    SectionsData.put("heading", EditedItem.getSections().get(i).getSectionHeading());

                                    if(EditedItem.getSections().get(i).getSectionDetails() != null)
                                    {
                                        if(EditedItem.getSections().get(i).getSectionDetails().size() > 0)
                                        {
                                            ArrayList<Long> DetailsList = new ArrayList<>();

                                            for (int j = 0; j < EditedItem.getSections().get(i).getSectionDetails().size(); j++)
                                            {
                                                String DetailsDocumentID = UUID.randomUUID().toString();

                                                DetailsID++;

                                                DetailsData.put("id", DetailsID);
                                                DetailsList.add(DetailsID);

                                                DetailsData.put("detail", EditedItem.getSections().get(i).getSectionDetails().get(j).getDetail());

                                                db.collection("Details")
                                                        .document(DetailsDocumentID)
                                                        .set(DetailsData)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                                        {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                // Leave Empty for now.
                                                            }
                                                        });
                                            }

                                            SectionsData.put("details", DetailsList);
                                        }
                                    }

                                    db.collection("Sections")
                                            .document(SectionsDocumentID)
                                            .set(SectionsData)
                                            .addOnCompleteListener(new OnCompleteListener<Void>()
                                            {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    // Leave Empty for now.
                                                }
                                            });
                                }

                                SupportData.put("sections", SectionsIDs);
                            }

                            SupportData.put("conclusion", EditedItem.getConclusion());

                            Long finalDetailsID = DetailsID;
                            Long finalSectionsID = SectionsID;

                            db.collection("Support_Contents")
                                    .document(SupportDocumentID)
                                    .update(SupportData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            db.collection("Settings")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                                    {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                                                        {
                                                            Map<String, Object> IDData = new HashMap<>();
                                                            IDData.put("SectionsID", finalSectionsID);
                                                            IDData.put("DetailsID", finalDetailsID);

                                                            String Path = task.getResult().getDocuments().get(0).getReference().getId();

                                                            db.collection("Settings")
                                                                    .document(Path)
                                                                    .update(IDData)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                                                    {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task)
                                                                        {

                                                                        }
                                                                    });
                                                        }
                                                    });


                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {
                                            Toast.makeText(AddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
        }


    }
}