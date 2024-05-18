package com.changshi.issa.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.DatabaseHandler.Details;
import com.changshi.issa.DatabaseHandler.Functions;
import com.changshi.issa.DatabaseHandler.SectionDetails;
import com.changshi.issa.DatabaseHandler.Supports;
import com.changshi.issa.Fragment.SupportsFragment;
import com.changshi.issa.HomeActivity;
import com.changshi.issa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.MyViewHolder>
{
    private final Context mContext;
    private final ArrayList<Functions> mFunctionList;
    private ImageView mFunctionImageToUpdate;

    public FunctionAdapter(Context _Context,  ArrayList<Functions> _FunctionList)
    {
        this.mContext = _Context;
        this.mFunctionList = _FunctionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_function, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        Functions currentFunction = mFunctionList.get(position);
        holder.txtFunctionTitle.setText(currentFunction.getNameOfFunction());

        if(Strings.isNullOrEmpty(mFunctionList.get(position).getFunctionURL()))
        {
            holder.functionImage.setImageResource(mFunctionList.get(position).getFunctionImage());
        }
        else
        {
            Picasso.get().load(currentFunction.getFunctionURL()).into(holder.functionImage);
        }

        SharedPreferences Pref = holder.itemView.getContext().getSharedPreferences("login_pref", MODE_PRIVATE);
        boolean IsLoggedIn = Pref.getBoolean("is_logged_in", false);

        if(!IsLoggedIn)
        {
            holder.updateImageButton.setVisibility(View.GONE);
        }

        // Click on the Update Image Button
        holder.updateImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Show a dialog to select an image
                mFunctionImageToUpdate = holder.functionImage;
                showImagePickerDialog();
            }

            //open a dialog to allow user update image
            private void showImagePickerDialog()
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Select Image Source")
                        .setItems(new CharSequence[]{"URL"}, (dialog, which) -> {
                            switch (which) {
                                //update image by url
                                case 0:
                                    showUrlInputDialog();
                                    break;
                                    // update image by local phone
//                                case 1:
//                                    localImageDialog();
//                                    break;
                            }
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                builder.create().show();
            }

            //function image url input
            private void showUrlInputDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Enter Image URL");

                final EditText input = new EditText(mContext);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                //submit the input url

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String imageUrl = input.getText().toString();

                        //if no update image, the function image will be default one
                        if(!Strings.isNullOrEmpty(imageUrl))
                            Picasso.get().load(imageUrl).into(mFunctionImageToUpdate);
                        else
                            holder.functionImage.setImageResource(mFunctionList.get(position).getFunctionImage());

                        mFunctionList.get(position).setFunctionURL(imageUrl);

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Settings")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                                    {
                                        for (DocumentSnapshot SelectedDocument : task.getResult().getDocuments())
                                        {
                                            if(SelectedDocument.contains("AccommodationUrl"))
                                            {
                                                Map<String, Object> UrlData = new HashMap<>();

                                                switch (mFunctionList.get(position).getNameOfFunction())
                                                {
                                                    case "Learning Support":
                                                        UrlData.put("LearningSupportUrl", imageUrl);
                                                        break;
                                                    case "Social Activities":
                                                        UrlData.put("SocialActivitiesUrl", imageUrl);
                                                        break;
                                                    case "Accommodation":
                                                        UrlData.put("AccommodationUrl", imageUrl);
                                                        break;
                                                    case "Transport":
                                                        UrlData.put("TransportUrl", imageUrl);
                                                        break;
                                                    case "Job Support":
                                                        UrlData.put("JobSupportUrl", imageUrl);
                                                        break;
                                                }

                                                String Path = SelectedDocument.getReference().getId();

                                                db.collection("Settings")
                                                        .document(Path)
                                                        .update(UrlData)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                                        {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {

                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });

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

        // Click on the Card and Do Code.
        holder.mainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Create a new Intent.
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("Support_Contents")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                ArrayList<Supports> AllSupports = new ArrayList<>();

                                for (DocumentSnapshot SelectedDocument : task.getResult().getDocuments())
                                {
                                    if(SelectedDocument.get("parentCategory").toString().equals(holder.txtFunctionTitle.getText()))
                                    {
                                        Supports NewSupports = new Supports();

                                        NewSupports.setId((Long)SelectedDocument.get("id"));
                                        NewSupports.setDocumentID(SelectedDocument.getReference().getId());
                                        NewSupports.setTitle(SelectedDocument.get("title").toString());
                                        NewSupports.setDescription(SelectedDocument.get("description").toString());

                                        if (SelectedDocument.contains("bannerUrl") && SelectedDocument.get("bannerUrl") != null) {
                                            NewSupports.setBannerUrl(SelectedDocument.get("bannerUrl").toString());
                                        }


                                        NewSupports.setParentCategory(SelectedDocument.get("parentCategory").toString());

                                        // Get the Sections.
                                        ArrayList<Long> SectionIDs = (ArrayList<Long>)SelectedDocument.get("sections");

                                        db.collection("Sections")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                                {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                                                    {
                                                        ArrayList<SectionDetails> AllSections = new ArrayList<>();

                                                        for (DocumentSnapshot SelectedSection : task.getResult().getDocuments())
                                                        {
                                                            boolean IDIsCorrect = false;

                                                            for (Long SelectedID : SectionIDs)
                                                            {
                                                                if(SelectedID == (Long) SelectedSection.get("id"))
                                                                {
                                                                    IDIsCorrect = true;
                                                                }
                                                            }

                                                            if(IDIsCorrect)
                                                            {
                                                                SectionDetails NewSection = new SectionDetails();
                                                                NewSection.setID((Long)SelectedSection.get("id"));
                                                                NewSection.setDocumentID(SelectedSection.getReference().getId());

                                                                NewSection.setSectionHeading(SelectedSection.get("heading").toString());

                                                                ArrayList<Long> DetailsIDs = (ArrayList<Long>)SelectedSection.get("details");

                                                                db.collection("Details")
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                                                        {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                                                                            {
                                                                                ArrayList<Details> AllDetails = new ArrayList<>();

                                                                                for(DocumentSnapshot SelectedDetail : task.getResult().getDocuments())
                                                                                {
                                                                                    //boolean IsCorrectID = false;

                                                                                    for(Long SelectedDetailID : DetailsIDs)
                                                                                    {
                                                                                        if(SelectedDetailID.equals(SelectedDetail.getData().get("id")) )
                                                                                        {
                                                                                            //IsCorrectID = true;
                                                                                            Details NewDetail = new Details();
                                                                                            NewDetail.setID((Long)SelectedDetail.getData().get("id"));
                                                                                            NewDetail.setDocumentID(SelectedDetail.getReference().getId());

                                                                                            NewDetail.setDetail(SelectedDetail.get("detail").toString());

                                                                                            if(SelectedDetail.contains("link"))
                                                                                                NewDetail.setLink(SelectedDetail.getString("link"));

                                                                                            AllDetails.add(NewDetail);
                                                                                        }
                                                                                    }

                                                                                   /* if(IsCorrectID)
                                                                                    {
                                                                                        Details NewDetail = new Details();
                                                                                        NewDetail.setID((Long)SelectedDetail.get("id"));
                                                                                        NewDetail.setDocumentID(SelectedDetail.getReference().getId());

                                                                                        NewDetail.setDetail(SelectedDetail.get("detail").toString());

                                                                                        if(SelectedDetail.contains("link"))
                                                                                            NewDetail.setLink(SelectedDetail.getString("link"));

                                                                                        AllDetails.add(NewDetail);
                                                                                    }*/
                                                                                }

                                                                                NewSection.setSectionDetails(AllDetails);
                                                                            }
                                                                        });

                                                                AllSections.add(NewSection);
                                                            }
                                                        }

                                                        NewSupports.setSections(AllSections);
                                                    }
                                                });

                                        if (SelectedDocument.contains("conclusion") && SelectedDocument.get("conclusion") != null) {
                                            NewSupports.setConclusion(SelectedDocument.get("conclusion").toString());
                                        }


                                        AllSupports.add(NewSupports);
                                    }
                                }

                                ((HomeActivity)mContext).openFragment(new SupportsFragment(AllSupports), holder.txtFunctionTitle.getText().toString());
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFunctionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtFunctionTitle;
        private final ImageView functionImage;
        private final ImageButton updateImageButton;
        private ConstraintLayout mainCard;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFunctionTitle = itemView.findViewById(R.id.txtFunctionTitle);
            functionImage = itemView.findViewById(R.id.functionImage);
            updateImageButton = itemView.findViewById(R.id.updateImageButton);
            mainCard = itemView.findViewById(R.id.functionCard);
        }

    }

}
