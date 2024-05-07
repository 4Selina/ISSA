package com.changshi.issa.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.Adapter.WebpageAdapter;
import com.changshi.issa.DatabaseHandler.WebpageItem;
import com.changshi.issa.R;
import com.changshi.issa.UpdateWebpageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WebpageFragment extends Fragment {

    public static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final String TAG = "WebpageFragment";
    private Context context;
    private ArrayList<WebpageItem> webpageItems;
    private ImageView imgWebpage;
    private ImageButton btnWebLogo;
    private Button btnUpdateWebpage;
    private RecyclerView webpageRV;
    private WebpageAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db;

    private ProgressDialog pd;
    public WebpageFragment(ArrayList<WebpageItem> webpageItems, Context context, FirebaseFirestore db)
    {
        this.webpageItems = webpageItems;
        this.context = context;
        this.db = db;
    }

    public WebpageFragment()
    {

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_webpage, container, false);

        db = FirebaseFirestore.getInstance();

        // Initialize layoutManager
        layoutManager = new LinearLayoutManager(getActivity());
        //set recyclerView
        webpageRV = view.findViewById(R.id.RV_Webpage_Content);
        webpageRV.setHasFixedSize(true);
        webpageRV.setLayoutManager(new LinearLayoutManager(getContext()));
        webpageRV.setAdapter(adapter);
        //set image and image button
        imgWebpage = (ImageView) view.findViewById(R.id.imgWebpage);

        btnWebLogo = view.findViewById(R.id.edtWebpageLogo);
        btnUpdateWebpage = view.findViewById(R.id.btnUpdateWebpage); ;

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);

        //hide edit buttons when no login
        SharedPreferences Pref = getActivity().getSharedPreferences("login_pref", MODE_PRIVATE);
        boolean IsLoggedIn = Pref.getBoolean("is_logged_in", false);

        if (!IsLoggedIn) {
            btnWebLogo.setVisibility(View.GONE);
            btnUpdateWebpage.setVisibility(View.GONE);
        }

//        pd = new ProgressDialog(getActivity());

        //retrieve data
        showData();
        loadImage();

        btnWebLogo.setOnClickListener(new View.OnClickListener() {

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

                        // Save the image URL to Firestore
                        saveImageUrlToFirestore(imageUrl);
                        // Update the image using the URL
                        updateImageFromUrl(imageUrl);

                    }

                    private void updateImageFromUrl(String imageUrl)
                    {
                        if(!Strings.isNullOrEmpty(imageUrl))
                            Picasso.get().load(imageUrl).into(imgWebpage);
                        else
                            imgWebpage.setImageResource(R.drawable.logo);
                    }

                    private void saveImageUrlToFirestore(String imageUrl)
                    {
                        Map<String, Object> data = new HashMap<>();
                        data.put("imageUrl", imageUrl);
                        db.collection("Documents").document("imageUrl")
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
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
                Intent intent = new Intent(getActivity(), UpdateWebpageActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    private void loadImage()
    {
        db.collection("Documents").document("imageUrl")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists())
                            {
                                String imageUrl = document.getString("imageUrl");

                                if(!Strings.isNullOrEmpty(imageUrl))
                                    Picasso.get().load(imageUrl).into(imgWebpage);
                                else
                                    imgWebpage.setImageResource(R.drawable.logo);
                            }
                            else
                            {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void showData()
    {
        //set title of progress dialog
        pd.setTitle("Loading Data...");
        pd.show();
        db.collection("Documents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        pd.dismiss();
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                    // Load image from Firestore
                                   // WebpageItem webpageItem = new WebpageItem(document.get)
                                    Map<String,Object> map = document.getData();

                                    Object imageUrl = map.get("imageUrl");

                                    if (null != imageUrl)
                                    {
                                        String imageUrlstr = imageUrl.toString();

                                        if(!Strings.isNullOrEmpty(imageUrlstr))
                                            Picasso.get().load(imageUrlstr).into(imgWebpage);
                                        else
                                            imgWebpage.setImageResource(R.drawable.logo);
                                    }
                                    else
                                    {
                                        Picasso.get().load(R.drawable.logo).into(imgWebpage);
                                        // Load text data into RecyclerView
                                        if (map != null && !map.isEmpty())
                                        {
                                            String id = (String) map.get("id");
                                            String department = (String) map.get("department");
                                            String email = (String) map.get("email");
                                            String contact = (String) map.get("contact");
                                            String address = (String) map.get("address");
                                            WebpageItem webpageItem = new WebpageItem(id, department, email, contact, address);
                                            webpageItems.add(webpageItem);
                                        }
                                    }
                                }
                                // Set adapter for RecyclerView
                                adapter = new WebpageAdapter(getActivity(), webpageItems, context);
                                webpageRV.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }
//    public void deleteData(int index) {
//        //set title of progress dialog
//        pd.setTitle("Deleting Data...");
//        pd.show();
//        db.collection("Documents").document(webpageItems.get(index).getId())
//                .delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        //this will be called when data is added successfully
//                        pd.dismiss();
//                        Toast.makeText(getActivity(), "Uploaded...", Toast.LENGTH_SHORT).show();
//                   showData();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        //this will be called if there is any error while uploading
//                        pd.dismiss();
//                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

}