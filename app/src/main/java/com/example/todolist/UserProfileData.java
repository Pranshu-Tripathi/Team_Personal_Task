package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileData extends AppCompatActivity {

    private TextView UsernameTextView,UserMailIdTextView,passwordTextView;
    private EditText UsernameEditText,UsernameMailIdEditText,PasswordEditText;
    private Button CreateUserProfile;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_data);
        setTitle("User Profile Data!");
        UsernameTextView = findViewById(R.id.UserInfoTitleText);
        UserMailIdTextView = findViewById(R.id.UserInfoDescriptionText);
        passwordTextView = findViewById(R.id.UserInfoPasswordText);
        UsernameEditText = findViewById(R.id.UserNameEditText);
        UsernameMailIdEditText = findViewById(R.id.UserInfoDescriptionEditText);
        PasswordEditText = findViewById(R.id.UserInfoPasswordEditText);
        CreateUserProfile = findViewById(R.id.btnCreateProfile);

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child("profile");

        CreateUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(UsernameEditText.getText()) && TextUtils.isEmpty(UsernameMailIdEditText.getText()) && TextUtils.isEmpty(PasswordEditText.getText())){
                    Toast.makeText(UserProfileData.this,"Please Input All parameters",Toast.LENGTH_SHORT).show();
                }else{
                    final String username = UsernameEditText.getText().toString();
                    final String usernameMailId = UsernameMailIdEditText.getText().toString();
                    String password = PasswordEditText.getText().toString();
                    firebaseAuth.createUserWithEmailAndPassword(usernameMailId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataSnapshot.getRef().child(firebaseAuth.getUid()).child("username").setValue(username);
                                        dataSnapshot.getRef().child(firebaseAuth.getUid()).child("mailid").setValue(usernameMailId);
                                        dataSnapshot.getRef().child(firebaseAuth.getUid()).child("userId").setValue(firebaseAuth.getUid());
                                        dataSnapshot.getRef().child(firebaseAuth.getUid()).child("TeamNumber").setValue(0);
                                        reference.removeEventListener(this);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                Intent a = new Intent(UserProfileData.this,AccountChoiceActivity.class);
                                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(a);

                            }else{
                                Toast.makeText(UserProfileData.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }
}
