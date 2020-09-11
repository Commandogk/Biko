package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    List<user> userList;
    Context ct;
    public MyAdapter(Context context,List<user> userList1) {
        ct=  context;
        userList=userList1;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.user_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.MyViewHolder holder, int position) {
        holder.Us.setVisibility(View.VISIBLE);
        user user=userList.get(position);
        holder.Us.setText(user.username);
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(userList.get(position).propic);
        final File file;
        try {
            file = File.createTempFile("profile","jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Glide.with(ct).load(file).circleCrop().into(holder.view);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Us;
        ImageView view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Us=((View)itemView).findViewById(R.id.username2);
            view=((View)itemView).findViewById(R.id.imageView3);
        }
    }
}
