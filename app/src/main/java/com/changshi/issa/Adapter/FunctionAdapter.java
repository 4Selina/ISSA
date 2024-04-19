package com.changshi.issa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.changshi.issa.DatabaseHandler.Functions;
import com.changshi.issa.Fragment.AccommodationFragment;
import com.changshi.issa.Fragment.FunctionsFragment;
import com.changshi.issa.Fragment.SupportContentFragment;
import com.changshi.issa.HomeActivity;
import com.changshi.issa.R;
import com.changshi.issa.SupportContent;

import java.util.ArrayList;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Functions> mFunctionList;

    public FunctionAdapter(Context _Context, ArrayList<Functions> _FunctionList)
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

        if(mFunctionList.get(position).getfuncationImageURL().equals(""))
        {
            holder.functionImage.setImageResource(mFunctionList.get(position).getfuncationImage());
        }
        else
        {
            // Use Glide or Picasso to Show the Image from the URL.
        }

        // Click on the Card and Do Code.
        holder.mainCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Create a new Intent.

                if(holder.txtFunctionTitle.getText() == "Learning Support")
                {
                    ArrayList<Functions> LearningSupportFunctions = new ArrayList<>();

                    Functions CourseSelectionFunction = new Functions();
                    CourseSelectionFunction.setNameOfFunction("Course Selection");
                    CourseSelectionFunction.setfuncationImage(R.drawable.learn);

                    LearningSupportFunctions.add(CourseSelectionFunction);
                    ((HomeActivity)mContext).openFragment(new FunctionsFragment(LearningSupportFunctions), "CourseSelection");

                    Functions AcademicFunction = new Functions();
                    AcademicFunction.setNameOfFunction("Academic Support");
                    AcademicFunction.setfuncationImage(R.drawable.learn);

                    LearningSupportFunctions.add(AcademicFunction);
                    ((HomeActivity)mContext).openFragment(new FunctionsFragment(LearningSupportFunctions), "Academic");


                    Functions StudentCouncilFunction = new Functions();
                    StudentCouncilFunction.setNameOfFunction("Student Council");
                    StudentCouncilFunction.setfuncationImage(R.drawable.learn);

                    LearningSupportFunctions.add(StudentCouncilFunction);
                    ((HomeActivity)mContext).openFragment(new FunctionsFragment(LearningSupportFunctions), "Student Council");

                    Functions HealthFunction = new Functions();
                    HealthFunction.setNameOfFunction("Health & Wellbeing");
                    HealthFunction.setfuncationImage(R.drawable.learn);

                    LearningSupportFunctions.add(HealthFunction);
                    ((HomeActivity)mContext).openFragment(new FunctionsFragment(LearningSupportFunctions), "Learning Support");
                }
                if(holder.txtFunctionTitle.getText() == "Social Activities")
                { ArrayList<Functions> SocialActFunctions = new ArrayList<>();

                    Functions NetworkingFunction = new Functions();
                    NetworkingFunction.setNameOfFunction("Networking");
                    NetworkingFunction.setfuncationImage(R.drawable.social);

                    SocialActFunctions.add(NetworkingFunction);
                    ((HomeActivity)mContext).openFragment(new FunctionsFragment(SocialActFunctions), "Networking");


                    Functions SportClubFunction = new Functions();
                    SportClubFunction.setNameOfFunction("Sport Club");
                    SportClubFunction.setfuncationImage(R.drawable.social);

                    SocialActFunctions.add(SportClubFunction);
                    ((HomeActivity)mContext).openFragment(new FunctionsFragment(SocialActFunctions), "Sport Club");


                    Functions FoodOptionsFunction = new Functions();
                    FoodOptionsFunction.setNameOfFunction("Food Options");
                    FoodOptionsFunction.setfuncationImage(R.drawable.social);

                    SocialActFunctions.add(FoodOptionsFunction);
                    ((HomeActivity)mContext).openFragment(new FunctionsFragment(SocialActFunctions), "Social Activities");

                    // Finish();
                }
                if(holder.txtFunctionTitle.getText() == "Accommodation")
                {

                    ArrayList<Functions> AccommodationFunctions = new ArrayList<>();

                    Functions HomeStayFunction = new Functions();
                    HomeStayFunction.setNameOfFunction("Homestay");
                    HomeStayFunction.setfuncationImage(R.drawable.accommodation);

                    AccommodationFunctions.add(HomeStayFunction);

                    Functions RentalFunction = new Functions();
                    RentalFunction.setNameOfFunction("Rental");
                    RentalFunction.setfuncationImage(R.drawable.accommodation);

                    AccommodationFunctions.add(RentalFunction);

                    ((HomeActivity) mContext).openFragment(new FunctionsFragment(AccommodationFunctions), "Accommodation");

                }
                if(holder.txtFunctionTitle.getText() == "Transports")
                {ArrayList<Functions> TransportsFunction = new ArrayList<>();

                    Functions PublicTranFunction = new Functions();
                    PublicTranFunction.setNameOfFunction("Public Transport System");
                    PublicTranFunction.setfuncationImage(R.drawable.transit);

                    TransportsFunction.add(PublicTranFunction);
                    ((HomeActivity)mContext).openFragment(new FunctionsFragment(TransportsFunction), "Public Transport System");


                    Functions AirportFunction = new Functions();
                    AirportFunction.setNameOfFunction("Airport Express");
                    AirportFunction.setfuncationImage(R.drawable.transit);

                    TransportsFunction.add(AirportFunction);
                    ((HomeActivity)mContext).openFragment(new FunctionsFragment(TransportsFunction), "Airport Express");


                    Functions CampusFunction = new Functions();
                    CampusFunction.setNameOfFunction("Campus Transfers");
                    CampusFunction.setfuncationImage(R.drawable.transit);

                    TransportsFunction.add(CampusFunction);
                    ((HomeActivity)mContext).openFragment(new FunctionsFragment(TransportsFunction), "Transports");
                }
                if(holder.txtFunctionTitle.getText() == "Job Support")
                {ArrayList<Functions> JobFunctions = new ArrayList<>();

                    Functions PartTimeFunction = new Functions();
                    PartTimeFunction.setNameOfFunction("Part-time Job");
                    PartTimeFunction.setfuncationImage(R.drawable.jobsupport);

                    JobFunctions.add(PartTimeFunction);
                    ((HomeActivity)mContext).openFragment(new FunctionsFragment(JobFunctions), "Part-time Job");


                    Functions InternshipFunction = new Functions();
                    InternshipFunction.setNameOfFunction("Internship");
                    InternshipFunction.setfuncationImage(R.drawable.jobsupport);

                    JobFunctions.add(InternshipFunction);
                    ((HomeActivity)mContext).openFragment(new FunctionsFragment(JobFunctions), "Internship");

                    //Graduate job card
                    Functions GraduateJobFunction = new Functions();
                    GraduateJobFunction.setNameOfFunction("Graduate Job");
                    GraduateJobFunction.setfuncationImage(R.drawable.jobsupport);

                    JobFunctions.add(GraduateJobFunction);
                    ((HomeActivity)mContext).openFragment(new FunctionsFragment(JobFunctions), "Job Support");
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
        private ConstraintLayout mainCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFunctionTitle = itemView.findViewById(R.id.txtFunctionTitle);
            functionImage = itemView.findViewById(R.id.functionImage);
            mainCard = itemView.findViewById(R.id.functionCard);
        }
    }
}
