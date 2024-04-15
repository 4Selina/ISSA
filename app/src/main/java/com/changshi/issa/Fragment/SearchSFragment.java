package com.changshi.issa.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changshi.issa.AddActivity;
import com.changshi.issa.HomeActivity;
import com.changshi.issa.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.R;

public class SearchSFragment extends Fragment {

    private EditText searchEditText;
    private Button searchButton;
    private ImageView imageView;
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
        imageView = view.findViewById(R.id.backIcon);
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHomeActivity();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if user is logged in
                SharedPreferences sharedPref = getActivity().getSharedPreferences("login_pref", Context.MODE_PRIVATE);
                boolean isLoggedIn = sharedPref.getBoolean("is_logged_in", false);

                if (isLoggedIn) {
                    navigateToHomeActivity();
                } else {
                    // Start HomeActivity without add and logout icons
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra("hide_icons", true);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        return view;
    }

    private void navigateToHomeActivity() {
        // Start HomeActivity with add and logout icons
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    private void performSearch(String query) {
        // Perform the search operation here
        // can use the query to search for items and update the RecyclerView accordingly
        // For simplicity, let's just show a Toast message with the search query
        Toast.makeText(getContext(), "Searching for: " + query, Toast.LENGTH_SHORT).show();
    }
}
