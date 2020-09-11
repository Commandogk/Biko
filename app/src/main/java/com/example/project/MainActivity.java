package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText email, pass;
    Button login;
    TextView forgotpassword, createacc;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.editTextTextEmailAddress1);
        pass = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();
        forgotpassword = findViewById(R.id.forgotpass);
        createacc = findViewById(R.id.newacc);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String password = pass.getText().toString();
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("logging in");
                progressDialog.show();
                auth.signInWithEmailAndPassword(Email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            progressDialog.cancel();
                            Toast.makeText(getApplicationContext(), "sign prb", Toast.LENGTH_LONG).show();
                        }
                        else{
                            progressDialog.cancel();
                            startActivity(new Intent(MainActivity.this, HomePage.class));
                            auth.addAuthStateListener(authStateListener);
                        }
                    }
                });
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, forgotpassword.class));finish();
            }
        });
        createacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, createaccount.class));finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser user = auth.getCurrentUser();
        Intent intent =getIntent();
        boolean first = intent.getBooleanExtra("first",false);
        boolean logout = intent.getBooleanExtra("logout",false);
        if(user!=null){
            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("logging in");
            progressDialog.show();
            if(!logout||!first) {
                Task<Void> usertask = auth.getCurrentUser().reload();
                usertask.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            authStateListener = new FirebaseAuth.AuthStateListener() {
                                @Override
                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                    if (firebaseAuth.getCurrentUser() != null) {
                                        progressDialog.cancel();
                                        startActivity(new Intent(MainActivity.this, HomePage.class));
                                        finish();

                                    }
                                }
                            };
                            auth.addAuthStateListener(authStateListener);
                        }

                    }
                });
            }
        }
        else
            FirebaseAuth.getInstance().signOut();
    }
}