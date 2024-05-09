package com.changshi.issa.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.DatabaseHandler.WebpageItem;
import com.changshi.issa.Fragment.WebpageFragment;
import com.changshi.issa.R;
import com.changshi.issa.UpdateWebpageActivity;

import java.util.ArrayList;

public class WebpageAdapter extends RecyclerView.Adapter<WebpageAdapter.ViewHolder> {
    public FragmentActivity webpageFragment;
    public WebpageFragment wpageFragment;
    public ArrayList<WebpageItem> webpageItems;
    public Context context;
//    private FirebaseFirestore mFirestore;


//    public WebpageAdapter() {
//
//    }

    public WebpageAdapter( WebpageFragment wpageFragment, FragmentActivity webpageFragment, ArrayList<WebpageItem> webpageItems, Context context)
    {
        this.webpageFragment = webpageFragment;
        this.webpageItems = webpageItems;
        this.context = context;
        this.wpageFragment = wpageFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_web_inform, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);

        //handle item clicks here
        viewHolder.setOnClickListener(new ViewHolder.ClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                //show data in toast on clicking
//              String department = webpageItems.get(position).getDepartment();
//              String email = webpageItems.get(position).getEmail();
//              String contact = webpageItems.get(position).getContact();
//              String address = webpageItems.get(position).getAddress();

                // Toast.makeText(webpageFragment, department + "\n" + email + "\n" + contact + "\n" + address, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, final int position)
            {
                SharedPreferences Pref = view.getContext().getSharedPreferences("login_pref", MODE_PRIVATE);
                boolean IsLoggedIn = Pref.getBoolean("is_logged_in", false);

                if(IsLoggedIn)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(webpageFragment);
                    //options to display in dialog
                    String[] options = {"Update", "Delete"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0)
                            {
                                //update is clicked
                                //get data

                                Long id = webpageItems.get(position).getId();
                                String documentID = webpageItems.get(position).getDocumentID();
                                String department = webpageItems.get(position).getDepartment();
                                String email = webpageItems.get(position).getEmail();
                                String contact = webpageItems.get(position).getContact();
                                String address = webpageItems.get(position).getAddress();

                                //intent to start activity
                                Intent intent = new Intent(webpageFragment, UpdateWebpageActivity.class);
                                //put data in intent
                                intent.putExtra("pId", id);
                                intent.putExtra("pDocID", documentID);
                                intent.putExtra("pDepartment", department);
                                intent.putExtra("pEmail", email);
                                intent.putExtra("pContact", contact);
                                intent.putExtra("pAddress", address);
                                //start activity
                                webpageFragment.startActivity(intent);

                            }
                            if (which == 1)
                            {
                                //delete is clicked
                                wpageFragment.deleteData(position);
                                webpageItems.remove(position);
                                notifyItemRemoved(position);
                            }
                        }
                    }).create().show();
                }
            }
        });
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        WebpageItem item = webpageItems.get(i);

        // Set department
        if (!TextUtils.isEmpty(item.getDepartment())) {
            viewHolder.mDepartmentTextView.setText(item.getDepartment());
            viewHolder.mDepartmentTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mDepartmentTextView.setVisibility(View.GONE);
        }

        // Set email
        if (!TextUtils.isEmpty(item.getEmail())) {
            viewHolder.mEmailTextView.setText(item.getEmail());
            viewHolder.mEmailTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mEmailTextView.setVisibility(View.GONE);
        }

        // Set contact
        if (!TextUtils.isEmpty(item.getContact())) {
            viewHolder.mContactTextView.setText(item.getContact());
            viewHolder.mContactTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mContactTextView.setVisibility(View.GONE);
        }

        // Set address
        if (!TextUtils.isEmpty(item.getAddress())) {
            viewHolder.mAddressTextView.setText(item.getAddress());
            viewHolder.mAddressTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mAddressTextView.setVisibility(View.GONE);
        }
    }

//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
//        //bind view /set data
//        viewHolder.mDepartmentTextView.setText(webpageItems.get(i).getDepartment());
//        viewHolder.mEmailTextView.setText(webpageItems.get(i).getEmail());
//        viewHolder.mContactTextView.setText(webpageItems.get(i).getContact());
//        viewHolder.mAddressTextView.setText(webpageItems.get(i).getAddress());
////        viewHolder.mImageView.
//        //use glide to update image
////        Glide.with(context).load(webpageItems.get(i).getImageUrl()).into(viewHolder.mImageView);
//
//    }

    @Override
    public int getItemCount() {
        return (webpageItems != null) ? webpageItems.size() : 0;
    }
    public void updateData(ArrayList<WebpageItem> newWebpageItems) {
        this.webpageItems = newWebpageItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        //work for webpage display
        public TextView mDepartmentTextView;
        public TextView mEmailTextView;
        public TextView mContactTextView;
        public TextView mAddressTextView;
        public ImageView mImageView;
        public View mView;

        public ViewHolder(@NonNull View itemView)
        {
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
            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
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
        public interface ClickListener
        {
            void onItemClick(View view, int position);
            void onItemLongClick(View view, int position);
        }

        public static void setOnClickListener(ViewHolder.ClickListener clickListener){
            mClickListener = clickListener; // Set mClickListener here
        }
    }
}