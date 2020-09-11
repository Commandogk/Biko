package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class EditProfile extends AppCompatActivity {
    EditText  name1,  bio1;
    Button update1, upload1;
    ImageView propic1;
    FirebaseAuth auth;
    Uri uri;
    DatabaseReference reference;
    StorageReference storageReference;
    String name2, bio2,pro1 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        name1 = findViewById(R.id.editTextTextPersonName7);
        bio1 = findViewById(R.id.bio7);
        update1 = findViewById(R.id.update7);
        propic1 = findViewById(R.id.imageView7);
        upload1 = findViewById(R.id.upload7);
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://project-923cc.appspot.com");
        final String userid = user.getUid();
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/").child("Users").child(userid);
        reference.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    name1.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("bio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bio1.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://project-923cc.appspot.com/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+"profilephoto");
        final File file;
        try {
            file = File.createTempFile("profile","jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Glide.with(getApplicationContext()).load(file).override(100, 100).centerCrop().into(propic1);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        upload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), 22);
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
        update1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    name2 = name1.getText().toString();
                    bio2 = bio1.getText().toString();
                    progressDialog.setMessage("updating");
                    progressDialog.show();

                                Toast.makeText(getApplicationContext(), "updated+\n", Toast.LENGTH_SHORT).show();
                                pro1="gs://project-923cc.appspot.com/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+"profilephoto";
                                reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/").child("Users").child(userid);
                                reference.child("name").setValue(name2);
                                reference.child("bio").setValue(bio2);
                                progressDialog.cancel();
                                Toast.makeText(getApplicationContext(), "successfully updated \n", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditProfile.this, HomePage.class));
                                finish();
                            }
            }
        });
    }
    private void uploadImage()
    {
        if (uri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profilephoto");
            ref.putFile(uri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfile.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    progressDialog.setMessage("Uploaded ");
                                }
                            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Glide.with(this).load(uri).override(100, 100).centerCrop().into(propic1);
            uploadImage();
        }
    }
}


