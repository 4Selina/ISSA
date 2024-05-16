package com.changshi.issa.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.Adapter.DisplaySectionAdapter;
import com.changshi.issa.AddActivity;
import com.changshi.issa.BackPressHandler;
import com.changshi.issa.DatabaseHandler.Supports;
import com.changshi.issa.R;
import com.google.common.base.Strings;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class SupportContentFragment extends Fragment implements BackPressHandler {

    private ImageView bannerImgV;
    private Supports selectedSupport;
    private TextView descriptionTv;
    private RecyclerView rVSection;
    private TextView conclusionTv;
    private Button buttonEdit;
    private Button buttonBack;
    public FirebaseFirestore db;

    public SupportContentFragment() {
        // Required empty public constructor
    }

    public SupportContentFragment(Supports SelectedSupport)
    {
        this.selectedSupport = SelectedSupport;
    }

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support_content, container, false);

        db = FirebaseFirestore.getInstance();

        //set recyclerView
        rVSection = view.findViewById(R.id.recycler_view_sections);

        bannerImgV = view.findViewById(R.id.image_banner);

        descriptionTv = view.findViewById(R.id.text_description);
        conclusionTv = view.findViewById(R.id.text_conclusion);

        buttonEdit = view.findViewById(R.id.button_edit);

        // hide button with no login
        SharedPreferences Pref = getActivity().getSharedPreferences("login_pref", MODE_PRIVATE);
        boolean IsLoggedIn = Pref.getBoolean("is_logged_in", false);

        if (!IsLoggedIn)
        {
            buttonEdit.setVisibility(View.GONE);
        }

        //Click edit button for updating data in Add activity
        buttonEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), AddActivity.class);

                intent.putExtra("IsEditMode", true);
                intent.putExtra("SelectedSupport", selectedSupport);

                startActivity(intent);
            }
        });

        LoadData();
        return view;
    }


    //read and show data from Firestore

    private void LoadData()
    {
        if(selectedSupport != null && !Strings.isNullOrEmpty(selectedSupport.getBannerUrl()))
        {
            Picasso.get().load(selectedSupport.getBannerUrl()).into(bannerImgV);
        }

        else
        {
            // 如果 BannerUrl 为空，则显示默认图片
            bannerImgV.setImageResource(R.drawable.logo);
        }

        descriptionTv.setText(selectedSupport.getDescription());

        DisplaySectionAdapter adapter = new DisplaySectionAdapter(selectedSupport.getSections());

        rVSection.setLayoutManager(new LinearLayoutManager(getContext()));
        rVSection.setAdapter(adapter);

        //if conclusion is empty, it will be hidden.
        if (TextUtils.isEmpty(selectedSupport.getConclusion())) {
            conclusionTv.setVisibility(View.GONE);
        } else {
            conclusionTv.setVisibility(View.VISIBLE);
            conclusionTv.setText(selectedSupport.getConclusion());
        }
    }


    //go back to the previous fragment layout
    @Override
    public boolean handleBackPress() {
        // Let the system handle the back press
        return false;
    }
}
