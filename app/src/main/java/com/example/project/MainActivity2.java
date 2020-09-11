package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity  {
    ViewPager viewPager;
    slider slider;
    LinearLayout linearLayout;
    int c;
    Button nxt, bck;
    TextView[] dots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        linearLayout = findViewById(R.id.linearl);
        viewPager = findViewById(R.id.slideview);
        slider = new slider(this);
        viewPager.setAdapter(slider);
        nxt = findViewById(R.id.next);
        bck = findViewById(R.id.back);
        setDots(0);
        viewPager.addOnPageChangeListener(listener);
        if(opened()){
            Intent b =new Intent(MainActivity2.this, MainActivity.class);
            b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            b.putExtra("first",opened());
            startActivity(b);
            finish();
        }
        else{
            SharedPreferences.Editor editor = getSharedPreferences("slide",MODE_PRIVATE).edit();
            editor.putBoolean("opened",true);
            editor.commit();
        }
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c <2)
                    viewPager.setCurrentItem(c + 1);
                else {
                    Intent b =new Intent(MainActivity2.this, MainActivity.class);
                    b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    b.putExtra("first",opened());
                    startActivity(b);
                    finish();
                }
            }
        });
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c != 0)
                    viewPager.setCurrentItem(c - 1);
            }
        });
    }

    private boolean opened() {
        SharedPreferences sharedPreferences =getSharedPreferences("slide",MODE_PRIVATE);
        boolean open = sharedPreferences.getBoolean("opened" +
                "",false);
        return open;
    }

    public void setDots(int p) {
        dots = new TextView[3];
        linearLayout.removeAllViews();
        for (int i = 0; i < 3; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(".");
            dots[i].setGravity(Gravity.CENTER);
            dots[i].setTextColor(Color.parseColor("#12b3e9"));
            dots[i].setTextSize(40);
            linearLayout.addView(dots[i]);
        }
        dots[p].setTextColor(Color.parseColor("#ffffff"));

    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setDots(position);
            c = position;
            if (position == 0) {
                nxt.setEnabled(true);
                nxt.setVisibility(View.VISIBLE);
                nxt.setText("Next");
                bck.setText(" ");
                bck.setEnabled(false);
                bck.setVisibility(View.INVISIBLE);
            } else if (position == 2) {
                nxt.setEnabled(true);
                nxt.setVisibility(View.VISIBLE);
                nxt.setText("Finish");
                bck.setText("back");
                bck.setEnabled(true);
                bck.setVisibility(View.VISIBLE);
            } else {
                nxt.setEnabled(true);
                nxt.setVisibility(View.VISIBLE);
                nxt.setText("Next");
                bck.setText("back");
                bck.setEnabled(true);
                bck.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
