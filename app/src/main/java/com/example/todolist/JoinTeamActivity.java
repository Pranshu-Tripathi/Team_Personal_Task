package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JoinTeamActivity extends AppCompatActivity {

    private TextView TeamName,Username;
    private EditText TeamNameEdit,UsernameEdit;
    private Button JTeam;
    private DatabaseReference reference;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ListView listView;
    static ArrayList<String> TeamsArray;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);

        TeamName = findViewById(R.id.JoinTeamNameText);
        TeamNameEdit = findViewById(R.id.JoinTeamNameEditText);
        JTeam = findViewById(R.id.btnJoinTeam);
        listView = findViewById(R.id.TeamLists);
        TeamsArray = new ArrayList<String>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference2 = FirebaseDatabase.getInstance().getReference()
                .child("DoesApp")
                .child("profile")
                .child(firebaseUser.getUid());

        // Filing up the array list
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("DoesApp")
                .child("profile")
                .child(firebaseUser.getUid())
                .child("Teams");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                     TeamsArray.clear();
                     for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                         TeamsArray.add(dataSnapshot1.getValue().toString());
                     }
                     arrayAdapter = new ArrayAdapter(JoinTeamActivity.this,android.R.layout.simple_list_item_1,TeamsArray) ;
                     listView.setAdapter(arrayAdapter);
                     arrayAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(JoinTeamActivity.this, "No Previous Team Activity", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        reference = FirebaseDatabase.getInstance().getReference()
                .child("DoesApp")
                .child("business")
                .child("teams");

        JTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(TeamNameEdit.getText())){
                    Toast.makeText(JoinTeamActivity.this, "Enter All Parameters", Toast.LENGTH_SHORT).show();
                }else{
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (dataSnapshot1.exists()) {
                                        if (dataSnapshot1.child("Team Name").getValue().toString().equals(TeamNameEdit.getText().toString())) {
                                            Toast.makeText(JoinTeamActivity.this, "Team Found", Toast.LENGTH_SHORT).show();
                                            if (dataSnapshot1.child("Team Admin").getValue().toString().equals(firebaseUser.getUid())) {
                                                Toast.makeText(JoinTeamActivity.this, "Found as Admin!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(JoinTeamActivity.this, BusinessMainActivity.class);
                                                intent.putExtra("UserPermits", true);
                                                intent.putExtra("User", firebaseUser.getUid());
                                                intent.putExtra("TeamName", dataSnapshot1.child("Team Name").getValue().toString());
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            } else {
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot.child(TeamNameEdit.getText().toString()).child("Members").getChildren()) {
                                                    if (dataSnapshot2.getValue().toString().equals(firebaseUser.getEmail())) {
                                                        Toast.makeText(JoinTeamActivity.this, "Found As Member!", Toast.LENGTH_SHORT).show();
                                                        boolean already = false;
                                                        for(int i = 0; i < TeamsArray.size() ; ++i){
                                                            if(TeamsArray.get(i).equals(TeamNameEdit.getText().toString())){
                                                                already = true;
                                                                break;
                                                            }
                                                        }
                                                        if(!already) {
                                                            databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    int teamNumber = Integer.parseInt(dataSnapshot.child("TeamNumber").getValue().toString());
                                                                    dataSnapshot.getRef().child("Teams").child("Team" + Integer.toString(teamNumber)).setValue(TeamNameEdit.getText().toString());
                                                                    teamNumber++;
                                                                    dataSnapshot.getRef().child("TeamNumber").setValue(teamNumber);
                                                                    databaseReference2.removeEventListener(this);
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }
                                                        Intent intent = new Intent(JoinTeamActivity.this, BusinessMainActivity.class);
                                                        intent.putExtra("UserPermits", false);
                                                        intent.putExtra("AdminDetails", dataSnapshot1.child("Team Admin").getValue().toString());
                                                        intent.putExtra("TeamName", dataSnapshot1.child("Team Name").getValue().toString());
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                        break;
                                                    } else {
                                                        Toast.makeText(JoinTeamActivity.this, "User Not Found!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        } else {
                                            Toast.makeText(JoinTeamActivity.this, "Team Not Found!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(JoinTeamActivity.this, "No Such Data Exists!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else {
                                Toast.makeText(JoinTeamActivity.this, "There are no teams!", Toast.LENGTH_SHORT).show();
                            }
                            reference.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }



            }
        });

        // List item Clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String nameTeam = TeamsArray.get(position);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            boolean found_team = false;
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                if(dataSnapshot1.exists()){

                                    if(dataSnapshot1.child("Team Name").getValue().toString().equals(nameTeam)){
                                        Toast.makeText(JoinTeamActivity.this, "team found!", Toast.LENGTH_SHORT).show();
                                        if(dataSnapshot1.child("Team Admin").getValue().toString().equals(firebaseUser.getUid())){
                                            Toast.makeText(JoinTeamActivity.this, "Found as Admin!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(JoinTeamActivity.this, BusinessMainActivity.class);
                                            intent.putExtra("UserPermits", true);
                                            intent.putExtra("User", firebaseUser.getUid());
                                            intent.putExtra("TeamName", nameTeam);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }else{
                                            boolean found = false;
                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.child(nameTeam).child("Members").getChildren()){
                                                if(dataSnapshot2.getValue().toString().equals(firebaseUser.getEmail())){
                                                    Toast.makeText(JoinTeamActivity.this, "Found As Member!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(JoinTeamActivity.this, BusinessMainActivity.class);
                                                    intent.putExtra("UserPermits", false);
                                                    intent.putExtra("AdminDetails", dataSnapshot1.child("Team Admin").getValue().toString());
                                                    intent.putExtra("TeamName", dataSnapshot1.child("Team Name").getValue().toString());
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    found = true;
                                                    break;
                                                }
                                            }
                                            if(!found){
                                                Toast.makeText(JoinTeamActivity.this, "Not Found!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        found_team = true;
                                        break;
                                    }
                                }
                            }
                            if(!found_team){
                                Toast.makeText(JoinTeamActivity.this, "No Such Team Exists!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(JoinTeamActivity.this, "No Teams!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
