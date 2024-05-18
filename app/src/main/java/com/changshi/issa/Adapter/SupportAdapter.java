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

    public SupportAdapter(Context mContext, ArrayList<Supports> mSupportList)
    {
        this.mContext = mContext;
        this.mSupportList = mSupportList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_support, parent, false);
        return new MyViewHolder(view);
    }

    public void AddItem(Supports NewSupports)
    {
        mSupportList.add(NewSupports);
        notifyItemInserted(mSupportList.size() - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
                ((HomeActivity)mContext).openFragment(new SupportContentFragment(mSupportList.get(position)), mSupportList.get(position).getTitle());
            }
        });

        holder.txtSupportTitle.setText(currentItem.getTitle());
        holder.txtSupportDescription.setText(currentItem.getDescription());

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

        holder.deleteButton.setOnClickListener(v -> {
            // confirm before deleting support content
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Are you sure you want to DELETE it？")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) ->
                    {
                        mSupportList.get(position).delete(mContext);
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
        return mSupportList != null ? mSupportList.size() : 0;
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
        }
    }
}