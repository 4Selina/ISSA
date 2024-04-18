package com.changshi.issa.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.view.ActionBarPolicy;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.changshi.issa.HomeActivity;
import com.changshi.issa.R;

import java.io.ByteArrayOutputStream;

public class WebpageFragment extends Fragment {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 2;
    private ImageView imgWebpage, edtWebpageLogo;
    private TextView contentsTextView;
    private Button btnUpdateWebpage;

    private HomeActivity ActivityInstance;

    private FragmentManager mFragmentManager;

    public WebpageFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_webpage, container, false);

        ActivityInstance = (HomeActivity) getActivity();

        imgWebpage = view.findViewById(R.id.imgWebpage);
        contentsTextView = view.findViewById(R.id.txtWebpageContent);
        btnUpdateWebpage = view.findViewById(R.id.btnUpdateWebpage);
        edtWebpageLogo = view.findViewById(R.id.edtWebpageLogo);

        // Set initial values
        imgWebpage.setImageResource(R.drawable.logo);
        contentsTextView.setText(getString(R.string.WelTecContent));

        // Set the formatted text
        setFormattedText();

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

                private void pickImageFromGallery() {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, REQUEST_CODE_PICK_IMAGE);
                }

                private void showUrlInputDialog() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Enter Image URL");

                    // Set up the input
                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String imageUrl = input.getText().toString();
                            // Update the image using the URL
                            updateImageFromUrl(imageUrl);
                        }

                        private void updateImageFromUrl(String imageUrl)
                        {

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }

        });
        btnUpdateWebpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit Webpage Contents");

                // Inflate the dialog layout
                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update_webpage, null);
                builder.setView(view);

                // Get references to dialog views
                TextView dialogContentsTextView = view.findViewById(R.id.dialog_webContents_text_view);
                EditText editContents = view.findViewById(R.id.dialog_edit_webContents_edit_text);
                Button btnSaveChanges = view.findViewById(R.id.dialog_btn_save_webChanges);
                Button btnCancelChanges = view.findViewById(R.id.dialog_btn_cancel_webChanges);

                // Set current contents in the dialog
                String currentContents = contentsTextView.getText().toString();
                editContents.setText(currentContents);

                // Show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();

                // Save changes when the Save Changes button is clicked
                btnSaveChanges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newContents = editContents.getText().toString();
                        contentsTextView.setText(newContents);
                        // You can also save the new contents to a database or shared preferences here
                        dialog.dismiss(); // Dismiss the dialog
                    }
                });

                // Cancel changes when the Cancel Changes button is clicked
                btnCancelChanges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder cancelBuilder = new AlertDialog.Builder(getContext());
                        cancelBuilder.setTitle("Cancel Changes");
                        cancelBuilder.setMessage("Are you sure you want to cancel the changes?");

                        // Add the buttons
                        cancelBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked Yes button
                                // Navigate to the webpage fragment
                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, new WebpageFragment());
                                transaction.commit();
                            }
                        });
                        cancelBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked No button
                                // Do nothing, stay in the dialog
                            }
                        });

                        // Create and show the AlertDialog
                        AlertDialog cancelDialog = cancelBuilder.create();
                        cancelDialog.show();
                    }
                });
            }
        });


        // Check the Home Activity to see if it has been Logged In.
        if (!ActivityInstance.IsLoggedIn) {
            // Hide all the Elements from a non Logged In User.

            edtWebpageLogo.setVisibility(View.GONE);
            btnUpdateWebpage.setVisibility(View.GONE);
        }

        return view;
    }

    private void setFormattedText() {
        String welTecDetails = getString(R.string.WelTecContent);
        String[] paragraphs = welTecDetails.split("\n\n");

        SpannableStringBuilder builder = new SpannableStringBuilder();
        int start = 0;
        for (int i = 0; i < paragraphs.length; i++) {
            String paragraph = paragraphs[i];
            if (paragraph.contains(":")) { // Assuming this indicates a section title
                builder.append(paragraph).append("\n\n");
                start += paragraph.length() + 2;
            } else {
                builder.append(paragraph).append("\n");
                if (i % 2 == 0) {
                    // Apply Teal 700 color to even lines
                    builder.setSpan(new ForegroundColorSpan(Color.parseColor("#00796B")), start, start + paragraph.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                start += paragraph.length() + 1;
            }
        }

        // Set the formatted text to your TextView
        contentsTextView.setText(builder);
    }



}