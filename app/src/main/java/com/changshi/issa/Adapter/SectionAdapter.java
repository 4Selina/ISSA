package com.changshi.issa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.DatabaseHandler.Details;
import com.changshi.issa.R;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {
    private List<SectionDetails> sectionDetailsList;
    private Context CurrentContext;

    public SectionAdapter(List<SectionDetails> sectionDetailsList, Context currentContext)
    {
        this.sectionDetailsList = sectionDetailsList;
        CurrentContext = currentContext;
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

        holder.AddNewSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSection();
            }
        });

        holder.RemoveSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSection(position);
            }
        });

        DetailsAdapter adapter = new DetailsAdapter(sectionDetailsList.get(position).getSectionDetails());

        holder.detailsRecyclerView.setLayoutManager(new LinearLayoutManager(CurrentContext));
        holder.detailsRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return sectionDetailsList.size();
    }

    public void addSection() {
        SectionDetails NewSection = new SectionDetails();

        Details DefaultDetail = new Details();

        ArrayList<Details> AllDetails = new ArrayList<>();
        AllDetails.add(DefaultDetail);

        NewSection.setSectionDetails(AllDetails);

        sectionDetailsList.add(NewSection);
        notifyItemInserted(sectionDetailsList.size() - 1);
    }

    public void removeSection(int position)
    {
        if(sectionDetailsList.size() > 1)
        {
            sectionDetailsList.remove(sectionDetailsList.get(position));
            notifyItemRemoved(position);
        }
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder
    {
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

        public void bindData(SectionDetails sectionDetails)
        {
            sectionTitleEditText.setText(sectionDetails.getSectionHeading());
            // 设置 detailsRecyclerView 的 Adapter 为 DetailsAdapter，传入当前部分的详细信息列表

            DetailsAdapter detailsAdapter = new DetailsAdapter(sectionDetails.getSectionDetails());
            detailsRecyclerView.setAdapter(detailsAdapter);
        }
    }
}
