package com.example.baitap10;

import static com.example.baitap10.MusicFragment.musics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, androidx.appcompat.widget.SearchView.OnQueryTextListener {

    ViewPager2 viewpager;
    TabLayout tab_Layout;
    public static final String MY_SORT_PREP = "SortOder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        init();
    }

    private void init(){

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.red2));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F1C8B8")));
        }

        viewpager = (ViewPager2) findViewById(R.id.viewPager);
        tab_Layout = (TabLayout) findViewById(R.id.tabLayout);


        FragmentManager fragmentManager = getSupportFragmentManager();
        AdapterViewPaper adapterViewPaper = new AdapterViewPaper(fragmentManager,getLifecycle());
        viewpager.setAdapter(adapterViewPaper);
        getPath(adapterViewPaper);

        tab_Layout.addTab(tab_Layout.newTab().setText("Music"));
        tab_Layout.addTab(tab_Layout.newTab().setText("Album"));
        tab_Layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab != null){
                    viewpager.setCurrentItem(tab.getPosition());
                }

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tab_Layout.selectTab(tab_Layout.getTabAt(position));
            }
        });

    }

    private void getPath(AdapterViewPaper adapterViewPaper){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Bundle");
        String path = bundle.getString("pathFile");
        bundle.putString("Uri",path);
        adapterViewPaper.musicFragment.setArguments(bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem = menu.findItem(R.id.searchOption);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String input = newText.toLowerCase();
        ArrayList<DTOMusic> listSongs = new ArrayList<>();
        for (DTOMusic song: musics) {
            if (song.getTitle().toLowerCase().contains(input)){
                listSongs.add(song);
            }
        }
        MusicFragment.adapterMusic.updateList(listSongs);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor = getSharedPreferences(MY_SORT_PREP,MODE_PRIVATE).edit();
        int id = item.getItemId();
        if (id==R.id.byName){
            editor.putString("sorting","sortByName");
            editor.apply();
            this.recreate();
        } else if (id==R.id.byDate) {
            editor.putString("sorting","sortByDate");
            editor.apply();
            this.recreate();
        }
        return super.onOptionsItemSelected(item);
    }
}