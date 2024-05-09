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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Details details = detailsList.get(position);
        holder.detailsTv.setText(details.getDetail());

        if(!Strings.isNullOrEmpty(details.getLink()))
        {
            holder.openLinkBn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent OpenLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(details.getLink()));
                    holder.itemView.getContext().startActivity(OpenLinkIntent);
                }
            });
        }
        else
        {
            holder.openLinkBn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount()
    {
        return detailsList.size();
    }

    public void setDetailsList(ArrayList<Details> detailsList)
    {
        // update adapter
        this.detailsList = detailsList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView detailsTv;
        Button openLinkBn;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            detailsTv = itemView.findViewById(R.id.detailsTv);
            openLinkBn = itemView.findViewById(R.id.openLinkBn);
        }
    }
}
