package com.changshi.issa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.Adapter.WebpageAdapter;
import com.changshi.issa.DatabaseHandler.WebpageItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UpdateWebpageActivity extends AppCompatActivity {

    private Button mBtnSubmit;

        private RecyclerView mRecyclerView;
        private WebpageAdapter mAdapter;
        private FirebaseFirestore mFirestore;
        private ArrayList<WebpageItem> mItems;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_update_webpage);

            mRecyclerView = findViewById(R.id.recycler_view_webpage);
            mFirestore = FirebaseFirestore.getInstance();

            mItems = new ArrayList<>();
            mAdapter = new WebpageAdapter(mItems, mFirestore);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mAdapter);

            loadDataFromFirestore();
        }

        private void loadDataFromFirestore() {
            // Load data from Firestore and populate mItems
            // For example:
            mFirestore.collection("webpageItems").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        WebpageItem item = document.toObject(WebpageItem.class);
                        if (item != null) {
                            mItems.add(item);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        }


    }
