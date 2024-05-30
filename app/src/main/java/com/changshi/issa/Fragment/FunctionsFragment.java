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
import com.changshi.issa.BackPressHandler;
import com.changshi.issa.DatabaseHandler.Functions;
import com.changshi.issa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FunctionsFragment extends Fragment implements BackPressHandler
{
    RecyclerView functionRecyclerView;

    private ArrayList<Functions> AllFunctions;
    private String FragmentTitle;

    public FunctionsFragment(ArrayList<Functions> allFunctions) { AllFunctions = allFunctions; }

    public FunctionsFragment()
    {
    }

    public String getFragmentTitle() { return FragmentTitle; }

    public void setFragmentTitle(String Title) { FragmentTitle = Title; }

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
                                Object learningSupportUrl = SelectedSnapshot.get("LearningSupportUrl");
                                if(learningSupportUrl != null && !Strings.isNullOrEmpty(learningSupportUrl.toString()))
                                    AllFunctions.get(0).setFunctionURL(learningSupportUrl.toString());

                                Object socialActivitiesUrl = SelectedSnapshot.get("SocialActivitiesUrl");
                                if(socialActivitiesUrl != null && !Strings.isNullOrEmpty(socialActivitiesUrl.toString()))
                                    AllFunctions.get(1).setFunctionURL(socialActivitiesUrl.toString());

                                Object accommodationUrl = SelectedSnapshot.get("AccommodationUrl");
                                if(accommodationUrl != null && !Strings.isNullOrEmpty(accommodationUrl.toString()))
                                    AllFunctions.get(2).setFunctionURL(accommodationUrl.toString());

                                Object transportUrl = SelectedSnapshot.get("TransportUrl");
                                if(transportUrl != null && !Strings.isNullOrEmpty(transportUrl.toString()))
                                    AllFunctions.get(3).setFunctionURL(transportUrl.toString());

                                Object jobSupportUrl = SelectedSnapshot.get("JobSupportUrl");
                                if(jobSupportUrl != null && !Strings.isNullOrEmpty(jobSupportUrl.toString()))
                                    AllFunctions.get(4).setFunctionURL(jobSupportUrl.toString());

                                functionRecyclerView.getAdapter().notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }
}