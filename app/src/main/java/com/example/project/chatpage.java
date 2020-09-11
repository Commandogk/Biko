package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class chatpage extends AppCompatActivity {
    ImageView p;
    TextView ut;
    FloatingActionButton send;
    EditText message;
    String chatref, uname;
    String ref;
    RecyclerView recyclerView;
    ValueEventListener a = null;
    List<chatmodel> chatmodelList;
     DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/").child("chat");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatpage);
        p = findViewById(R.id.chatpropic);
        ut = findViewById(R.id.chatuser);
        recyclerView = findViewById(R.id.chatlist);
        send = findViewById(R.id.chatmessagesend);
        message = findViewById(R.id.chatmessage);
        Intent chat = getIntent();
        chatmodelList = new ArrayList<>();
        String u = chat.getStringExtra("chatusername");
        String up = chat.getStringExtra("chatuserpro");
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(up);
        final File file;
        try {
            file = File.createTempFile("profile", "jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Glide.with(getApplicationContext()).load(file).circleCrop().into(p);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        ref(new ref_user() {
            @Override
            public void onAvailableCallback( String username) {

                uname = username;
                Intent chat = getIntent();
                String uid = chat.getStringExtra("chatusername");
                final String id = uid + "_" + uname;
                final String id1 = uname + "_" + uid;
                databaseReference.removeEventListener(a);
                getref(new ref1() {
                    @Override
                    public void onAvailableCallback1(String ref) {
                        chatref = ref;
                        loadchat();
                    }
                },id,id1);

            }
        });
        ut.setText(u);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mess = message.getText().toString();
                if (!mess.isEmpty()) {
                    sendmessage(chatref, uname, mess);
                    message.setText("");
                }
            }
        });

//
    }

    private void loadchat() {
        chatmodelList.clear();
        DatabaseReference e = FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/").child("chat/"+chatref);
        e.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    chatmodel chatmodel = new chatmodel();
                    chatmodel = dsp.getValue(chatmodel.class);
                    chatmodelList.add(chatmodel);
                }
                MyAdapter4 myAdapter;
                myAdapter = new MyAdapter4(getApplicationContext(), chatmodelList, uname);
                recyclerView.setAdapter(myAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendmessage(String chatref, String uname, String s) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/").child("chat").child(chatref);
        HashMap<String, String> hashMap = new HashMap<>();
        String t = String.valueOf(Calendar.getInstance().getTimeInMillis());
        hashMap.put("message", s);
        hashMap.put("user", uname);
        reference.child(t).setValue(hashMap);
    }

    public interface ref_user {
        void onAvailableCallback( String username);
    }
    public interface ref1 {
        void onAvailableCallback1( String ref);
    }
    public void ref(final ref_user callback) {
        databaseReference.addValueEventListener(a = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final user user = snapshot.getValue(user.class);
                assert user != null;

                callback.onAvailableCallback(user.username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public  void getref(final ref1 callback, final String id, final String id1){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        if (id.equals(dsp.getKey()) || id1.equals(dsp.getKey())) {
                            if (dsp.getKey().equals(id)) {
                                ref = id;
                            } else {
                                ref = id1;
                            }
                            break;
                        }
                    }
                }

                if (ref == null)
                    ref = id;
                callback.onAvailableCallback1(ref);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
