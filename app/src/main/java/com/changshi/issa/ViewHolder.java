package com.changshi.issa;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView mDepartmentTextView;
    public TextView mEmailTextView;
    public TextView mContactTextView;
    public TextView mAddressTextView;
    public ImageView mImageView;
    public View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

        //item long click listener
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                    mClickListener.onItemLongClick(v, getAdapterPosition());

                return true;
            }
        });

        //initialize views with webpageItem layout
        mDepartmentTextView = itemView.findViewById(R.id.departmentTv);
        mEmailTextView = itemView.findViewById(R.id.emailTv);
        mContactTextView = itemView.findViewById(R.id.contactTv);
        mAddressTextView = itemView.findViewById(R.id.addressTv);
        mImageView = itemView.findViewById(R.id.imgWebpage);
    }

    private static ViewHolder.ClickListener mClickListener;

    //interface for click listener
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public static void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener; // Set mClickListener here
    }
}
