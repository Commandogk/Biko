package com.example.project;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class CurrentUser {
    user currentuser;
    public user getCurrentuser() {
        final DatabaseReference reference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/Users/").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user user = snapshot.getValue(user.class);
                assert user != null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return currentuser;
    }
}
