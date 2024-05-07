package com.changshi.issa.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.Adapter.FunctionAdapter;
import com.changshi.issa.DatabaseHandler.Functions;
import com.changshi.issa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FunctionsFragment extends Fragment
{

    RecyclerView functionRecyclerView;

    private ArrayList<Functions> AllFunctions;

    public FunctionsFragment(ArrayList<Functions> allFunctions)
    {
        AllFunctions = allFunctions;
    }

    public FunctionsFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_functions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        functionRecyclerView = getView().findViewById(R.id.functionRecyclerView);

        FunctionAdapter adapter = new FunctionAdapter(getActivity(), AllFunctions);

        functionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        functionRecyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Settings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        for(DocumentSnapshot SelectedSnapshot : task.getResult().getDocuments())
                        {
                            if(SelectedSnapshot.contains("AccommodationUrl"))
                            {
                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("LearningSupportUrl").toString()))
                                    AllFunctions.get(0).setFunctionURL(SelectedSnapshot.get("LearningSupportUrl").toString());

                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("SocialActivitiesUrl").toString()))
                                    AllFunctions.get(1).setFunctionURL(SelectedSnapshot.get("SocialActivitiesUrl").toString());

                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("AccommodationUrl").toString()))
                                    AllFunctions.get(2).setFunctionURL(SelectedSnapshot.get("AccommodationUrl").toString());

                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("TransportUrl").toString()))
                                    AllFunctions.get(3).setFunctionURL(SelectedSnapshot.get("TransportUrl").toString());

                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("JobSupportUrl").toString()))
                                    AllFunctions.get(4).setFunctionURL(SelectedSnapshot.get("JobSupportUrl").toString());

                                functionRecyclerView.getAdapter().notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}