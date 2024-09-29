package com.example.bt05;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE1 = 1, REQUEST_PERMISSION_CODE2 = 2;
    public RecyclerView recyclerView;
    private ArrayList<Music> listMusic = new ArrayList<Music>();

    public void init() {
        getMusicFromDevice();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        Adapter adapter = new Adapter(this, listMusic);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S_V2) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, REQUEST_PERMISSION_CODE1);
                finish();
            }else {
                init();
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            if(checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                String[] permission = {Manifest.permission.READ_MEDIA_AUDIO};
                requestPermissions(permission, REQUEST_PERMISSION_CODE2);
                finish();
            }else {
                init();
            }
        }
    }

    public void getMusicFromDevice() {
        String[] strings = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA};

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, strings, null, null, null);

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    int audioIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                    int audioId = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    String nameMusic = cursor.getString(audioIndex);
                    String locationMusic = cursor.getString(audioId);

                    listMusic.add(new Music(nameMusic, locationMusic, R.drawable.play_25));
                } while(cursor.moveToNext());
            }
        }
        cursor.close();
    }
}