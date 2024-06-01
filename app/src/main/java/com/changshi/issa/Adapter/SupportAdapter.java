package com.changshi.issa.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.changshi.issa.R.drawable.accommodation;
import static com.changshi.issa.R.drawable.jobsupport;
import static com.changshi.issa.R.drawable.learn;
import static com.changshi.issa.R.drawable.social;
import static com.changshi.issa.R.drawable.transit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.changshi.issa.AddActivity;
import com.changshi.issa.DatabaseHandler.Supports;
import com.changshi.issa.Fragment.SupportContentFragment;
import com.changshi.issa.HomeActivity;
import com.changshi.issa.R;
import com.google.common.base.Strings;

import java.util.ArrayList;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Supports> mSupportList;

    // Constructor to initialize the context and the support list
    public SupportAdapter(Context mContext, ArrayList<Supports> mSupportList) {
        this.mContext = mContext;
        this.mSupportList = mSupportList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_support, parent, false);
        return new MyViewHolder(view);
    }

    // Method to add a new item to the support list and notify the adapter
    public void AddItem(Supports NewSupports) {
        mSupportList.add(NewSupports);
        notifyItemInserted(mSupportList.size() - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Get the current item from the support list
        Supports currentItem = mSupportList.get(position);

        // Check if the banner URL is null or empty and set the appropriate image
        if (Strings.isNullOrEmpty(currentItem.getBannerUrl())) {
            switch (currentItem.getParentCategory()) {
                case "Learning Support":
                    holder.supportImage.setImageResource(learn);
                    break;
                case "Social Activities":
                    holder.supportImage.setImageResource(social);
                    break;
                case "Accommodation":
                    holder.supportImage.setImageResource(accommodation);
                    break;
                case "Transports":
                    holder.supportImage.setImageResource(transit);
                    break;
                case "Job Support":
                    holder.supportImage.setImageResource(jobsupport);
                    break;
            }
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(currentItem.getBannerUrl())
                    .into(holder.supportImage);
        }

        // Get shared preferences to check if the user is logged in
        SharedPreferences Pref = holder.itemView.getContext().getSharedPreferences("login_pref", MODE_PRIVATE);
        boolean IsLoggedIn = Pref.getBoolean("is_logged_in", false);

        // Hide the edit and delete buttons if the user is not logged in
        if (!IsLoggedIn) {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }

        // Set the click listener for the item to open the SupportContentFragment
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) mContext).openFragment(new SupportContentFragment(mSupportList.get(position)), mSupportList.get(position).getTitle());
            }
        });

        // Set the support title and description
        holder.txtSupportTitle.setText(currentItem.getTitle());
        holder.txtSupportDescription.setText(currentItem.getDescription());

        // Set the click listener for the edit button to start the AddActivity with edit mode
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NewIntent = new Intent(mContext, AddActivity.class);
                Supports SelectedSupport = mSupportList.get(position);
                NewIntent.putExtra("IsEditMode", true);
                NewIntent.putExtra("SelectedSupport", SelectedSupport);
                mContext.startActivity(NewIntent);
            }
        });

        // Set the click listener for the delete button to confirm and delete the support
        holder.deleteButton.setOnClickListener(v -> {
            // Show a confirmation dialog before deleting the support content
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Are you sure you want to DELETE it？")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        mSupportList.get(position).delete(mContext);
                        // Remove the support from the list and notify the adapter
                        mSupportList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mSupportList.size());
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        // Cancel the dialog
                        dialog.cancel();
                    });
            // Show the dialog
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    @Override
    public int getItemCount() {
        return mSupportList != null ? mSupportList.size() : 0;
    }

    // ViewHolder class to hold the views for each item
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView supportImage;
        private TextView txtSupportTitle;
        private TextView txtSupportDescription;
        private ImageView editButton;
        private ImageView deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            supportImage = itemView.findViewById(R.id.support_image_view);
            txtSupportTitle = itemView.findViewById(R.id.txt_support_title);
            txtSupportDescription = itemView.findViewById(R.id.txt_support_description);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
