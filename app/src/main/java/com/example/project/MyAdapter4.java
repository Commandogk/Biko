package com.example.project;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MyAdapter4 extends RecyclerView.Adapter<MyAdapter4.MyViewHolder>{
    List<chatmodel> chatmodels;
    Context ct;
    String currentuser;
    public MyAdapter4(Context context,List<chatmodel> chatmodels,String currentuser) {
        ct=  context;
        this.currentuser=currentuser;
       this.chatmodels=chatmodels;
    }
    @NonNull
    @Override
    public MyAdapter4.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.chatmessage,parent,false);
        return new MyAdapter4.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter4.MyViewHolder holder, final int position) {
        holder.cmessage.setText(chatmodels.get(position).message);
        if(chatmodels.get(position).user.equals(currentuser)){
            RelativeLayout.LayoutParams textViewLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            textViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.cmessage.setLayoutParams(textViewLayoutParams);
        }
        else{
            RelativeLayout.LayoutParams textViewLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            textViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.cmessage.setLayoutParams(textViewLayoutParams);
            holder.cmessage.setBackground(ct.getResources().getDrawable(R.drawable.roundrect));
        }
    }

    @Override
    public int getItemCount() {
        return chatmodels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cmessage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cmessage =((View)itemView).findViewById(R.id.messagel);
        }
    }
}
