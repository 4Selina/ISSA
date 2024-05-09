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
import com.changshi.issa.DatabaseHandler.Details;
import com.changshi.issa.DatabaseHandler.SectionDetails;
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

        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        backBtn = view.findViewById(R.id.backIcon);
        searchView = view.findViewById(R.id.searchRecyclerView);
        db = FirebaseFirestore.getInstance();

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                navigateToHomeActivity();
            }
        });

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

    private void navigateToHomeActivity()
    {
        // Start HomeActivity with add and logout icons
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void performSearch(String query)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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

                            NewSupports.setId((Long)SelectedDocument.get("id"));
                            NewSupports.setDocumentID(SelectedDocument.getReference().getId());
                            NewSupports.setTitle(SelectedDocument.get("title").toString());

                            if(NewSupports.getTitle().toLowerCase().contains(query.toLowerCase()))
                            {
                                NewSupports.setDescription(SelectedDocument.get("description").toString());

                                if(SelectedDocument.contains("bannerUrl"))
                                {
                                    NewSupports.setBannerUrl(SelectedDocument.get("bannerUrl").toString());
                                }

                                NewSupports.setParentCategory(SelectedDocument.get("parentCategory").toString());

                                // Get the Sections.
                                ArrayList<Long> SectionIDs= (ArrayList<Long>)SelectedDocument.get("sections");

                                db.collection("Sections")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                                            {
                                                ArrayList<SectionDetails> AllSections = new ArrayList<>();

                                                for (DocumentSnapshot SelectedSection : task.getResult().getDocuments())
                                                {
                                                    boolean IDIsCorrect = false;

                                                    for (Long SelectedID : SectionIDs)
                                                    {
                                                        if(SelectedID == (Long) SelectedSection.get("id"))
                                                        {
                                                            IDIsCorrect = true;
                                                        }
                                                    }

                                                    if(IDIsCorrect)
                                                    {
                                                        SectionDetails NewSection = new SectionDetails();

                                                        NewSection.setID((Long)SelectedSection.get("id"));
                                                        NewSection.setDocumentID(SelectedSection.getReference().getId());
                                                        NewSection.setSectionHeading(SelectedSection.get("heading").toString());

                                                        ArrayList<Long> DetailsIDs = (ArrayList<Long>)SelectedSection.get("details");

                                                        db.collection("Details")
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                                                {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                                                                    {
                                                                        ArrayList<Details> AllDetails = new ArrayList<>();

                                                                        for(DocumentSnapshot SelectedDetail : task.getResult().getDocuments())
                                                                        {
                                                                            boolean IsCorrectID = false;

                                                                            for(Long SelectedDetailID : DetailsIDs)
                                                                            {
                                                                                if(SelectedDetailID == (Long) SelectedDetail.get("id"))
                                                                                {
                                                                                    IsCorrectID = true;
                                                                                }
                                                                            }

                                                                            if(IsCorrectID)
                                                                            {
                                                                                Details NewDetail = new Details();

                                                                                NewDetail.setID((Long)SelectedDetail.get("id"));
                                                                                NewDetail.setDocumentID(SelectedDetail.getReference().getId());
                                                                                NewDetail.setDetail(SelectedDetail.get("detail").toString());

                                                                                if(SelectedDetail.contains("link"))
                                                                                    NewDetail.setLink(SelectedDetail.getString("link"));

                                                                                AllDetails.add(NewDetail);
                                                                            }
                                                                        }

                                                                        NewSection.setSectionDetails(AllDetails);
                                                                    }
                                                                });

                                                        AllSections.add(NewSection);
                                                    }
                                                }

                                                NewSupports.setSections(AllSections);
                                            }
                                        });

                                if(SelectedDocument.contains("conclusion"))
                                {
                                    NewSupports.setConclusion(SelectedDocument.get("conclusion").toString());
                                }

                                AllSupports.add(NewSupports);
                            }
                        }

                        if(AllSupports.isEmpty())
                        {
                            Toast.makeText(getActivity(), "No result", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            SupportAdapter mAdapter = new SupportAdapter(getActivity(), AllSupports);

                            searchView.setLayoutManager(new LinearLayoutManager(getContext()));
                            searchView.setAdapter(mAdapter);
                        }
                    }
                });
    }
}