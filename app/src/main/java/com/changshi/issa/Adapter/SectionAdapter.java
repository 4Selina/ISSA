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
    private List<SectionDetails> sectionDetailsList;
    private Context currentContext;

    public SectionAdapter(List<SectionDetails> sectionDetailsList, Context currentContext) {
        this.sectionDetailsList = sectionDetailsList;
        this.currentContext = currentContext;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SectionDetails sectionDetails = sectionDetailsList.get(position);
        holder.bindData(sectionDetails);

        holder.AddNewSection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addSection();
            }
        });

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

        holder.RemoveSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSection(position);
            }
        });

        DetailsAdapter adapter = new DetailsAdapter(sectionDetailsList.get(position).getSectionDetails());

        holder.detailsRecyclerView.setLayoutManager(new LinearLayoutManager(currentContext));
        holder.detailsRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return sectionDetailsList.size();
    }

    public void addSection()
    {
        ArrayList<Details> AllDetails = new ArrayList<>();
        Details DefaultDetail = new Details();

        AllDetails.add(DefaultDetail);

        SectionDetails newSection = new SectionDetails("", AllDetails);

        sectionDetailsList.add(newSection);
        notifyItemInserted(sectionDetailsList.size() -1);
    }

    public void removeSection(int position) {
        if (position >= 0 && position < sectionDetailsList.size()) {
            sectionDetailsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public SectionDetails getItem(int i) {
        return sectionDetailsList.get(i);
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {
        private EditText sectionTitleEditText;
        public RecyclerView detailsRecyclerView;
        public Button AddNewSection;
        public Button RemoveSection;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTitleEditText = itemView.findViewById(R.id.edit_heading);
            detailsRecyclerView = itemView.findViewById(R.id.recycler_view_details);
            AddNewSection = itemView.findViewById(R.id.button_add_section);
            RemoveSection = itemView.findViewById(R.id.button_remove_section);
        }

        public void bindData(SectionDetails sectionDetails) {
            sectionTitleEditText.setText(sectionDetails.getSectionHeading());
            DetailsAdapter detailsAdapter = new DetailsAdapter(sectionDetails.getSectionDetails());
            detailsRecyclerView.setAdapter(detailsAdapter);
        }
    }
}
