package com.changshi.issa.Adapter;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.DatabaseHandler.Details;
import com.changshi.issa.R;
import com.google.common.base.Strings;

import java.util.ArrayList;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {
    // List to hold Details objects
    private ArrayList<Details> CurrentDetails;

    // Method to set data and notify adapter of changes
    public void setData(ArrayList<Details> supportDetails) {
        this.CurrentDetails = supportDetails;
        notifyDataSetChanged();
    }

    // Constructor for the adapter
    public DetailsAdapter(ArrayList<Details> supportDetails) {
        this.CurrentDetails = supportDetails;
    }

    // Method to create ViewHolder
    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details, parent, false);
        return new DetailsViewHolder(view);
    }

    // Method to bind data to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Details ThisDetail = CurrentDetails.get(position);
        holder.bindData(ThisDetail);

        // TextWatcher for DetailsText EditText
        holder.DetailsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                CurrentDetails.get(position).setDetail(s.toString());
            }
        });

        // TextWatcher for linkET EditText
        holder.linkET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                CurrentDetails.get(position).setLink(s.toString());
            }
        });

        // Set OnClickListener for AddDetails button
        holder.AddDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDetails();
            }
        });

        // Set OnClickListener for RemoveDetails button
        holder.RemoveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDetails(position);
            }
        });
    }

    // Method to add a new Details object to the list and notify the adapter
    public void addDetails() {
        CurrentDetails.add(new Details());
        notifyItemInserted(CurrentDetails.size() - 1);
    }

    // Method to remove a Details object from the list and notify the adapter
    public void removeDetails(int position) {
        if (CurrentDetails.size() > 1) {
            CurrentDetails.remove(CurrentDetails.get(position));
            notifyItemRemoved(position);
        }
    }

    // Method to return the size of the list
    @Override
    public int getItemCount() {
        if (CurrentDetails != null)
            return CurrentDetails.size();
        else
            return 0;
    }

    // ViewHolder class to hold and manage view elements
    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        private EditText DetailsText;
        private EditText linkET;
        public Button AddDetails;
        public Button RemoveDetails;

        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            DetailsText = itemView.findViewById(R.id.edit_detail_text);
            linkET = itemView.findViewById(R.id.linkET);
            AddDetails = itemView.findViewById(R.id.button_add_detail);
            RemoveDetails = itemView.findViewById(R.id.button_remove_detail);
        }

        // Method to bind data to view elements
        public void bindData(Details details) {
            DetailsText.setText(details.getDetail());

            if (!Strings.isNullOrEmpty(details.getLink()))
                linkET.setText(details.getLink());
        }
    }
}
