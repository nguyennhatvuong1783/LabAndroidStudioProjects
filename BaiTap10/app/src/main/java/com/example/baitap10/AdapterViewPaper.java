package com.example.baitap10;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class AdapterViewPaper extends FragmentStateAdapter {

    MusicFragment musicFragment = new MusicFragment();
    AlbumFragment albumFragment = new AlbumFragment();

    public AdapterViewPaper(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0){
            return musicFragment;
        }else {
            return albumFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
