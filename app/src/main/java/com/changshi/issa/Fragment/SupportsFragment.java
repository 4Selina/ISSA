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

import com.changshi.issa.Adapter.SupportAdapter;
import com.changshi.issa.Adapter.SupportItemClickListener;
import com.changshi.issa.DatabaseHandler.Supports;
import com.changshi.issa.R;

import java.util.ArrayList;

public class SupportsFragment extends Fragment implements SupportItemClickListener {

    private RecyclerView mRecyclerView;
    private SupportAdapter mAdapter;
    private ArrayList<Supports> mSupportList;

    public SupportsFragment(ArrayList<Supports> allSupports) {
        this.mSupportList = allSupports;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_supports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View supportRecyclerView = getView().findViewById(R.id.supportRecyclerView);

        mRecyclerView = view.findViewById(R.id.supportRecyclerView);

        mAdapter = new SupportAdapter(getActivity(), mSupportList, this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onImageClick(int position) {

    }

    @Override
    public void onEditClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {

    }
}

