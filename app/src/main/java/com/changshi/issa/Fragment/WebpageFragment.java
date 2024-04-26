package com.changshi.issa.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.Adapter.WebpageAdapter;
import com.changshi.issa.AddActivity;
import com.changshi.issa.DatabaseHandler.WebpageItem;
import com.changshi.issa.HomeActivity;
import com.changshi.issa.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WebpageFragment extends Fragment {

    public static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 2;
    private ImageView imgWebpage, edtWebpageLogo;
    private TextView contentsTextView;
    private Button btnUpdateWebpage;

    private HomeActivity ActivityInstance;

    private RecyclerView WebpageRV;

    public WebpageFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_webpage, container, false);

        ActivityInstance = (HomeActivity) getActivity();

        imgWebpage = view.findViewById(R.id.imgWebpage);
        btnUpdateWebpage = view.findViewById(R.id.btnUpdateWebpage);
        edtWebpageLogo = view.findViewById(R.id.edtWebpageLogo);

        WebpageRV = view.findViewById(R.id.RV_Webpage_Content);

        ArrayList<WebpageItem> DefaultArrayList = new ArrayList<>();
        WebpageItem DefaultItem = new WebpageItem();

        DefaultArrayList.add(DefaultItem);

        WebpageAdapter Adapter = new WebpageAdapter(DefaultArrayList, FirebaseFirestore.getInstance());

        WebpageRV.setLayoutManager(new LinearLayoutManager(getContext()));
        WebpageRV.setAdapter(Adapter);

        // Set initial values
        imgWebpage.setImageResource(R.drawable.logo);

        // Set the formatted text

        edtWebpageLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a dialog for picking an image
                showImagePickerDialog();
            }

            private void showImagePickerDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Image Source")
                        .setItems(new CharSequence[]{"Gallery", "URL"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        // Pick image from gallery
                                        pickImageFromGallery();
                                        break;
                                    case 1:
                                        // Show dialog to enter URL
                                        showUrlInputDialog();
                                        break;
                                }
                            }


                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }

            private void pickImageFromGallery()
            {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_CODE_PICK_IMAGE);
            }

            private void showUrlInputDialog()
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter Image URL");

                // Set up the input
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String imageUrl = input.getText().toString();
                        // Update the image using the URL
                        updateImageFromUrl(imageUrl);
                    }

                    private void updateImageFromUrl(String imageUrl)
                    {
                        Picasso.get().load(imageUrl).into(imgWebpage);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        btnUpdateWebpage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("ButtonClicked", "Update button clicked");
                // Create and show update webpage dialog

            }
        });

        return view;
    }
}