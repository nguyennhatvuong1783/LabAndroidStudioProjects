package com.example.lvnc01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String[] items ={"Hà Nội", "TP. Hồ Chí Minh","Đà Nẵng", "Hải Phòng", "Cần Thơ"};
    Integer[] thumb ={R.drawable.city_icon,R.drawable.city_buildings,R.drawable.city_icon,R.drawable.city_buildings,R.drawable.city_icon };
    ListView lvCity;
    CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCity = (ListView) findViewById(R.id.mylist);
        adapter = new CityAdapter(this, R.layout.listitem, items, thumb);
        lvCity.setAdapter(adapter);
//        _myList.setBackgroundColor(Color.BLUE);
        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = items[position];
                thongbao(item);
            }
        });
    }

    public void thongbao(String item) {
        Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
    }
}