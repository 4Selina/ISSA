package com.changshi.issa.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.R;
import com.changshi.issa.SupportContent;

import java.util.List;

public class SupportContentAdapter extends RecyclerView.Adapter<SupportContentAdapter.SupportContentViewHolder> {
    private List<SupportContent> supportContentList;

    public SupportContentAdapter(List<SupportContent> supportContentList) {
        this.supportContentList = supportContentList;
    }

    @NonNull
    @Override
    public SupportContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_support_content, parent, false);
        return new SupportContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupportContentViewHolder holder, int position) {
        SupportContent supportContent = supportContentList.get(position);
        holder.bindData(supportContent);
    }

    @Override
    public int getItemCount() {
        return supportContentList.size();
    }

    public static class SupportContentViewHolder extends RecyclerView.ViewHolder {
        private TextView supportContentTitleTextView;
        private TextView supportContentDetailsTextView;

        public SupportContentViewHolder(@NonNull View itemView) {
            super(itemView);
            supportContentTitleTextView = itemView.findViewById(R.id.text_description);
            supportContentDetailsTextView = itemView.findViewById(R.id.text_conclusion);
        }

        public void bindData(SupportContent supportContent) {
            supportContentTitleTextView.setText(supportContent.getTitle());
            supportContentDetailsTextView.setText(supportContent.getDetails().toString());
        }
    }
}
