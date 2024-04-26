package com.changshi.issa.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.changshi.issa.R.drawable.accommodation;
import static com.changshi.issa.R.drawable.job;
import static com.changshi.issa.R.drawable.jobsupport;
import static com.changshi.issa.R.drawable.learn;
import static com.changshi.issa.R.drawable.social;
import static com.changshi.issa.R.drawable.transit;
import static com.changshi.issa.R.drawable.transport;

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

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.changshi.issa.AddActivity;
import com.changshi.issa.EditActivity;
import com.changshi.issa.DatabaseHandler.Supports;
import com.changshi.issa.Fragment.SupportContentFragment;
import com.changshi.issa.Fragment.SupportsFragment;
import com.changshi.issa.HomeActivity;
import com.changshi.issa.R;
import com.google.common.base.Strings;

import java.util.ArrayList;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Supports> mSupportList;
    private static SupportItemClickListener mListener;

    public SupportAdapter(Context mContext, ArrayList<Supports> mSupportList, SupportItemClickListener mListener) {
        this.mContext = mContext;
        this.mSupportList = mSupportList;
        this.mListener = mListener;
    }

    public SupportAdapter(FragmentActivity activity, ArrayList<Supports> allSupports) {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_support, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Supports currentItem = mSupportList.get(position);

        if(Strings.isNullOrEmpty(currentItem.getBannerUrl()))
        {
            switch (currentItem.getParentCategory())
            {
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
        }
        else
        {
            Glide.with(holder.itemView.getContext())
                    .load(currentItem.getBannerUrl())
                    .into(holder.supportImage);
        }

        SharedPreferences Pref = holder.itemView.getContext().getSharedPreferences("login_pref", MODE_PRIVATE);
        boolean IsLoggedIn = Pref.getBoolean("is_logged_in", false);

        if(!IsLoggedIn)
        {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ((HomeActivity)mContext).openFragment(new SupportContentFragment(new ArrayList<>()), "Learning Support");
            }
        });

        holder.txtSupportTitle.setText(currentItem.getTitle());
        holder.txtSupportDescription.setText(currentItem.getDescription());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NewIntent = new Intent(mContext, AddActivity.class);
                NewIntent.putExtra("IsEditModeSupport", true);

                mContext.startActivity(NewIntent);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            // confirm before deleting support content
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Are you sure you want to DELETE it？")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        // yes, delete support
                        mSupportList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mSupportList.size());
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        // no, cancel
                        dialog.cancel();
                    });
            // dialog display
            AlertDialog alert = builder.create();
            alert.show();
        });

    }

    @Override
    public int getItemCount() {
        return mSupportList.size();
    }


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
            editButton.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onEditClick(getAdapterPosition());
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onDeleteClick(getAdapterPosition());
                }
            });
        }
    }
}