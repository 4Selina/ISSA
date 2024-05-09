package com.changshi.issa.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.DatabaseHandler.Supports;
import com.changshi.issa.R;

import java.util.ArrayList;

public class DisplaySectionAdapter extends RecyclerView.Adapter<DisplaySectionAdapter.ViewHolder> {
        private ArrayList<Supports.Section> sectionList;

        public DisplaySectionAdapter(ArrayList<Supports.Section> sectionList) {
            this.sectionList = sectionList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_section, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Supports.Section section = sectionList.get(position);
            holder.headingTv.setText(section.getHeading());
            holder.displayDetailsAdapter.setDetailsList(section.getDetails());
            holder.displayDetailsAdapter.notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return sectionList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView headingTv;
            RecyclerView rvDisplayDetails;
            DisplayDetailsAdapter displayDetailsAdapter;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                headingTv = itemView.findViewById(R.id.headingTv);
                rvDisplayDetails = itemView.findViewById(R.id.rv_display_details);
                rvDisplayDetails.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
                displayDetailsAdapter = new DisplayDetailsAdapter(new ArrayList<>());
                rvDisplayDetails.setAdapter(displayDetailsAdapter);
            }
        }
    }