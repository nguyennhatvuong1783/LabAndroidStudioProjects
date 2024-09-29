package com.example.baitap10;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class SecondActivity extends AppCompatActivity {
    RecyclerView myView2;
    TextView txtNote2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }

        myView2 = (RecyclerView) findViewById(R.id.myRecyclerView2);
        txtNote2 = (TextView) findViewById(R.id.textView2);

        Intent intent = getIntent();
        String path = intent.getStringExtra("pathAbsolute");
        File root = new File(path);
        File[] fileAndFolders = root.listFiles();
        if (fileAndFolders==null || fileAndFolders.length==0){
            txtNote2.setVisibility(View.VISIBLE);
            return;
        }

        myView2.setLayoutManager(new LinearLayoutManager(this));
        myView2.setAdapter(new Adapter(this,fileAndFolders));

    }
}