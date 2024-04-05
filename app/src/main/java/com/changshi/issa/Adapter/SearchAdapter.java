package com.changshi.issa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context context;
    private List<String> searchResults;

    public SearchAdapter(Context context, List<String> searchResults) {
        this.context = context;
        this.searchResults = searchResults;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.bind(searchResults.get(position));
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public void setSearchResults(List<String> searchResults) {
        this.searchResults = searchResults;
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String text) {
            textView.setText(text);
        }
    }
}

