package com.changshi.issa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    public EditText edtUsername;
    public EditText edtPasswordEditText;
    public Button btnLogin, btnSkip;

    private ArrayList<DocumentSnapshot> AllUsers = new ArrayList<>();

//    public TextView txtForgotPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
          ////review later !!!!////
        SharedPreferences Pref = getSharedPreferences("login_pref", MODE_PRIVATE);
        boolean IsLoggedIn = Pref.getBoolean("is_logged_in", false);

        if(IsLoggedIn)
        {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        }

        edtUsername = findViewById(R.id.edtUserName);
        edtPasswordEditText = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSkip = findViewById(R.id.btnSkip);


        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String username = edtUsername.getText().toString().trim();
                String password = edtPasswordEditText.getText().toString().trim();

                if (username.isEmpty())
                {
                    edtUsername.setError("Username is required");
                    edtUsername.requestFocus();
                    return;
                }

                if (password.isEmpty())
                {
                    edtPasswordEditText.setError("Password is required");
                    edtPasswordEditText.requestFocus();
                    return;
                }

                // Check user credentials against a database or web service
                // and start the dashboard activity if login is successful
                GetData();

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        boolean UserExists = false;

                        for(DocumentSnapshot SelectedDocument : AllUsers)
                        {
                            Object documentUsername = SelectedDocument.get("Username");
                            Object documentPassword = SelectedDocument.get("Password");

                            if (documentUsername != null && documentUsername.equals(username))
                            {
                                if(documentPassword != null && documentPassword.equals(password))
                                {
                                    UserExists = true;
                                }
                            }
                        }

                        if(UserExists)
                        {
                            // session management
                            SharedPreferences sharedPref = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putBoolean("is_logged_in", true);
                            editor.putString("username", username);
                            editor.apply();

                            Log.d("LoginActivity", "onClick: " + sharedPref.getBoolean("is_logged_in", false));

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 2000);
            }
        });
    }

    private void GetData()
    {
        // Check if the username and password match the admin credentials
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        AllUsers.addAll(task.getResult().getDocuments());
                    }
                });
    }
}
