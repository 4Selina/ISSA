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

    private FragmentActivity webpageFragment;
    private WebpageFragment wpageFragment;
    private ArrayList<WebpageItem> webpageItems;
    private Context context;

    // Constructor to initialize the adapter with required data
    public WebpageAdapter(WebpageFragment wpageFragment, FragmentActivity webpageFragment, ArrayList<WebpageItem> webpageItems, Context context) {
        this.webpageFragment = webpageFragment;
        this.webpageItems = webpageItems;
        this.context = context;
        this.wpageFragment = wpageFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // Inflate the layout for each item in the RecyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_web_inform, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        // Handle item clicks here
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Show data in toast on clicking (this is commented out, can be customized)
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                // Get shared preferences to check if the user is logged in
                SharedPreferences Pref = view.getContext().getSharedPreferences("login_pref", MODE_PRIVATE);
                boolean IsLoggedIn = Pref.getBoolean("is_logged_in", false);

                if (IsLoggedIn) {
                    // Show a dialog with options to update or delete the item
                    AlertDialog.Builder builder = new AlertDialog.Builder(webpageFragment);
                    String[] options = {"Update", "Delete"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                // Update is clicked
                                // Get data from the selected item
                                Long id = webpageItems.get(position).getId();
                                String documentID = webpageItems.get(position).getDocumentID();
                                String department = webpageItems.get(position).getDepartment();
                                String email = webpageItems.get(position).getEmail();
                                String contact = webpageItems.get(position).getContact();
                                String address = webpageItems.get(position).getAddress();

                                // Intent to start UpdateWebpageActivity
                                Intent intent = new Intent(webpageFragment, UpdateWebpageActivity.class);
                                // Put data in the intent
                                intent.putExtra("pId", id);
                                intent.putExtra("pDocID", documentID);
                                intent.putExtra("pDepartment", department);
                                intent.putExtra("pEmail", email);
                                intent.putExtra("pContact", contact);
                                intent.putExtra("pAddress", address);
                                // Start the activity
                                webpageFragment.startActivity(intent);
                            }
                            if (which == 1) {
                                // Delete is clicked
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
        // Get the current item from the list
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

    @Override
    public int getItemCount() {
        return (webpageItems != null) ? webpageItems.size() : 0;
    }

    // Method to update the data in the adapter
    public void updateData(ArrayList<WebpageItem> newWebpageItems) {
        this.webpageItems = newWebpageItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Views for webpage display
        public TextView mDepartmentTextView;
        public TextView mEmailTextView;
        public TextView mContactTextView;
        public TextView mAddressTextView;
        public ImageView mImageView;
        public View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            // Item click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });

            // Item long click listener
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });

            // Initialize views with webpage item layout
            mDepartmentTextView = itemView.findViewById(R.id.departmentTv);
            mEmailTextView = itemView.findViewById(R.id.emailTv);
            mContactTextView = itemView.findViewById(R.id.contactTv);
            mAddressTextView = itemView.findViewById(R.id.addressTv);
            mImageView = itemView.findViewById(R.id.imgWebpage);
        }

        private static ClickListener mClickListener;

        // Interface for click listener
        public interface ClickListener {
            void onItemClick(View view, int position);
            void onItemLongClick(View view, int position);
        }

        public static void setOnClickListener(ClickListener clickListener) {
            mClickListener = clickListener; // Set mClickListener here
        }
    }
}
