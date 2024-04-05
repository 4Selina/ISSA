package com.changshi.issa.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.changshi.issa.HomeActivity;
import com.changshi.issa.R;

import java.io.ByteArrayOutputStream;

public class WebpageFragment extends Fragment {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 2;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView contentsTextView;
    private EditText editTitle;
    private EditText editContents;
    private Button btnChangeImage;
    private Button btnSave;

    private HomeActivity ActivityInstance;

    public WebpageFragment() {
        // Required empty public constructor
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_webpage, container, false);

        ActivityInstance = (HomeActivity)getActivity();

        imageView = view.findViewById(R.id.imgWebpage);
        titleTextView = view.findViewById(R.id.txtWebTitle);
        contentsTextView = view.findViewById(R.id.txtWebContent);
        editTitle = view.findViewById(R.id.editWebTitle);
        editContents = view.findViewById(R.id.editWebContents);
        btnChangeImage = view.findViewById(R.id.btnChangeWebImage);
        btnSave = view.findViewById(R.id.btnSave);

        // Set initial values
        imageView.setImageResource(R.drawable.logo);
        titleTextView.setText("Whitireia WelTec");
        contentsTextView.setText(getString(R.string.contactUs));

        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a dialog for picking an image
                showImagePickerDialog();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save changes
                String newTitle = editTitle.getText().toString();
                String newContents = editContents.getText().toString();

                // Update title and contents TextViews
                titleTextView.setText(newTitle);
                contentsTextView.setText(newContents);

                // You can also save the new title and contents to your database or shared preferences
            }
        });

        // Check the Home Activity to see if it has been Logged In.
        if(!ActivityInstance.IsLoggedIn)
        {
            // Hide all the Elements from a non Logged In User.
            editTitle.setVisibility(View.GONE);
            editContents.setVisibility(View.GONE);

            btnChangeImage.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
        }

        return view;
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick Image")
                .setItems(new CharSequence[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Pick image from gallery
                                pickImageFromGallery();
                                break;
                            case 1:
                                // Capture image from camera
                                captureImageFromCamera();
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

    private void captureImageFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_IMAGE && data != null) {
                // Image picked from gallery
                Uri selectedImage = data.getData();
                setImage(selectedImage);
            } else if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && data != null) {
                // Image captured from camera
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri imageUri = getImageUri(requireContext(), photo);
                setImage(imageUri);
            }
        }
    }
    public void setImage(Uri imageUri) {
        imageView.setImageURI(imageUri);
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
}