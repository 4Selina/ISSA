package com.changshi.issa.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changshi.issa.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.R;

public class SearchSFragment extends Fragment {

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView searchRecyclerView;

    public SearchSFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_s, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString();
                performSearch(query);
            }
        });

        return view;
    }

    private void performSearch(String query) {
        // Perform the search operation here
        // can use the query to search for items and update the RecyclerView accordingly
        // For simplicity, let's just show a Toast message with the search query
        Toast.makeText(getContext(), "Searching for: " + query, Toast.LENGTH_SHORT).show();
    }
}
