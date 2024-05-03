package com.changshi.issa.Fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changshi.issa.Adapter.WebpageAdapter;
import com.changshi.issa.DatabaseHandler.WebpageItem;
import com.changshi.issa.HomeActivity;
import com.changshi.issa.R;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchSFragment extends Fragment {

    private EditText searchEditText;
    private Button searchButton;
    private ImageView backBtn;
    private RecyclerView searchView;
    private FirebaseFirestore db;
    private List<WebpageItem> dataList = new ArrayList<>();
    private WebpageAdapter adapter;

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
        backBtn = view.findViewById(R.id.backIcon);
        searchView = view.findViewById(R.id.searchRecyclerView);
        db = FirebaseFirestore.getInstance();


//        // Initialize your adapter
//        adapter = new WebpageAdapter(getActivity(), webpageItems, WebpageFragment.this);
        searchView.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHomeActivity();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString();
                if (!query.isEmpty()) {
//                    performSearch(query);
                } else {
                    Toast.makeText(getActivity(), "Please enter a search term", Toast.LENGTH_SHORT).show();
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

//    private void performSearch(String query) {
//        // Query the Firestore database
//        db.collection("document")
//                .whereEqualTo("department", query)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                            dataList.clear();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                // Assuming your data is of type YourDataType
//                                YourDataType data = document.toObject(YourDataType.class);
//                                dataList.add(data);
//                            }
//                            adapter.notifyDataSetChanged();
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }
}