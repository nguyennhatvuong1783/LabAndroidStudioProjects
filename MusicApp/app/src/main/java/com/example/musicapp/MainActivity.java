package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.LinearLayout;

import java.sql.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE1 = 1, REQUEST_PERMISSION_CODE2 = 2;
    RecyclerView myRecyclerView;
    static  ArrayList<MusicFiles> musicFiles;
    static Boolean shufferFlag=false, repeatFlag=false;

    public void init(){
        myRecyclerView= (RecyclerView) findViewById(R.id.MyView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecyclerView.setLayoutManager(linearLayoutManager);

            musicFiles = getAllMusic(this);
            Adapter adapter = new Adapter(MainActivity.this,musicFiles);
            myRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayout = findViewById(R.id.MainLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S_V2) {
            if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] permission = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, REQUEST_PERMISSION_CODE1);
                finish();
            }else {
                init();
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            if(checkSelfPermission(android.Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                String[] permission = {Manifest.permission.READ_MEDIA_AUDIO};
                requestPermissions(permission, REQUEST_PERMISSION_CODE2);
                finish();
            }else {
                init();
            }
        }

    }

    public ArrayList<MusicFiles> getAllMusic(Context context){
        ArrayList<MusicFiles> tmp = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String []projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID
        };
        Cursor cursor = context.getContentResolver().query(uri,projection,
                null,null,null);
        if (cursor != null){
            while (cursor.moveToNext()){
                String title = cursor.getString(0);
                String path = cursor.getString(1);
                String duration = cursor.getString(2);
                String artist = cursor.getString(3);
                String id = cursor.getString(4);

                MusicFiles musicFiles = new MusicFiles(path,title,artist,duration,id);
                tmp.add(musicFiles);
            }
            cursor.close();

        }

        return tmp;

    }
}