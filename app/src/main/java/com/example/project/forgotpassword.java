package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {
    EditText email1;
    Button reset;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        email1 =(EditText)findViewById(R.id.resetemail);
        reset = findViewById(R.id.reset);
        auth = FirebaseAuth.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!email1.getText().toString().isEmpty()) {
                    auth.sendPasswordResetEmail(email1.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),"Reset Mail sent",Toast.LENGTH_SHORT).show();
                                        Handler m=new Handler();
                                        m.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(new Intent(forgotpassword.this,MainActivity.class));
                                                finish();
                                            }
                                        },1000);
                                    }
                                }
                            });
                }
                else
                    Toast.makeText(getApplicationContext(),"empty"+email1.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}