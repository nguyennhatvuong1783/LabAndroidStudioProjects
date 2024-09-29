package com.example.baitap10;

import static com.example.baitap10.MusicActivity.MY_SORT_PREP;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MusicFragment extends Fragment {

    public static ArrayList<DTOMusic> musics = new ArrayList<>();
    public static ArrayList<DTOMusic> albums;
    public static ArrayList<String> duplicate ;
    static AdapterMusic adapterMusic;
    RecyclerView myView;
    String path;

    static Boolean shufferFlag=false, repeatFlag=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        myView =  view.findViewById(R.id.viewMusic);

        Bundle bundle = getArguments();
        if (bundle!=null){
            path = bundle.getString("Uri");

            musics = getListMusic();
            adapterMusic = new AdapterMusic(getContext(),musics);
            myView.setLayoutManager(new LinearLayoutManager(getContext()));
            myView.setAdapter(adapterMusic);

        }
        return view;
    }


    private boolean equalPath(String pathFile){
        File root = new File(path);
        File[] fileMusic = root.listFiles();
        String filePath = new String();
        if (fileMusic==null || fileMusic.length==0){
            return false;
        } else {
            for (int i =0; i<fileMusic.length ; i++){
                if (fileMusic[i].isDirectory()==false){
                    filePath = fileMusic[i].getAbsolutePath();
                    String compare = filePath.substring(filePath.lastIndexOf(".")).toLowerCase();
                    if (compare.equals(".mp3") || compare.equals(".wav") || compare.equals(".ogg")){
                        if (filePath.equals(pathFile)){
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }


    private ArrayList<DTOMusic> getListMusic(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences( MY_SORT_PREP, Context.MODE_PRIVATE);
        String sortOder = sharedPreferences.getString("sorting","sortByName");
        ArrayList<DTOMusic> tmp = new ArrayList<>();
        albums = new ArrayList<>();
        duplicate =  new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String order = null;
        switch (sortOder){
            case "sortByName":
                order = MediaStore.MediaColumns.DISPLAY_NAME + " ASC";
                break;
            case "sortByDate":
                order = MediaStore.MediaColumns.DATE_ADDED + " ASC";
        }
        String []projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM
        };
        Cursor cursor = getContext().getContentResolver().query(uri,projection,
                null,null,order);
        if (cursor != null){
            while (cursor.moveToNext()){
                String title = cursor.getString(0);
                String path1 = cursor.getString(1);
                String duration = cursor.getString(2);
                String artist = cursor.getString(3);
                String id = cursor.getString(4);
                String album = cursor.getString(5);

                if (equalPath(path1)==true){
                    DTOMusic musicFiles = new DTOMusic(path1,title,artist,duration,id,album);
                    tmp.add(musicFiles);
                    if (duplicate.contains(album)==false){
                        albums.add(musicFiles);
                        duplicate.add(album);
                    }
                }
            }
            cursor.close();

        }
        return tmp;
    }



}