package com.changshi.issa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {


    private EditText edtContentId, edtContentTitle, edtImageURL, edtContentDetails;
    private Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Initialize views
        edtContentId = findViewById(R.id.edtContentId);
        edtContentTitle = findViewById(R.id.edtContentTitle);
        edtImageURL = findViewById(R.id.edtImageURL);
        edtContentDetails = findViewById(R.id.edtContentDetails);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);

        // Set click listeners
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AddActivity.this, "Content added!", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set admin login status to true
                SharedPreferences sharedPref = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("is_logged_in", true);
                editor.apply();

                // Start HomeActivity
                Intent intent = new Intent(AddActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}