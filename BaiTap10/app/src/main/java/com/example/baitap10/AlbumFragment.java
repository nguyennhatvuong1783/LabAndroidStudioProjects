package com.example.baitap10;

import static com.example.baitap10.MusicFragment.albums;
import static com.example.baitap10.MusicFragment.musics;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AlbumFragment extends Fragment {
    RecyclerView myView;
    AdapterAlbum adapterAlbum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        myView = view.findViewById(R.id.myRecyclerAlbumn);
        adapterAlbum = new AdapterAlbum(getContext(),albums);
        myView.setAdapter(adapterAlbum);
        myView.setLayoutManager(new GridLayoutManager(getContext(),2));
        return view;
    }
}