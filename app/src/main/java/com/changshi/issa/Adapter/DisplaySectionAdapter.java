package com.changshi.issa.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.DatabaseHandler.Details;
import com.changshi.issa.DatabaseHandler.SectionDetails;
import com.changshi.issa.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class DisplaySectionAdapter extends RecyclerView.Adapter<DisplaySectionAdapter.ViewHolder> {
    // List to hold SectionDetails objects
    private ArrayList<SectionDetails> sectionList;

    // Constructor for the adapter
    public DisplaySectionAdapter(ArrayList<SectionDetails> sectionList) {
        this.sectionList = sectionList;
    }

    // Method to create ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for the section
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_section, parent, false);
        return new ViewHolder(view);
    }

    // Method to bind data to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the SectionDetails object for the current position
        SectionDetails section = sectionList.get(position);

        // Set the heading text of the section
        holder.headingTv.setText(section.getSectionHeading());

        // Set up the RecyclerView for displaying details
        holder.rvDisplayDetails.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));

        // Get the list of details, sort it by ID, and set it to the DisplayDetailsAdapter
        ArrayList<Details> details = section.getSectionDetails();
        ArrayList<Details> orderedDetails = (ArrayList<Details>) details.stream()
                .sorted(Comparator.comparingLong(Details::getID))
                .collect(Collectors.toList());
        holder.displayDetailsAdapter = new DisplayDetailsAdapter(orderedDetails);
        holder.rvDisplayDetails.setAdapter(holder.displayDetailsAdapter);
    }

    // Method to update the list of SectionDetails objects and notify the adapter
    public void updateSection(ArrayList<SectionDetails> sectionList) {
        this.sectionList = sectionList;
        notifyDataSetChanged();
    }

    // Method to return the size of the list
    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    // ViewHolder class to hold and manage view elements
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView headingTv;
        RecyclerView rvDisplayDetails;
        DisplayDetailsAdapter displayDetailsAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find views by their IDs
            headingTv = itemView.findViewById(R.id.headingTv);
            rvDisplayDetails = itemView.findViewById(R.id.rv_display_details);
        }
    }
}
