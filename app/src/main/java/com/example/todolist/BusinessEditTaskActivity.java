package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

public class BusinessEditTaskActivity extends AppCompatActivity {

    String CurrentUser;
    String AdminDetails;
    String TeamName;
    String businessKey;
    static boolean isAdmin;
    Calendar mCurrentDateTime;
    int date,month,year,hour,min;

    private TextView editteamtitlepage,editteamtitleText,editteamdescriptionText,editteamtimelineText,editteamDateText,editteamTimeText;
    private EditText editteamtitleEdText,editteamdescriptionEdText,editteamassignerEditText;
    private Button editbtnTeamSaveTask,editbtnTeamCancelTask;
    private CheckBox editstatusBox;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference,reference2,getReference;
    ImageView calenderIcon,timeIcon;

    public void DateSetter(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(BusinessEditTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateS="";
                dateS+= Integer.toString(dayOfMonth)+"/"+Integer.toString(month+1)+"/"+Integer.toString(year);
                editteamDateText.setText(dateS);
            }
        },year,month,date);
        datePickerDialog.show();
    }

    public void TimeSetter(View view){
        TimePickerDialog timePickerDialog = new TimePickerDialog(BusinessEditTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String timeS="";
                timeS+= Integer.toString(hourOfDay)+":"+Integer.toString(minute);
                editteamTimeText.setText(timeS);
            }
        },hour,min,true);
        timePickerDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_edit_task);

        TeamName = getIntent().getStringExtra("TeamName");
        businessKey = getIntent().getStringExtra("businesskeyvalue");

        mCurrentDateTime = Calendar.getInstance();
        date = mCurrentDateTime.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDateTime.get(Calendar.MONTH);
        year = mCurrentDateTime.get(Calendar.YEAR);
        hour = mCurrentDateTime.get(Calendar.HOUR_OF_DAY);
        min = mCurrentDateTime.get(Calendar.MINUTE);

        editteamtitlepage = findViewById(R.id.EditnewTeamTaskTitlePageText);
        editteamtitleText = findViewById(R.id.EditnewTeamTaskTitleText);
        editteamdescriptionText = findViewById(R.id.EditnewTeamTaskDescriptionText);
        editteamtimelineText = findViewById(R.id.EditnewTeamTaskTimeText);
        editteamDateText = findViewById(R.id.EditTeamTaskdeadlinedateTextView);
        editteamTimeText = findViewById(R.id.EditTeamTaskdeadlinetimeTextView);
        editteamtitleEdText = findViewById(R.id.EditnewTeamTaskTitleEditText);
        editteamdescriptionEdText = findViewById(R.id.EditnewTeamTaskDescriptionEditText);
        editteamassignerEditText = findViewById(R.id.EditAssigningEditText);
        editstatusBox = findViewById(R.id.EditStatusCheckBox);
        editbtnTeamSaveTask = findViewById(R.id.EditbtnTeamTaskSaveTask);
        editbtnTeamCancelTask = findViewById(R.id.EditbtnTeamTaskCancelTask);
        calenderIcon = findViewById(R.id.calenderIcon);
        timeIcon = findViewById(R.id.timeIcon);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        CurrentUser = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference()
                .child("DoesApp")
                .child("business")
                .child("teams")
                .child(TeamName);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AdminDetails = dataSnapshot.child("Team Admin").getValue().toString();
                if(AdminDetails.equals(CurrentUser))    isAdmin = true;
                else    isAdmin = false;
                if(isAdmin){
                    // Admin at work
                }else{
                    // Member to stock
                    editteamassignerEditText.setEnabled(false);
                    editteamtitleEdText.setEnabled(false);
                    editteamdescriptionEdText.setEnabled(false);
                    editteamassignerEditText.setEnabled(false);
                    editteamassignerEditText.setEnabled(false);
                    editteamassignerEditText.setEnabled(false);
                    editteamassignerEditText.setEnabled(false);
                    editbtnTeamCancelTask.setEnabled(false);
                    calenderIcon.setEnabled(false);
                    timeIcon.setEnabled(false);
                }
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Setting up the page
        String DateTime = getIntent().getStringExtra("dead");
        String[] DateTimeArray = DateTime.split("\n");
        editteamDateText.setText(DateTimeArray[0]);
        editteamTimeText.setText(DateTimeArray[1]);
        editteamtitleEdText.setText(getIntent().getStringExtra("title"));
        editteamdescriptionEdText.setText(getIntent().getStringExtra("desc"));
        editteamassignerEditText.setText(getIntent().getStringExtra("assigner"));

        String Stat = getIntent().getStringExtra("stat");
        if(Stat.equals("pending")){
            editstatusBox.setChecked(false);
        }else if(Stat.equals("done"))
            editstatusBox.setChecked(true);


        reference2 = FirebaseDatabase.getInstance().getReference()
                .child("DoesApp")
                .child("business")
                .child("teams")
                .child(TeamName)
                .child("Tasks")
                .child("Task"+businessKey);

        getReference = FirebaseDatabase.getInstance().getReference()
                .child("DoesApp")
                .child("business")
                .child("teams")
                .child(TeamName)
                .child("Tasks")
                .child("Task"+businessKey);


        editbtnTeamSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("businessdeadlinedoes").setValue(editteamDateText.getText().toString()+"\n"+editteamTimeText.getText().toString());
                        dataSnapshot.getRef().child("businessassignedtochange").setValue(editteamassignerEditText.getText().toString());
                        if(editstatusBox.isChecked()){
                            dataSnapshot.getRef().child("businessstatuschange").setValue("done");
                        }else{
                            dataSnapshot.getRef().child("businessstatuschange").setValue("pending");
                        }
                        dataSnapshot.getRef().child("businesstitledoes").setValue(editteamtitleEdText.getText().toString());
                        dataSnapshot.getRef().child("businessdescriptiondoes").setValue(editteamdescriptionEdText.getText().toString());
                        reference2.removeEventListener(this);
//                        Intent a = new Intent(BusinessEditTaskActivity.this,BusinessMainActivity.class);
//                        a.putExtra("UserPermits",true);
//                        a.putExtra("TeamName",TeamName);
//                        a.putExtra("User",firebaseUser.getUid());
//                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(a);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                // returning Intent
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AdminDetails = dataSnapshot.child("Team Admin").getValue().toString();
                        if(AdminDetails.equals(CurrentUser))    isAdmin = true;
                        else    isAdmin = false;
                        if(isAdmin){
                            // Admin at work
                            Intent a = new Intent(BusinessEditTaskActivity.this,BusinessMainActivity.class);
                            a.putExtra("UserPermits",true);
                            a.putExtra("TeamName",TeamName);
                            a.putExtra("User",firebaseUser.getUid());
                            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(a);
                        }else{
                            // Member to stock
                            Intent a = new Intent(BusinessEditTaskActivity.this,BusinessMainActivity.class);
                            a.putExtra("UserPermits",false);
                            a.putExtra("TeamName",TeamName);
                            a.putExtra("AdminDetails",dataSnapshot.child("Team Admin").getValue().toString());
                            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(a);

                        }
                        reference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        editbtnTeamCancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BusinessEditTaskActivity.this)
                        .setTitle("Are You Sure?")
                        .setMessage("The Delete Choice is Irreversible")
                        .setIcon(android.R.drawable.alert_dark_frame)
                        .setNegativeButton("No",null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataSnapshot.getRef().removeValue();
                                        Intent a = new Intent(BusinessEditTaskActivity.this,BusinessMainActivity.class);
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
                        })
                        .show();
            }
        });

    }
}
