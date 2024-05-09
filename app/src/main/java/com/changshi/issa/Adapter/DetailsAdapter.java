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

import java.util.ArrayList;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {
    private ArrayList<Details> CurrentDetails;

    public void setData(ArrayList<Details> supportDetails)
    {
        this.CurrentDetails = supportDetails;
        notifyDataSetChanged();
    }

    public DetailsAdapter(ArrayList<Details> supportDetails) {
        this.CurrentDetails = supportDetails;
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details, parent, false);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Details ThisDetail = CurrentDetails.get(position);
        holder.bindData(ThisDetail);

        holder.DetailsText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                CurrentDetails.get(position).setDetail(s.toString());
            }
        });

        holder.linkET.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                CurrentDetails.get(position).setLink(s.toString());
            }
        });

        holder.AddDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDetails();
            }
        });

        holder.RemoveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                removeDetails(position);
            }
        });
    }

    public void addDetails() {
        CurrentDetails.add(new Details());
        notifyItemInserted(CurrentDetails.size() - 1);
    }

    public void removeDetails(int position)
    {
        if(CurrentDetails.size() > 1)
        {
            CurrentDetails.remove(CurrentDetails.get(position));
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        if(CurrentDetails != null)
            return CurrentDetails.size();
        else
            return 0;
    }

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

        public void bindData(Details details) {
            DetailsText.setText(details.getDetail());
        }
    }

}
