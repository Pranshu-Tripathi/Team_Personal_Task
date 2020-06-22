package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstInstallActivity extends AppCompatActivity {

    Button signup,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_install);
        login = findViewById(R.id.btnLoginTaskFirstPage);
        signup = findViewById(R.id.btnSignUpTaskFirstPage);

        setTitle("Welcome");

        // already signedup user;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(FirstInstallActivity.this,LoginActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(a);
            }
        });

        // first time signing the account --- need to add him to database profile.
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(FirstInstallActivity.this,UserProfileData.class);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(a);
            }
        });
    }
}
