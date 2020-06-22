package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class CreateTeamActivity extends AppCompatActivity {



    private TextView TeamNameTextView, AddMemberTextView;
    private EditText TeamNameEditTextView, MemberEditTextView;
    private Button BtnCreateTeam, BtnAddMember, BtnCancelCreate;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    private DatabaseReference getReference;
    private DatabaseReference getReference2;
    private int member_added = 0;
    int number_team = new Random().nextInt(10000);
    String TeamKey = Integer.toString(number_team);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child("business").child("teams").child("Team" + TeamKey);
        reference2 = FirebaseDatabase.getInstance().getReference().child("DoesApp").child("business").child("teams").child("Team" + TeamKey);
        getReference = FirebaseDatabase.getInstance().getReference()
                .child("DoesApp")
                .child("business")
                .child("teams")
                .child("Team" + TeamKey);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("Team Admin").setValue(firebaseAuth.getCurrentUser().getUid());
                dataSnapshot.getRef().child("TeamKey").setValue(TeamKey);
                dataSnapshot.getRef().child("Total Members").setValue(0);
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        TeamNameTextView = findViewById(R.id.newBusinessCreateTeamNameText);
        AddMemberTextView = findViewById(R.id.newBusinessCreateTeamMemberText);
        TeamNameEditTextView = findViewById(R.id.newBusinessCreateTeamNameEditText);
        MemberEditTextView = findViewById(R.id.newBusinessCreateTeamMemberEditText);
        BtnCreateTeam = findViewById(R.id.btnCreateTeamSecondTask);
        BtnCancelCreate = findViewById(R.id.btnCancelCreationTeamTask);
        BtnAddMember = findViewById(R.id.addMemberButton);
        TeamNameTextView = findViewById(R.id.newBusinessCreateTeamNameText);

        TeamNameEditTextView.setText("Team" + TeamKey);
        TeamNameEditTextView.setEnabled(false);



        BtnCancelCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CreateTeamActivity.this)
                        .setTitle("Are You Sure?")
                        .setMessage("The Team would be erased!")
                        .setIcon(android.R.drawable.alert_dark_frame)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteTeam();
                            }
                        }).setNegativeButton("No", null).show();

            }
        });


        BtnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(MemberEditTextView.getText())) {
                    Toast.makeText(CreateTeamActivity.this, "Members URL not Provided", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        getReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().child("Members").child("Member" + Integer.toString(member_added)).setValue(MemberEditTextView.getText().toString());
                                member_added++;
                                MemberEditTextView.setText("");
                                Toast.makeText(CreateTeamActivity.this, "Member : "+Integer.toString(member_added), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        final PleaseWaitDialog pleaseWaitDialog = new PleaseWaitDialog(CreateTeamActivity.this);

        BtnCreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtnCreateTeam.setEnabled(false);

                // get data from firebase

                if (TextUtils.isEmpty(TeamNameEditTextView.getText())) {
                    Toast.makeText(CreateTeamActivity.this, "Team Needs To have a Name", Toast.LENGTH_SHORT).show();
                    BtnCreateTeam.setEnabled(true);
                } else if (member_added == 0) {
                    Toast.makeText(CreateTeamActivity.this, "No Other Members Added", Toast.LENGTH_SHORT).show();
                    BtnCreateTeam.setEnabled(true);
                } else {


                    final Runnable r = new Runnable() {
                        @Override
                        public void run() {
                              pleaseWaitDialog.dismissDialog();
                        }
                    };
                    getReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().child("Team Name").setValue(TeamNameEditTextView.getText().toString());
                            dataSnapshot.getRef().child("Total Members").setValue(Integer.toString(member_added));
                            getReference.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    pleaseWaitDialog.startLoadingDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pleaseWaitDialog.dismissDialog();
                        }
                    },16000);
                    for (int i = 0;i < 100000000 ;++i);
                    for (int i = 0;i < 1000000000 ;++i);
//                    for (int i = 0;i < 1000000000 ;++i);

                    Intent intent = new Intent(CreateTeamActivity.this, BusinessMainActivity.class);
                    intent.putExtra("UserPermits", true);
                    intent.putExtra("User", firebaseUser.getUid());
                    intent.putExtra("TeamName", TeamNameEditTextView.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    public void deleteTeam() {
        reference2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(CreateTeamActivity.this, AccountChoiceActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }
}
