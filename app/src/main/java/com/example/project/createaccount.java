package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class createaccount extends AppCompatActivity {
    EditText username, name, email, pass, bio;
    Button register, upload, veri;
    ImageView propic;
    FirebaseAuth auth;
    Uri uri;
    DatabaseReference reference;
    StorageReference storageReference;
    boolean isAvailable=false;
    String email1, pass1, name1, bio1, username1, pro = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        username = findViewById(R.id.username);
        name = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.emaila);
        pass = findViewById(R.id.password1);
        bio = findViewById(R.id.bio);
        veri = findViewById(R.id.verify);
        register = findViewById(R.id.register);
        propic = findViewById(R.id.imageView);
        upload = findViewById(R.id.upload);
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://project-923cc.appspot.com");
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Toast.makeText(getApplicationContext(), "Verify email first to upload profilephoto", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), 22);
                }
            }
        });
        register.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(createaccount.this);
        veri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || pass.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "email/pass is empty", Toast.LENGTH_SHORT).show();
                else {
                    register.setEnabled(true);
                    progressDialog.setMessage("Sending Verification Mail");
                    progressDialog.show();
                    email1 = email.getText().toString();
                    pass1 = pass.getText().toString();
                    auth.createUserWithEmailAndPassword(email1, pass1)
                            .addOnCompleteListener(createaccount.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = auth.getCurrentUser();
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            progressDialog.cancel();
                                                            Toast.makeText(getApplicationContext(), "verification mail sent", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            progressDialog.cancel();
                                                            Toast.makeText(getApplicationContext(), " email already present", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                    else {
                                        progressDialog.cancel();
                                        Toast.makeText(getApplicationContext(), "not valid email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || pass.getText().toString().isEmpty() || pass.getText().toString().isEmpty() || username.getText().toString().isEmpty() || pass.getText().toString().isEmpty() || pass.getText().toString().isEmpty() || pass.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "email/pass is empty", Toast.LENGTH_SHORT).show();
                else {

                        email1 = email.getText().toString();
                        pass1 = pass.getText().toString();
                        username1 = username.getText().toString();
                        name1 = name.getText().toString();
                        bio1 = bio.getText().toString();
                    isAvailable(new IsAvailableCallback() {
                        @Override
                        public void onAvailableCallback(boolean userexist) {
                                 isAvailable=userexist;
                        }
                    });
                    if(isAvailable){
                        Toast.makeText(getApplicationContext(),"username already exist",Toast.LENGTH_SHORT).show();
                    }else {
                        progressDialog.setMessage("please wait....Creating Your Account");
                        progressDialog.show();
                        final FirebaseUser user = auth.getCurrentUser();
                        Task<Void> usertask = auth.getCurrentUser().reload();
                        usertask.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (user.isEmailVerified()) {
                                    uploadImage();
                                    Toast.makeText(getApplicationContext(), "verified", Toast.LENGTH_SHORT).show();
                                    String userid = user.getUid();
                                    pro = "gs://project-923cc.appspot.com/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + "profilephoto";
                                    reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/").child("Users").child(userid);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("username", username1);
                                    hashMap.put("name", name1);
                                    hashMap.put("bio", bio1);
                                    hashMap.put("propic", pro);
                                    hashMap.put("email-id", email1);
                                    reference.setValue(hashMap);
                                    user.getDisplayName();
                                    progressDialog.cancel();
                                    Toast.makeText(getApplicationContext(), "successfully registered \n", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(createaccount.this, MainActivity.class));
                                    finish();
                                } else {
                                    progressDialog.cancel();
                                    Toast.makeText(getApplicationContext(), "not verified \n", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Glide.with(this).load(uri).override(100, 100).centerCrop().into(propic);
        }
    }

    private void uploadImage() {
        if (uri != null) {
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profilephoto");
            ref.putFile(uri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast.makeText(createaccount.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(createaccount.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.setMessage("Uploaded ");
                                }
                            });
        }
    }
    public void isAvailable(final IsAvailableCallback callback) {
        reference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    user user = dsp.getValue(user.class);
                    if(user.username.equals(username.getText().toString())){
                        isAvailable = true;
                        callback.onAvailableCallback(isAvailable);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onAvailableCallback(isAvailable);
            }
        });
    }
    public interface IsAvailableCallback {
        void onAvailableCallback(boolean isAvailable);
    }
}