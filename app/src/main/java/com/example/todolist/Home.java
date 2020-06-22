package com.example.todolist;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            Intent a = new Intent(Home.this,MainActivity.class);
            a.putExtra("Username",firebaseUser.getUid());
            a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
        }else{
            Intent a = new Intent(Home.this,FirstInstallActivity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);

        }
    }
}
