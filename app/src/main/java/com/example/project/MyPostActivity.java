package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyPostActivity extends Fragment {
    FloatingActionButton addpost;
    RecyclerView postlist;
    DatabaseReference reference, databaseReference;
    List<userpost> userpostList;


    public MyPostActivity() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        addpost = v.findViewById(R.id.addpost);
        postlist = v.findViewById(R.id.postcycle);
        userpostList = new ArrayList<>();
        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Post.class));
            }
        });
        loadpost();
        return v;
    }

    private void loadpost() {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/Post");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    userpost userposts = new userpost();
                    userposts = dsp.getValue(userpost.class);
                    userpostList.add(userposts);
                }
                MyAdapter2 myAdapter;
                myAdapter = new MyAdapter2(getContext(), userpostList);
                postlist.setAdapter(myAdapter);
                postlist.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

