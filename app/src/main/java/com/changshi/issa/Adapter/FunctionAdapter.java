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
import com.changshi.issa.Fragment.FunctionsFragment;
import com.changshi.issa.HomeActivity;
import com.changshi.issa.R;

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
                // This is Commented because the Activities don't exist yet.
                // Create a new Intent.

                // Code.
                if(holder.txtFunctionTitle.getText() == "Learning Support")
                {
                    ArrayList<Functions> LearningSupportFunctions = new ArrayList<>();

                    Functions CourseSelectionFunction = new Functions();

                    CourseSelectionFunction.setNameOfFunction("Course Selection");
                    CourseSelectionFunction.setfuncationImage("http://www.google.com");

                    LearningSupportFunctions.add(CourseSelectionFunction);
                    ((HomeActivity)mContext).openFragment(new FunctionsFragment(LearningSupportFunctions));
                }
                if(holder.txtFunctionTitle.getText() == "Social Activities")
                {
                    // Create Intent to Send to Social Activities Activity.
                    // Send to Social Activities Activity.
                    // Finish();
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
