package com.changshi.issa.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.Adapter.SupportAdapter;
import com.changshi.issa.DatabaseHandler.Supports;
import com.changshi.issa.HomeActivity;
import com.changshi.issa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchSFragment extends Fragment {

    private EditText searchEditText;
    private Button searchButton;
    private ImageView backBtn;
    private RecyclerView searchView;
    private FirebaseFirestore db;

    public SearchSFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_s, container, false);

        // Initialize views
        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        backBtn = view.findViewById(R.id.backIcon);
        searchView = view.findViewById(R.id.searchRecyclerView);
        db = FirebaseFirestore.getInstance();

        // Back button click listener
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                navigateToHomeActivity();
            }
        });

        // Search button click listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString();

                if (!query.isEmpty())
                {
                    performSearch(query);
                }
                else
                {
                    Toast.makeText(getActivity(), "Please enter a search term", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    // Navigate to HomeActivity
    private void navigateToHomeActivity()
    {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    // Perform search based on the query
    private void performSearch(String query)
    {
        db.collection("Support_Contents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        ArrayList<Supports> AllSupports = new ArrayList<>();

                        for (DocumentSnapshot SelectedDocument : task.getResult().getDocuments())
                        {
                            Supports NewSupports = new Supports();

                            // Set Supports properties

                            // Check if title contains query
                            if(NewSupports.getTitle().toLowerCase().contains(query.toLowerCase()))
                            {
                                // Set other properties

                                // Retrieve and set sections

                                // Retrieve and set conclusion

                                AllSupports.add(NewSupports);
                            }
                        }

                        if(AllSupports.isEmpty())
                        {
                            Toast.makeText(getActivity(), "No result", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            // Display search results in RecyclerView
                            SupportAdapter mAdapter = new SupportAdapter(getActivity(), AllSupports);

                            searchView.setLayoutManager(new LinearLayoutManager(getContext()));
                            searchView.setAdapter(mAdapter);
                        }
                    }
                });
    }
}
