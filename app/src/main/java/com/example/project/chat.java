package com.example.project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class chat extends Fragment {
    List<user> userList;
    RecyclerView recyclerView ;

    public chat() {
    }
    public static chat newInstance(String param1, String param2) {
        chat fragment = new chat();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = v.findViewById(R.id.list1);
        userList=new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dsp : snapshot.getChildren()){
                    user user = dsp.getValue(user.class);
                    assert user != null;
                    userList.add((user));
                }
                MyAdapter3 myAdapter1 ;
                myAdapter1 = new MyAdapter3(getContext(),userList);
                recyclerView.setAdapter(myAdapter1);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                userList=new ArrayList<>();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;
    }
}