package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;

public class EditTaskDesk extends AppCompatActivity {

    private TextView titlepage,titleText,descriptionText,timelineText,DateTextView,TimeTextView;
    private EditText titleEdText,descriptionEdText;
    private Button btnUpdateTask,btnDeleteTask;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    Calendar mCurrentDateTime;
    int date,month,year,hour,min;

    public void DateSetter(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditTaskDesk.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateS="";
                dateS+= Integer.toString(dayOfMonth)+"/"+Integer.toString(month+1)+"/"+Integer.toString(year);
                DateTextView.setText(dateS);
            }
        },year,month,date);
        datePickerDialog.show();
    }


    public void TimeSetter(View view){
        TimePickerDialog timePickerDialog = new TimePickerDialog(EditTaskDesk.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String timeS="";
                timeS+= Integer.toString(hourOfDay)+":"+Integer.toString(minute);
                TimeTextView.setText(timeS);
            }
        },hour,min,true);
        timePickerDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_desk);

        setTitle("Edit Task");

        mCurrentDateTime = Calendar.getInstance();
        date = mCurrentDateTime.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDateTime.get(Calendar.MONTH);
        year = mCurrentDateTime.get(Calendar.YEAR);
        hour = mCurrentDateTime.get(Calendar.HOUR_OF_DAY);
        min = mCurrentDateTime.get(Calendar.MINUTE);



        DateTextView = findViewById(R.id.EditdeadlinedateTextView);
        TimeTextView = findViewById(R.id.EditdeadlinetimeTextView);
        titlepage = findViewById(R.id.newUpdateTitlePageText);
        titleText = findViewById(R.id.newUpdateTitleText);
        descriptionText = findViewById(R.id.newUpdateDescriptionText);
        timelineText = findViewById(R.id.newUpdateTimeText);
        titleEdText = findViewById(R.id.newUpdateTitleEditText);
        descriptionEdText = findViewById(R.id.newUpdateDescriptionEditText);
        btnUpdateTask= findViewById(R.id.btnUpdateTask);
        btnDeleteTask= findViewById(R.id.btnDeleteTask);

        firebaseAuth = FirebaseAuth.getInstance();

        // getting previous values
        titleEdText.setText(getIntent().getStringExtra("titledoes"));
        descriptionEdText.setText(getIntent().getStringExtra("descdoes"));


            String DateTime = getIntent().getStringExtra("datedoes");

            String[] DateTimeArray = DateTime.split("\n");
            DateTextView.setText(DateTimeArray[0]);
            TimeTextView.setText(DateTimeArray[1]);

        final String getKeydoes = getIntent().getStringExtra("keydoes");

        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child("personal").child(firebaseAuth.getCurrentUser().getUid()).child("Does").child("Does"+getKeydoes);
        // deletion
        btnDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditTaskDesk.this)
                        .setTitle("Are You Sure?")
                        .setMessage("The Delete Choice is Irreversible")
                        .setIcon(android.R.drawable.alert_dark_frame)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        reference.getRef().removeValue();
                                        Intent a = new Intent(EditTaskDesk.this,MainActivity.class);
                                        a.putExtra("Username",firebaseAuth.getCurrentUser().getUid());
                                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(a);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                        }
                        })
                        .setNegativeButton("NO",null)
                        .show();
            }
        });

        // now button addition
        btnUpdateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("titledoes").setValue(titleEdText.getText().toString());
                        dataSnapshot.getRef().child("descdoes").setValue(descriptionEdText.getText().toString());
                        dataSnapshot.getRef().child("datedoes").setValue(DateTextView.getText()+"\n"+TimeTextView.getText());
                        dataSnapshot.getRef().child("keydoes").setValue(getKeydoes);
                        Intent a = new Intent(EditTaskDesk.this,MainActivity.class);
                        a.putExtra("Username",firebaseAuth.getCurrentUser().getUid());
                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(a);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
