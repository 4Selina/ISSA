package com.changshi.issa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.DatabaseHandler.Details;
import com.changshi.issa.DatabaseHandler.SectionDetails;
import com.changshi.issa.R;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {
    private List<SectionDetails> sectionDetailsList; // List to hold section details
    private Context currentContext; // Context of the calling activity or fragment

    // Constructor for the adapter
    public SectionAdapter(List<SectionDetails> sectionDetailsList, Context currentContext) {
        this.sectionDetailsList = sectionDetailsList;
        this.currentContext = currentContext;
    }

    // Method to create ViewHolder
    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    // Method to bind data to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SectionDetails sectionDetails = sectionDetailsList.get(position);
        holder.bindData(sectionDetails);

        // Click listener for the Add New Section button
        holder.AddNewSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSection();
            }
        });

        // TextWatcher to update section title in the list
        holder.sectionTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                sectionDetailsList.get(position).setSectionHeading(s.toString());
            }
        });

        // Click listener for the Remove Section button
        holder.RemoveSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSection(position);
            }
        });

        // Setting up the details RecyclerView for the current section
        DetailsAdapter adapter = new DetailsAdapter(sectionDetailsList.get(position).getSectionDetails());
        holder.detailsRecyclerView.setLayoutManager(new LinearLayoutManager(currentContext));
        holder.detailsRecyclerView.setAdapter(adapter);
    }

    // Return the size of the section list
    @Override
    public int getItemCount() {
        return sectionDetailsList.size();
    }

    // Method to add a new section
    public void addSection() {
        ArrayList<Details> AllDetails = new ArrayList<>();
        Details DefaultDetail = new Details(); // Create a default detail object
        AllDetails.add(DefaultDetail);

        SectionDetails newSection = new SectionDetails("", AllDetails); // Create a new section with empty title and default detail
        sectionDetailsList.add(newSection);
        notifyItemInserted(sectionDetailsList.size() - 1);
    }

    // Method to remove a section at a specific position
    public void removeSection(int position) {
        if (position >= 0 && position < sectionDetailsList.size()) {
            sectionDetailsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    // Method to get a specific item from the section list
    public SectionDetails getItem(int i) {
        return sectionDetailsList.get(i);
    }

    // ViewHolder class to hold references to the views
    public class SectionViewHolder extends RecyclerView.ViewHolder {
        private EditText sectionTitleEditText; // EditText for section title
        public RecyclerView detailsRecyclerView; // RecyclerView for the details
        public Button AddNewSection; // Button to add a new section
        public Button RemoveSection; // Button to remove the current section

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTitleEditText = itemView.findViewById(R.id.edit_heading);
            detailsRecyclerView = itemView.findViewById(R.id.recycler_view_details);
            AddNewSection = itemView.findViewById(R.id.button_add_section);
            RemoveSection = itemView.findViewById(R.id.button_remove_section);
        }

        // Method to bind data to the views
        public void bindData(SectionDetails sectionDetails) {
            sectionTitleEditText.setText(sectionDetails.getSectionHeading());

            // Ensure section has at least one detail
            if (sectionDetails.getSectionDetails().isEmpty()) {
                Details DefaultDetail = new Details();
                ArrayList<Details> AllDetails = new ArrayList<>();
                AllDetails.add(DefaultDetail);
                sectionDetails.setSectionDetails(AllDetails);
            }

            // Set up the details adapter for the details RecyclerView
            DetailsAdapter detailsAdapter = new DetailsAdapter(sectionDetails.getSectionDetails());
            detailsRecyclerView.setAdapter(detailsAdapter);
        }
    }
}
