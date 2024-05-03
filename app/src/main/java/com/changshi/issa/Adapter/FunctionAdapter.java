package com.changshi.issa.Adapter;

import static android.content.Context.MODE_PRIVATE;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.changshi.issa.Fragment.WebpageFragment.REQUEST_CODE_PICK_IMAGE;
import static java.security.AccessController.*;
import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.MediaRouteButton;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.changshi.issa.DatabaseHandler.Functions;
import com.changshi.issa.DatabaseHandler.Supports;
import com.changshi.issa.Fragment.AccommodationFragment;
import com.changshi.issa.Fragment.FunctionsFragment;
import com.changshi.issa.Fragment.SupportContentFragment;
import com.changshi.issa.Fragment.SupportsFragment;
import com.changshi.issa.HomeActivity;
import com.changshi.issa.R;
import com.changshi.issa.SupportContent;
import com.squareup.picasso.Picasso;

import java.security.AccessController;
import java.util.ArrayList;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Functions> mFunctionList;
    private ImageView mFunctionImageToUpdate;

    public FunctionAdapter(Context _Context,  ArrayList<Functions> _FunctionList)
    {
        this.mContext = _Context;
        this.mFunctionList = _FunctionList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_function, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Functions currentFunction = mFunctionList.get(position);
        holder.txtFunctionTitle.setText(currentFunction.getNameOfFunction());

        if(mFunctionList.get(position).getFunctionURL().equals(""))
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
        holder.updateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a dialog to select an image
                mFunctionImageToUpdate = holder.functionImage;
                showImagePickerDialog();
            }

            private void showImagePickerDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
                ((Activity) mContext).startActivityForResult(galleryIntent, REQUEST_CODE_PICK_IMAGE);
            }

            private void showUrlInputDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Enter Image URL");

                final EditText input = new EditText(mContext);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String imageUrl = input.getText().toString();
                        Picasso.get().load(imageUrl).into(mFunctionImageToUpdate);
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
        holder.mainCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Create a new Intent.

                if(holder.txtFunctionTitle.getText() == "Learning Support")
                {
                    ArrayList<Supports> LearningSupportFunctions = new ArrayList<>();

                    Supports CourseSelectionFunction = new Supports();
                    CourseSelectionFunction.setTitle("Course Selection");
                    CourseSelectionFunction.setParentCategory("Learning Support");

                    LearningSupportFunctions.add(CourseSelectionFunction);

                    Supports AcademicFunction = new Supports();
                    AcademicFunction.setTitle("Academic Support");
                    AcademicFunction.setParentCategory("Learning Support");

                    LearningSupportFunctions.add(AcademicFunction);

                    Supports StudentCouncilFunction = new Supports();
                    StudentCouncilFunction.setTitle("Student Council");
                    StudentCouncilFunction.setParentCategory("Learning Support");

                    LearningSupportFunctions.add(StudentCouncilFunction);

                    Supports HealthFunction = new Supports();
                    HealthFunction.setTitle("Health & Wellbeing");
                    HealthFunction.setParentCategory("Learning Support");

                    // Remove all this Hard Coded information and put it in the Database.

                    // Get Information from DB.
                    // Add Information from DB to the Array.

                    LearningSupportFunctions.add(HealthFunction);
                    ((HomeActivity)mContext).openFragment(new SupportsFragment(LearningSupportFunctions), "Learning Support");
                }
                if(holder.txtFunctionTitle.getText() == "Social Activities")
                {
                    ArrayList<Supports> SocialActFunctions = new ArrayList<>();

                    Supports NetworkingFunction = new Supports();
                    NetworkingFunction.setTitle("Networking");
                    NetworkingFunction.setParentCategory("Social Activities");

                    SocialActFunctions.add(NetworkingFunction);

                    Supports SportClubFunction = new Supports();
                    SportClubFunction.setTitle("Sport Club");
                    SportClubFunction.setParentCategory("Social Activities");

                    SocialActFunctions.add(SportClubFunction);

                    Supports FoodOptionsFunction = new Supports();
                    FoodOptionsFunction.setTitle("Food Options");
                    FoodOptionsFunction.setParentCategory("Social Activities");

                    SocialActFunctions.add(FoodOptionsFunction);
                    ((HomeActivity)mContext).openFragment(new SupportsFragment(SocialActFunctions), "Social Activities");
                }
                if(holder.txtFunctionTitle.getText() == "Accommodation")
                {
                    ArrayList<Supports> AccommodationFunctions = new ArrayList<>();
//                    ArrayList<Supports> AccommodationFunctions = GetSupports(2);

                    Supports HomeStayFunction = new Supports();
                    HomeStayFunction.setTitle("Homestay");
                    HomeStayFunction.setParentCategory("Accommodation");

                    AccommodationFunctions.add(HomeStayFunction);

                    Supports RentalFunction = new Supports();
                    RentalFunction.setTitle("Rental");
                    RentalFunction.setParentCategory("Accommodation");

                    AccommodationFunctions.add(RentalFunction);

                    ((HomeActivity) mContext).openFragment(new SupportsFragment(AccommodationFunctions), "Accommodation");
                }
                if(holder.txtFunctionTitle.getText() == "Transports")
                {
                    ArrayList<Supports> TransportsFunction = new ArrayList<>();

                    Supports PublicTranFunction = new Supports();
                    PublicTranFunction.setTitle("Public Transport System");
                    PublicTranFunction.setParentCategory("Transports");

                    TransportsFunction.add(PublicTranFunction);

                    Supports AirportFunction = new Supports();
                    AirportFunction.setTitle("Airport Express");
                    AirportFunction.setParentCategory("Transports");

                    TransportsFunction.add(AirportFunction);

                    Supports CampusFunction = new Supports();
                    CampusFunction.setTitle("Campus Transfers");
                    CampusFunction.setParentCategory("Transports");

                    TransportsFunction.add(CampusFunction);

                    ((HomeActivity) mContext).openFragment(new SupportsFragment(TransportsFunction), "Transports");

                }
                if(holder.txtFunctionTitle.getText() == "Job Support")
                {
                    ArrayList<Supports> JobFunctions = new ArrayList<>();

                    Supports PartTimeFunction = new Supports();
                    PartTimeFunction.setTitle("Part-time Job");
                    PartTimeFunction.setParentCategory("Job Support");

                    JobFunctions.add(PartTimeFunction);

                    Supports InternshipFunction = new Supports();
                    InternshipFunction.setTitle("Internship");
                    InternshipFunction.setParentCategory("Job Support");

                    JobFunctions.add(InternshipFunction);

                    //Graduate job card
                    Supports GraduateJobFunction = new Supports();
                    GraduateJobFunction.setTitle("Graduate Job");
                    GraduateJobFunction.setParentCategory("Job Support");

                    JobFunctions.add(GraduateJobFunction);
                    ((HomeActivity)mContext).openFragment(new SupportsFragment(JobFunctions), "Job Support");
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mFunctionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtFunctionTitle;
        private ImageView functionImage;
        private ImageButton updateImageButton;
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

//    public ArrayList<Supports> GetSupports(int CategoryID)
//    {
//        ArrayList<Supports> CurrentSupports = new ArrayList<>();
//
//        return CurrentSupports;
//    }
}
