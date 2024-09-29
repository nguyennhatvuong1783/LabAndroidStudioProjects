package com.example.lvnc01;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CityAdapter extends ArrayAdapter<String> {
    Context context;
    String[] items;
    Integer[] thumbs;

    public CityAdapter(Context context, int layoutTobeInflated, String[] items, Integer[] thumbnail) {
        super(context, R.layout.listitem, items);
        this.context = context;
        this.thumbs = thumbnail;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.listitem, null);
        TextView label = (TextView) row.findViewById(R.id.tvCity);
        ImageView icon = (ImageView) row.findViewById(R.id.imgCity);
        label.setText(items[position]);
        icon.setImageResource(thumbs[position]);

        return row;
    }
}
