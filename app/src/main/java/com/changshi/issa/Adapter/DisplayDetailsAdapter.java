package com.changshi.issa.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.DatabaseHandler.Details;
import com.changshi.issa.R;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

public class DisplayDetailsAdapter extends RecyclerView.Adapter<DisplayDetailsAdapter.ViewHolder> {
    // List to hold Details objects
    private List<Details> detailsList;

    // Constructor for the adapter
    public DisplayDetailsAdapter(List<Details> detailsList) {
        // Initialize detailsList, if null, create a new empty list
        this.detailsList = detailsList != null ? detailsList : new ArrayList<>();
    }

    // Method to create ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for the details
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_details, parent, false);
        return new ViewHolder(view);
    }

    // Method to bind data to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the Details object for the current position
        Details details = detailsList.get(position);
        if (details != null) {
            // Set the text of the TextView to the detail
            holder.detailsTv.setText(details.getDetail());

            // If the link is not empty, set an OnClickListener for the button
            if (!Strings.isNullOrEmpty(details.getLink())) {
                holder.openLinkBn.setOnClickListener(v -> {
                    // Create an Intent to open the link
                    Intent OpenLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(details.getLink()));
                    holder.itemView.getContext().startActivity(OpenLinkIntent);
                });
            } else {
                // If the link is empty, hide the button
                holder.openLinkBn.setVisibility(View.GONE);
            }
        }
    }

    // Method to return the size of the list
    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    // Method to set a new list of Details objects and notify the adapter
    public void setDetailsList(ArrayList<Details> detailsList) {
        this.detailsList = detailsList != null ? detailsList : new ArrayList<>();
        notifyDataSetChanged();
    }

    // ViewHolder class to hold and manage view elements
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView detailsTv;
        Button openLinkBn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find views by their IDs
            detailsTv = itemView.findViewById(R.id.detailsTv);
            openLinkBn = itemView.findViewById(R.id.openLinkBn);
        }
    }
}
