package com.changshi.issa.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.changshi.issa.DatabaseHandler.WebpageItem;
import com.changshi.issa.Fragment.WebpageFragment;
import com.changshi.issa.R;
import com.changshi.issa.UpdateWebpageActivity;
import com.changshi.issa.ViewHolder;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class WebpageAdapter extends RecyclerView.Adapter<ViewHolder> {
    private FragmentActivity webpageFragment;
    private ArrayList<WebpageItem> webpageItems;
    private Context context;
//    private FirebaseFirestore mFirestore;


//    public WebpageAdapter(FragmentActivity webpageFragment, ArrayList<WebpageItem> webpageItems, Context context) {
//        this.webpageFragment = webpageFragment;
//        this.webpageItems = webpageItems;
//        this.context = context;
//    }

    public WebpageAdapter(FragmentActivity webpageFragment, ArrayList<WebpageItem> webpageItems, Context context) {
        this.webpageFragment = webpageFragment;
        this.webpageItems = webpageItems;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_web_inform, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);

        //handle item clicks here
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //show data in toast on clicking
                String department = webpageItems.get(position).getDepartment();
                String email = webpageItems.get(position).getEmail();
                String contact = webpageItems.get(position).getContact();
                String address = webpageItems.get(position).getAddress();
//                Toast.makeText(webpageFragment, department + "\n" + email + "\n" + contact + "\n" + address, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(webpageFragment);
                //options to display in dialog
                String[] options = {"Update", "Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //update is clicked
                            //get data
                            String id = webpageItems.get(position).getId();
                            String department = webpageItems.get(position).getDepartment();
                            String email = webpageItems.get(position).getEmail();
                            String contact = webpageItems.get(position).getContact();
                            String address = webpageItems.get(position).getAddress();
                            //intent to start activity
                            Intent intent = new Intent(webpageFragment, UpdateWebpageActivity.class);
                            //put data in intent
                            intent.putExtra("pId", id);
                            intent.putExtra("pDepartment", department);
                            intent.putExtra("pEmail", email);
                            intent.putExtra("pContact", contact);
                            intent.putExtra("pAddress", address);
                            //start activity
                            webpageFragment.startActivity(intent);

                        }
                        if (which == 1) {
                            //delete is clicked
                            webpageItems.remove(position);
                            notifyItemRemoved(position);
                        }
                    }
                }).create().show();
            }
        });
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //bind view /set data
        viewHolder.mDepartmentTextView.setText(webpageItems.get(i).getDepartment());
        viewHolder.mEmailTextView.setText(webpageItems.get(i).getEmail());
        viewHolder.mContactTextView.setText(webpageItems.get(i).getContact());
        viewHolder.mAddressTextView.setText(webpageItems.get(i).getAddress());
//        viewHolder.mImageView.
        //use glide to update image
//        Glide.with(context).load(webpageItems.get(i).getImageUrl()).into(viewHolder.mImageView);

    }

    @Override
    public int getItemCount() {
        return (webpageItems != null) ? webpageItems.size() : 0;
    }
    public void updateData(ArrayList<WebpageItem> newWebpageItems) {
        this.webpageItems = newWebpageItems;
        notifyDataSetChanged();
    }
}