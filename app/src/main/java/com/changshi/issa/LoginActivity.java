package com.changshi.issa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public EditText edtUsername;
    public EditText edtPasswordEditText;
    public Button btnLogin, btnSkip;

    public TextView txtForgotPassword;

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
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Please contact the International Department to request the admin password.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Do nothing, just close the dialog
                            }
                        });
                // Create the AlertDialog object and show it
                builder.create().show();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPasswordEditText.getText().toString().trim();

                if (username.isEmpty()) {
                    edtUsername.setError("Username is required");
                    edtUsername.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    edtPasswordEditText.setError("Password is required");
                    edtPasswordEditText.requestFocus();
                    return;
                }

                // Check user credentials against a database or web service
                // and start the dashboard activity if login is successful
                if (authenticateUser(username, password)) {
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
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean authenticateUser(String username, String password) {
        // Check if the username and password match the admin credentials
        return username.equals("admin") && password.equals("admin");
    }
}
