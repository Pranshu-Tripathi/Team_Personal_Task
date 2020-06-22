package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BusinessChoiceActivity extends AppCompatActivity {


    private Button CreateTeam,JoinTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_choice);

        CreateTeam = findViewById(R.id.btnCreateTeamTask);
        JoinTeam = findViewById(R.id.btnJoinTeamTask);

        CreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusinessChoiceActivity.this,CreateTeamActivity.class);
                startActivity(intent);
            }
        });

        JoinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusinessChoiceActivity.this,JoinTeamActivity.class);
                startActivity(intent);
            }
        });



    }
}
