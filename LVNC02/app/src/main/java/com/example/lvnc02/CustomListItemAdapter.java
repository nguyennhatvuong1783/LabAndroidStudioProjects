package com.example.lvnc02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListItemAdapter extends ArrayAdapter<ItemsList> {
    Context context;
    ItemsList[] items;
    public CustomListItemAdapter(Context context, int layoutTobeInflated, ItemsList[] items) {
        super(context, R.layout.listitem1, items);
        this.context = context;
        this.items = items;
    }

    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.foodName) {
//                TextView fn = (TextView)v;
                String fl = (String) v.getTag();
                Toast.makeText(context, fl.toString(), Toast.LENGTH_SHORT).show();
                showView(fl.toString());
            }else if(v.getId() == R.id.foodImage) {
                ImageView fi = (ImageView) v;
                String floc = (String) fi.getTag();
                Toast.makeText(context, floc.toString(), Toast.LENGTH_SHORT).show();
                showView(floc.toString());
            }
        }
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.listitem1, null);
        TextView foodName = (TextView) row.findViewById(R.id.foodName);
        ImageView foodImage = (ImageView) row.findViewById(R.id.foodImage);
        TextView foodLink = (TextView)row.findViewById(R.id.foodLink);
        TextView foodLocation = (TextView)row.findViewById(R.id.foodLocation);
        foodName.setText(items[position].getFoodName().toString());
        foodLink.setText(items[position].getFoodLink().toString());
        foodName.setTag(foodLink.getText().toString());
        foodImage.setImageResource(items[position].getFoodImage());
        foodLocation.setText(items[position].getFoodLocation().toString());
        foodImage.setTag(foodLocation.getText().toString());
        foodName.setOnClickListener(itemClickListener);
        foodImage.setOnClickListener(itemClickListener);

        return row;
    }

    private void showView(String v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(v));
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
    }
}
