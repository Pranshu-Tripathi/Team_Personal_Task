package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class NewTaskAct extends AppCompatActivity {




    Calendar mCurrentDateTime;
    int date,month,year,hour,min;

    private TextView titlepage,titleText,descriptionText,timelineText,DateText,TimeText;
    private EditText titleEdText,descriptionEdText;
    private Button btnSaveTask,btnCancelTask;
    Integer num = new Random().nextInt();
    String keydoes  = Integer.toString(num);

    private DatabaseReference reference;


    public void DateSetter(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(NewTaskAct.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateS="";
                dateS+= Integer.toString(dayOfMonth)+"/"+Integer.toString(month+1)+"/"+Integer.toString(year);
                DateText.setText(dateS);
            }
        },year,month,date);
        datePickerDialog.show();
    }

    public void TimeSetter(View view){
        TimePickerDialog timePickerDialog = new TimePickerDialog(NewTaskAct.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String timeS="";
                timeS+= Integer.toString(hourOfDay)+":"+Integer.toString(minute);
                TimeText.setText(timeS);
            }
        },hour,min,true);
        timePickerDialog.show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        setTitle("New Task");

        // Date time setter
        mCurrentDateTime = Calendar.getInstance();
        date = mCurrentDateTime.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDateTime.get(Calendar.MONTH);
        year = mCurrentDateTime.get(Calendar.YEAR);
        hour = mCurrentDateTime.get(Calendar.HOUR_OF_DAY);
        min = mCurrentDateTime.get(Calendar.MINUTE);


        DateText = findViewById(R.id.deadlinedateTextView);
        TimeText = findViewById(R.id.deadlinetimeTextView);
        titlepage = findViewById(R.id.newTitlePageText);
        titleText = findViewById(R.id.newTitleText);
        descriptionText = findViewById(R.id.newDescriptionText);
        timelineText = findViewById(R.id.newTimeText);
        titleEdText = findViewById(R.id.newTitleEditText);
        descriptionEdText = findViewById(R.id.newDescriptionEditText);
        btnSaveTask= findViewById(R.id.btnSaveTask);
        btnCancelTask= findViewById(R.id.btnCancelTask);
        final String username = getIntent().getStringExtra("username");
        Toast.makeText(this,"User "+ username,Toast.LENGTH_SHORT).show();
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // insert new data to firebase
                reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child("personal").child(username).child("Does").child("Does"+num);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy\nHH:mm");
                        String CreationDate = simpleDateFormat.format(new Date());
                        dataSnapshot.getRef().child("creationdoes").setValue(CreationDate);
                        dataSnapshot.getRef().child("titledoes").setValue(titleEdText.getText().toString());
                        dataSnapshot.getRef().child("descdoes").setValue(descriptionEdText.getText().toString());
                        dataSnapshot.getRef().child("datedoes").setValue(DateText.getText()+"\n"+TimeText.getText());
                        dataSnapshot.getRef().child("keydoes").setValue(keydoes);

                        Intent a = new Intent(NewTaskAct.this,MainActivity.class);
                        a.putExtra("Username",username);
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
        btnCancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(NewTaskAct.this,MainActivity.class);
                a.putExtra("Username",username);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
            }
        });
    }
}
