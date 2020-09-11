package com.example.project;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.CookieStore;
import java.util.ArrayList;
import java.util.List;

public class profile extends Fragment {
    ImageView propic;
    TextView username,bio;
    DatabaseReference reference;
    FirebaseAuth auth;
    Button  a;
    List<user> users;
    List<userpost> userpostList;
    RecyclerView recyclerView ;
    public profile() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_profile, container, false);
        auth = FirebaseAuth.getInstance();
        propic=v.findViewById(R.id.propic);
        username=v.findViewById(R.id.username1);
        a = v.findViewById(R.id.edit);
        bio=v.findViewById(R.id.bio1);
        recyclerView = v.findViewById(R.id.list);
        String id =auth.getCurrentUser().getUid();
        users=new ArrayList<>();
        userpostList = new ArrayList<>();
        String url="https://project-923cc.firebaseio.com/Users/"+id;
        reference=FirebaseDatabase.getInstance().getReferenceFromUrl(url);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfile.class));
                getActivity().finish();
            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StorageReference storageReference;
                user user = snapshot.getValue(user.class);
                assert user != null;
                String u=user.username;
                String b=user.bio;
                String n =user.name;
                storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(user.propic);
                final File file;
                try {
                    file = File.createTempFile("profile","jpg");
                    storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Glide.with(getContext()).load(file).circleCrop().into(propic);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                username.setText(u);
                bio.setText(b);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       userlist obj = new userlist();
       users=obj.getUserList();
        MyAdapter myAdapter ;
        myAdapter = new MyAdapter(getContext(),users);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        users=new ArrayList<>();
        return v;
    }
}