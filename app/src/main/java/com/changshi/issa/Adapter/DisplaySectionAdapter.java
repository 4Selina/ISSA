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
    private ArrayList<SectionDetails> sectionList;

    public DisplaySectionAdapter(ArrayList<SectionDetails> sectionList)
    {
        this.sectionList = sectionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_section, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        SectionDetails section = sectionList.get(position);

        holder.headingTv.setText(section.getSectionHeading());

        holder.rvDisplayDetails.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        ArrayList<Details> details = section.getSectionDetails();
        ArrayList<Details> orderedDetails = (ArrayList)details.stream().sorted(Comparator.comparingLong(Details::getID)).collect(Collectors.toList());
        holder.displayDetailsAdapter = new DisplayDetailsAdapter(orderedDetails);
        holder.rvDisplayDetails.setAdapter(holder.displayDetailsAdapter);
    }

    public void updateSection(ArrayList<SectionDetails> sectionList)
    {
        this.sectionList = sectionList;
        notifyDataSetChanged();
    }

    @Override
        public int getItemCount() {
            return sectionList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView headingTv;
            RecyclerView rvDisplayDetails;
            DisplayDetailsAdapter displayDetailsAdapter;

            public ViewHolder(@NonNull View itemView)
            {
                super(itemView);

                headingTv = itemView.findViewById(R.id.headingTv);
                rvDisplayDetails = itemView.findViewById(R.id.rv_display_details);
            }
        }
    }