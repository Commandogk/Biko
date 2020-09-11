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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;


public class Post extends AppCompatActivity {
    EditText desc;
    Button upload, post;
    String description,imguri,random;
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageView imageView;
    Uri uri;
    String u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        desc = findViewById(R.id.description);
        imageView = findViewById(R.id.postimage);
        upload = findViewById(R.id.ImageUpload);
        post = findViewById(R.id.post);
        FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        random=UUID.randomUUID().toString();
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://project-923cc.appspot.com");
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/Users/").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), 22);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!desc.getText().toString().isEmpty())
                description=desc.getText().toString();
                else
                    description="Desc";
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user user = snapshot.getValue(user.class);
                        assert user != null;
                         u=user.username;
                        uploadImage();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/").child("Post").child(random);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("desc", description);
                        hashMap.put("postimg",imguri);
                        hashMap.put("username",user.username);
                        reference.setValue(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                startActivity(new Intent(Post.this, HomePage.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
                Glide.with(this).load(uri).into(imageView);
        }
    }
    private void uploadImage()
    {
        if (uri != null) {
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("posts").child(random);
            imguri="gs://project-923cc.appspot.com"+ref.getPath();
            ref.putFile(uri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(Post.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(Post.this, "Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    progressDialog.setMessage(
                                            "Uploaded ");
                                }
                            });
        }
    }
}