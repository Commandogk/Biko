package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

public class slider extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public slider(Context context){
        this.context=context;
    }
    public String[] lst_title = {
            "IMAGE POST",
            "CHAT WITH FRIENDS",
            "HAVE CUSTOM PROFILE"
    }   ;

    public String[] lst_description = {
            "Post Images ,",
            "Chat with Your friends in realtime in a secure medium,",
            "Have Custom Profile with picture and Bio,"
    };
    public  int[] slideimg ={ R.drawable.imagepost,R.drawable.chatslide,R.drawable.profileslide};
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide, container, false);
        ImageView imageView = view.findViewById(R.id.slideimg);
        TextView textView = view.findViewById(R.id.head);
        TextView textView1 = view.findViewById(R.id.description2);
        Glide.with(context).load(slideimg[position]).into(imageView);
        textView.setText(lst_title[position]);
        textView1.setText(lst_description[position]);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
