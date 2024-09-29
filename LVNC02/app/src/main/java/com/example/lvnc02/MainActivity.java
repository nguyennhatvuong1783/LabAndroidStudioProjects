package com.example.lvnc02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    ListView lst;
    ItemsList[] items;
    CustomListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareData();
        adapter = new CustomListItemAdapter(this, R.layout.listitem1, items);
        lst = (ListView)findViewById(R.id.lstTour);
        lst.setAdapter(adapter);
    }

    private void prepareData() {
        items = new ItemsList[2];
        items[0] = new ItemsList("Quán ăn ngon Sài Gòn", "http://www.google.com",
                R.drawable.food, "geo:0,0?q=14+Đồng+Đen+phường+14+Hồ+Chí+Minh+Bến+Nghé");
        items[1] = new ItemsList("Quán Nướng Việt", "http://123-zo.vn",
        R.drawable.food, "geo:10.7588455,106.6850891");
    }
}