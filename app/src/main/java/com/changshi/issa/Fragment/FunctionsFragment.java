package com.changshi.issa.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changshi.issa.DatabaseHandler.Functions;
import com.changshi.issa.R;

import java.util.ArrayList;

import kotlin.Function;
import com.changshi.issa.Adapter.FunctionAdapter;

public class FunctionsFragment extends Fragment
{
    RecyclerView functionRecyclerView;

    private ArrayList<Functions> AllFunctions;

    public FunctionsFragment(ArrayList<Functions> allFunctions)
    {
        AllFunctions = allFunctions;
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
    }
}