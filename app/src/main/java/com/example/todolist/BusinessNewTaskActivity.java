package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class BusinessNewTaskActivity extends AppCompatActivity {


    Calendar mCurrentDateTime;
    int date,month,year,hour,min;

    private TextView teamtitlepage,teamtitleText,teamdescriptionText,teamtimelineText,teamDateText,teamTimeText;
    private EditText teamtitleEdText,teamdescriptionEdText,teamassignerEditText;
    private Button btnTeamSaveTask,btnTeamCancelTask;
    private CheckBox statusBox;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    Integer teamNum = new Random().nextInt();
    String TaskKey = Integer.toString(teamNum);

    public void DateSetter(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(BusinessNewTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateS="";
                dateS+= Integer.toString(dayOfMonth)+"/"+Integer.toString(month+1)+"/"+Integer.toString(year);
                teamDateText.setText(dateS);
            }
        },year,month,date);
        datePickerDialog.show();
    }

    public void TimeSetter(View view){
        TimePickerDialog timePickerDialog = new TimePickerDialog(BusinessNewTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String timeS="";
                timeS+= Integer.toString(hourOfDay)+":"+Integer.toString(minute);
                teamTimeText.setText(timeS);
            }
        },hour,min,true);
        timePickerDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_new_task_actvity);

        mCurrentDateTime = Calendar.getInstance();
        date = mCurrentDateTime.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDateTime.get(Calendar.MONTH);
        year = mCurrentDateTime.get(Calendar.YEAR);
        hour = mCurrentDateTime.get(Calendar.HOUR_OF_DAY);
        min = mCurrentDateTime.get(Calendar.MINUTE);


        teamtitlepage = findViewById(R.id.newTeamTaskTitlePageText);
        teamtitleText = findViewById(R.id.newTeamTaskTitleText);
        teamdescriptionText = findViewById(R.id.newTeamTaskDescriptionText);
        teamtimelineText = findViewById(R.id.newTeamTaskTimeText);
        teamDateText = findViewById(R.id.TeamTaskdeadlinedateTextView);
        teamTimeText = findViewById(R.id.TeamTaskdeadlinetimeTextView);
        teamtitleEdText = findViewById(R.id.newTeamTaskTitleEditText);
        teamdescriptionEdText = findViewById(R.id.newTeamTaskDescriptionEditText);
        teamassignerEditText = findViewById(R.id.AssigningEditText);
        statusBox = findViewById(R.id.StatusCheckBox);
        btnTeamSaveTask = findViewById(R.id.btnTeamTaskSaveTask);
        btnTeamCancelTask = findViewById(R.id.btnTeamTaskCancelTask);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        final String TeamName = getIntent().getStringExtra("TeamName");

        btnTeamSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DoesApp")
                        .child("business")
                        .child("teams")
                        .child(TeamName)
                        .child("Tasks")
                        .child("Task"+TaskKey);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("businessstatus").setValue("Status : ");
                        dataSnapshot.getRef().child("businessassignedto").setValue("Assigned To :");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy\nHH:mm");
                        String CreationDate = simpleDateFormat.format(new Date());
                        dataSnapshot.getRef().child("businesscreationdoes").setValue(CreationDate);
                        dataSnapshot.getRef().child("businessdeadlinedoes").setValue(teamDateText.getText().toString()+"\n"+teamTimeText.getText().toString());
                        dataSnapshot.getRef().child("businesskeyvalue").setValue(TaskKey);
                        dataSnapshot.getRef().child("businessassignedtochange").setValue(teamassignerEditText.getText().toString());
                        if(statusBox.isChecked()){
                            dataSnapshot.getRef().child("businessstatuschange").setValue("done");
                        }else{
                            dataSnapshot.getRef().child("businessstatuschange").setValue("pending");
                        }
                        dataSnapshot.getRef().child("businesstitledoes").setValue(teamtitleEdText.getText().toString());
                        dataSnapshot.getRef().child("businessdescriptiondoes").setValue(teamdescriptionEdText.getText().toString());
                        dataSnapshot.getRef().child("teamname").setValue(TeamName);
                        reference.removeEventListener(this);
                        Intent a = new Intent(BusinessNewTaskActivity.this,BusinessMainActivity.class);
                        a.putExtra("UserPermits",true);
                        a.putExtra("TeamName",TeamName);
                        a.putExtra("User",firebaseUser.getUid());
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

        btnTeamCancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(BusinessNewTaskActivity.this,BusinessMainActivity.class);
                a.putExtra("UserPermits",true);
                a.putExtra("TeamName",TeamName);
                a.putExtra("User",firebaseUser.getUid());
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
            }
        });
    }
}
