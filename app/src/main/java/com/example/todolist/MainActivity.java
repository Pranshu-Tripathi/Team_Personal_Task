package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // menu code


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.signout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.signout:
                firebaseAuth.signOut();
                Intent a = new Intent(this,FirstInstallActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
                return true;
            case R.id.accountSwitch:
                Intent aa = new Intent(this,AccountChoiceActivity.class);
                aa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                aa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(aa);
                return true;
            default:
                return false;
        }
    }

    TextView titlePage,subTitlePage,endPage;
    Button btnAdd;

    DatabaseReference reference;
    RecyclerView ourDoes;
    ArrayList<Mydoes> list;
    DoesAdapter doesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("My Tasks!");

        titlePage = findViewById(R.id.newTitlePageText);
        subTitlePage = findViewById(R.id.subtitleTextView);
        endPage = findViewById(R.id.endPage);
        btnAdd = findViewById(R.id.addNewButton);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NewTaskAct.class);
                intent.putExtra("username",getIntent().getStringExtra("Username"));
                startActivity(intent);
            }
        });

        // working with data
        ourDoes = findViewById(R.id.OurDoes);
        ourDoes.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Mydoes>();


        FirebaseApp.initializeApp(MainActivity.this);
        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child("personal").child(getIntent().getStringExtra("Username")).child("Does");

        final PleaseWaitDialog pleaseWaitDialog = new PleaseWaitDialog(MainActivity.this);
        // get data from firebase
        pleaseWaitDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pleaseWaitDialog.dismissDialog();
            }
        },4000);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // setCode To retrieve data
                list.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Mydoes p = dataSnapshot1.getValue(Mydoes.class);
                    list.add(p);

                }

                doesAdapter = new DoesAdapter(MainActivity.this,list);
                ourDoes.setAdapter(doesAdapter);
                doesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            // set code to show an error
                Toast.makeText(MainActivity.this,"No Data Found!",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
