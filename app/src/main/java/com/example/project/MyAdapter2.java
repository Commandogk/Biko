package com.example.project;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;
import java.util.UUID;

public class MyAdapter2  extends RecyclerView.Adapter<MyAdapter2.MyViewHolder>{
    List<userpost> userposts;
    Context ct;

    public MyAdapter2(Context context,List<userpost> userposts1) {
        ct=  context;
        userposts=userposts1;
    }
    @NonNull
    @Override
    public MyAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.postlayout,parent,false);
        return new MyAdapter2.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter2.MyViewHolder holder, final int position) {
        holder.imageView.setVisibility(View.VISIBLE);
        holder.textView.setText(userposts.get(position).desc);
        holder.textView1.setText(userposts.get(position).username);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-923cc.firebaseio.com/Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dsp : snapshot.getChildren()){
                    user user = dsp.getValue(user.class);
                    if(userposts.get(position).username.equals(user.username)){
                        StorageReference referenceFromUrl = FirebaseStorage.getInstance().getReferenceFromUrl(user.propic);
                        final File file;
                        try {
                            file = File.createTempFile("pro"+position,"jpg");
                            referenceFromUrl.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Glide.with(ct).load(file).circleCrop().into(holder.view);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(userposts.get(position).postimg);
                        final File file1;
                        try {
                            file1 = File.createTempFile("post","jpg");
                            storageReference.getFile(file1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Glide.with(ct).load(file1).override(200, 200).centerCrop().into(holder.imageView);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       holder.entercomment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String comment = holder.comment.getText().toString();
               if(comment.isEmpty())
                   Toast.makeText(ct,"Add Your Comment",Toast.LENGTH_SHORT).show();
               else{
                 String  random= UUID.randomUUID().toString();
               }
           }
       });
    }

    @Override
    public int getItemCount() {
        return userposts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,view;
        TextView textView,textView1;
        EditText comment;
        FloatingActionButton entercomment;
        RecyclerView commentlist;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=((View)itemView).findViewById(R.id.imageView4);
            comment=((View)itemView).findViewById(R.id.addcommentedit);
            entercomment=((View)itemView).findViewById(R.id.sendcomment);
            commentlist=((View)itemView).findViewById(R.id.commentlist);
            view=((View)itemView).findViewById(R.id.smallprofile);
            textView1 =((View)itemView).findViewById(R.id.users);
            textView =((View)itemView).findViewById(R.id.description1);
        }
    }
}
