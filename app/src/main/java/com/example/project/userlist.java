package com.example.project;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class userlist {
    DatabaseReference reference;
    List<user> userList;

    public List<user> getUserList() {
        userList=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dsp : snapshot.getChildren()){
                    userList.add((user)dsp.getValue(user.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return userList;
    }
}
