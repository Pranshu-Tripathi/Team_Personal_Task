package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BusinessMainActivity extends AppCompatActivity {




    private TextView businessTitlePageText, businessSubtitleTeamPageText, businessSubtitleTeamMemberPageText,endBusinessPage;
    private Button addNewTeamTask;


    private DatabaseReference reference;
    private DatabaseReference reference2;
    private DatabaseReference reference3;
    private DatabaseReference reference4;
    private RecyclerView teamDoes;
    private ArrayList<BusinessDoes> task;
    private BusinessDoesAdapter businessDoesAdapter;
    FirebaseAuth firebaseAuth;
    private boolean admin;
    private String Admin;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_main);

        firebaseAuth = FirebaseAuth.getInstance();


        businessTitlePageText = findViewById(R.id.newTeamTitlePageText);
        businessSubtitleTeamPageText = findViewById(R.id.subtitleTeamTextView);
        businessSubtitleTeamMemberPageText = findViewById(R.id.subtitleTeamMembersTextView);
        endBusinessPage = findViewById(R.id.endTeamPage);
        addNewTeamTask = findViewById(R.id.addNewTeamTaskButton);
        final PleaseWaitDialog pleaseWaitDialog = new PleaseWaitDialog(BusinessMainActivity.this);
        // Setting up the Titles
        businessTitlePageText.setText(getIntent().getStringExtra("TeamName"));
        admin = getIntent().getBooleanExtra("UserPermits",false);
        if(admin){
            reference2 = FirebaseDatabase.getInstance().getReference()
                    .child("DoesApp")
                    .child("profile")
                    .child(getIntent().getStringExtra("User"));
            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    businessSubtitleTeamPageText.setText("Admin : "+dataSnapshot.child("username").getValue().toString());
                    Admin = dataSnapshot.child("username").getValue().toString();
                    boolean already = false;
                    for(int i = 0; i < JoinTeamActivity.TeamsArray.size() ; ++i){
                        if(JoinTeamActivity.TeamsArray.get(i).equals(businessTitlePageText.getText().toString())){
                            already = true;
                            break;
                        }
                    }
                    if(!already) {
                        int teamNumber = Integer.parseInt(dataSnapshot.child("TeamNumber").getValue().toString());
                        dataSnapshot.getRef().child("Teams").child("Team" + Integer.toString(teamNumber)).setValue(businessTitlePageText.getText().toString());
                        teamNumber++;
                        dataSnapshot.getRef().child("TeamNumber").setValue(teamNumber);
                    }
                    reference2.removeEventListener(this);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            reference2 = FirebaseDatabase.getInstance().getReference()
                    .child("DoesApp")
                    .child("profile")
                    .child(getIntent().getStringExtra("AdminDetails"));
            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    businessSubtitleTeamPageText.setText("Admin : "+dataSnapshot.child("username").getValue().toString());
                    Admin = dataSnapshot.child("username").getValue().toString();
                    reference2.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        reference2 = FirebaseDatabase.getInstance().getReference()
                .child("DoesApp")
                .child("business")
                .child("teams")
                .child(getIntent().getStringExtra("TeamName"));


        // get data from firebase
        pleaseWaitDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pleaseWaitDialog.dismissDialog();
            }
        },5000);

        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                businessSubtitleTeamMemberPageText.setText("Members : "+dataSnapshot.child("Total Members").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        teamDoes = findViewById(R.id.TeamDoes);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        teamDoes.setLayoutManager(llm);
        task = new ArrayList<BusinessDoes>();

        FirebaseApp.initializeApp(this);
        reference = FirebaseDatabase.getInstance().getReference()
                .child("DoesApp")
                .child("business")
                .child("teams")
                .child(getIntent().getStringExtra("TeamName"))
                .child("Tasks");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                task.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    try {
                        BusinessDoes p = dataSnapshot1.getValue(BusinessDoes.class);
                        task.add(p);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                businessDoesAdapter = new BusinessDoesAdapter(BusinessMainActivity.this,task);
                teamDoes.setAdapter(businessDoesAdapter);
                businessDoesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BusinessMainActivity.this, "Something went Wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        addNewTeamTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(admin){
                    Toast.makeText(BusinessMainActivity.this, "Permit Granted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BusinessMainActivity.this,BusinessNewTaskActivity.class);
                    intent.putExtra("TeamName",businessTitlePageText.getText());
                    startActivity(intent);
                }else{
                    Toast.makeText(BusinessMainActivity.this, "Permit Denied", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.business_signout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.businessSignOut:
                firebaseAuth.signOut();
                Intent a = new Intent(this,FirstInstallActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
                return true;
            case R.id.businessSwitchAccount:
                Intent aa = new Intent(this,AccountChoiceActivity.class);
                aa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                aa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(aa);
                return true;
            case R.id.addMember:
                reference4 = FirebaseDatabase.getInstance().getReference()
                        .child("DoesApp")
                        .child("profile")
                        .child(firebaseAuth.getUid());
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("username").getValue().toString().equals(Admin)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(BusinessMainActivity.this);
                            builder.setTitle("Enter The Mail ID");
                            builder.setMessage("Enter GmailId/MailId of the user");
                            final EditText memberText = new EditText(BusinessMainActivity.this);
                            memberText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                            builder.setView(memberText);
                            builder.setIcon(R.drawable.common_google_signin_btn_icon_light_focused);
                            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(TextUtils.isEmpty(memberText.getText())){
                                        Toast.makeText(BusinessMainActivity.this, "Enter Valid Mail Id", Toast.LENGTH_SHORT).show();
                                    }else {
                                        reference3 = FirebaseDatabase.getInstance().getReference()
                                                .child("DoesApp")
                                                .child("business")
                                                .child("teams")
                                                .child(businessTitlePageText.getText().toString());
                                        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                int teamMemberNum = Integer.parseInt(dataSnapshot.child("Total Members").getValue().toString());
                                                dataSnapshot.getRef().child("Members").child("Member"+teamMemberNum).setValue(memberText.getText().toString());
                                                teamMemberNum++;
                                                dataSnapshot.getRef().child("Total Members").setValue(teamMemberNum);
                                                businessSubtitleTeamMemberPageText.setText("Members : " + teamMemberNum);
                                                reference3.removeEventListener(this);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            });
                            builder.setNegativeButton("No",null);
                            builder.show();
                        }else{
                            Toast.makeText(BusinessMainActivity.this, "Only Admin Has This Right.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return true;
            default:
                return false;
        }
    }
}
