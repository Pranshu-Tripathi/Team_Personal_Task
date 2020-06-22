package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView loginPageTitle, loginTitleText, loginPasswordText;
    EditText loginUsername , loginPassword;
    Button btnLogin;
    FirebaseAuth firebaseAuth;
   // DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        loginPageTitle = findViewById(R.id.loginTitlePageText);
        loginTitleText = findViewById(R.id.loginTitleText);
        loginPasswordText = findViewById(R.id.loginDescriptionText);
        loginUsername = findViewById(R.id.loginTitleEditText);
        loginPassword = findViewById(R.id.loginDescriptionEditText);
        btnLogin = findViewById(R.id.btnLoginTask);


        firebaseAuth = FirebaseAuth.getInstance();

//        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String username = loginUsername.getText().toString();
                final String password = loginPassword.getText().toString();
                if (!TextUtils.isEmpty(loginUsername.getText().toString()) && !TextUtils.isEmpty(loginPassword.getText().toString())) {
                    final PleaseWaitDialog pleaseWaitDialog = new PleaseWaitDialog(LoginActivity.this);
                    // get data from firebase
                    pleaseWaitDialog.startLoadingDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pleaseWaitDialog.dismissDialog();
                        }
                    },2000);
                    firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("Username", firebaseAuth.getUid());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this,"None of the options cannot be left empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
