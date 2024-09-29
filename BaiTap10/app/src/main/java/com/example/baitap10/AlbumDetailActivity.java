package com.example.baitap10;

import static com.example.baitap10.MusicFragment.musics;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;

public class AlbumDetailActivity extends AppCompatActivity {
    RecyclerView myRecyclerView;
    ImageView album_Photo;
    String albumName;
    ArrayList<DTOMusic> listSongs = new ArrayList<>();
    AdapterAlbumDetails adapterAlbumDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F1C8B8")));
        }
        myRecyclerView = findViewById(R.id.recyclerView);
        album_Photo = findViewById(R.id.albumPhoto);
        albumName = getIntent().getStringExtra("albumName");
        int j = 0;
        for (int i =0; i<musics.size(); i++){
            if (albumName.equals(musics.get(i).getAlbumn())){
                listSongs.add(j,musics.get(i));
                j++;
            }
        }
        try {
            byte[] img = getAlbumnArt(listSongs.get(0).getPath());
            if (img!=null){
                Glide.with(this).load(img).into(album_Photo);
            }else {
                Glide.with(this).load(R.drawable.music_basic).into(album_Photo);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(listSongs.size()<1)){
            adapterAlbumDetails = new AdapterAlbumDetails(this,listSongs);
            myRecyclerView.setAdapter(adapterAlbumDetails);
            myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private byte[] getAlbumnArt(String uri) throws IOException {
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(uri);
        byte[] arr = metadataRetriever.getEmbeddedPicture();
        metadataRetriever.release();
        return arr;
    }
}