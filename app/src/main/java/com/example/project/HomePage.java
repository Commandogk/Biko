package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.project.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {
FloatingActionButton logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        logout=findViewById(R.id.logout1);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
        tabs.getTabAt(1).setIcon(R.drawable.ic_baseline_chat_24);
        tabs.getTabAt(2).setIcon(R.drawable.logo);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("logout",true);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}