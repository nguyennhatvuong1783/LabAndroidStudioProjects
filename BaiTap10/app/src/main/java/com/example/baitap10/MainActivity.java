package com.example.baitap10;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE1 = 1, REQUEST_PERMISSION_CODE2 = 2;
    public RecyclerView myView;
    public TextView txtNote;
    public File[] allFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S_V2) {
            if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] permission = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, REQUEST_PERMISSION_CODE1);
                finish();
            }else {
                init();
                allFile = getAllFiles();
                if (allFile != null){
                    myView.setLayoutManager(new LinearLayoutManager(this));
                    myView.setAdapter(new Adapter(MainActivity.this,allFile));
                }

            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            if(checkSelfPermission(android.Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                String[] permission = {Manifest.permission.READ_MEDIA_AUDIO};
                requestPermissions(permission, REQUEST_PERMISSION_CODE2);
                finish();
            }else {
                init();
                allFile = getAllFiles();
                if (allFile != null){
                    myView.setLayoutManager(new LinearLayoutManager(this));
                    myView.setAdapter(new Adapter(MainActivity.this,allFile));
                }
            }
        }
    }

    private void init(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }
        myView = (RecyclerView) findViewById(R.id.myRecyclerView);
        txtNote = (TextView) findViewById(R.id.textView1);
    }

    private File[] getAllFiles(){
        String path = Environment.getExternalStorageDirectory().getPath();
        File fileRoot = new File(path);
        File[] filesAndFolders = fileRoot.listFiles();
        return filesAndFolders;
    }

}