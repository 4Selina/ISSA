package com.changshi.issa.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.DatabaseHandler.Details;
import com.changshi.issa.R;

import java.util.List;

public class DisplayDetailsAdapter extends RecyclerView.Adapter<DisplayDetailsAdapter.ViewHolder> {
    private List<Details> detailsList;

    public DisplayDetailsAdapter(List<Details> detailsList) {
        this.detailsList = detailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Details details = detailsList.get(position);
        holder.detailsTv.setText(details.getDetail());
        holder.linkTv.setText(details.getLink());
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    public void setDetailsList(List<String> details) {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView detailsTv;
        TextView linkTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            detailsTv = itemView.findViewById(R.id.detailsTv);
            linkTv = itemView.findViewById(R.id.linkTv);
        }
    }
}
