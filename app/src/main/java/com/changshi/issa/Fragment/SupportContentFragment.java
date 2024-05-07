package com.changshi.issa.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.AddActivity;
import com.changshi.issa.DatabaseHandler.Details;
import com.changshi.issa.DatabaseHandler.SectionDetails;
import com.changshi.issa.DatabaseHandler.Supports;
import com.changshi.issa.R;
import com.changshi.issa.SupportContent;
import com.google.common.base.Strings;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SupportContentFragment extends Fragment {

    private ImageView bannerImgV;
    private Supports selectedSupport;
    private TextView descriptionTv;
    private RecyclerView rVSection;
    private TextView conclusionTv;
    private Button buttonEdit;

    private ProgressDialog pd;

    private List<SupportContent> supportContentList;
    FirebaseFirestore db;

    public SupportContentFragment(Supports SelectedSupport)
    {
        this.selectedSupport = SelectedSupport;
    }

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support_content, container, false);

        db = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(getContext());
        pd.setTitle("Loading Data...");

        //set recyclerView
        rVSection = view.findViewById(R.id.recycler_view_sections);

        bannerImgV = view.findViewById(R.id.image_banner);
//        //display title in support card view???????

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

    private void LoadData()
    {
        if(!Strings.isNullOrEmpty(selectedSupport.getBannerUrl()))
        {
            Picasso.get().load(selectedSupport.getBannerUrl()).into(bannerImgV);
        }

        descriptionTv.setText(selectedSupport.getDescription());

        for (SectionDetails SelectedSection : selectedSupport.getSections())
        {
            // Do this for Each Item in Sections.

            for (Details SelectedDetail : SelectedSection.getSectionDetails())
            {
                // Do this for each item in Details.
            }
        }

        conclusionTv.setText(selectedSupport.getConclusion());
    }
}
