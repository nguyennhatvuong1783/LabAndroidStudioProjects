package com.example.bai_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class Linear_layout extends AppCompatActivity {

    Toolbar tb;

    SeekBar seekBar;

    TextView tv1, tv2, tv3, tv4, tv5;
    private static final String URL = "https://sites.google.com/view/website-nhom5/trang-ch%E1%BB%A7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear_layout);

        tb = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(tb);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tv4 = findViewById(R.id.tv_4);
        tv5 = findViewById(R.id.tv_5);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int color1 = Color.rgb(255 - i, i, 0);
                int color2 = Color.rgb(i, 255 - i, 0);
                int color3 = Color.rgb(0, i, 255 - i);
                int color4 = Color.rgb(i, 0, 255 - i);
                int color5 = Color.rgb(255 - i, 0, i);

                tv1.setBackgroundColor(color1);
                tv2.setBackgroundColor(color2);
                tv3.setBackgroundColor(color3);
                tv4.setBackgroundColor(color4);
                tv5.setBackgroundColor(color5);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            TextView dialog_title = new TextView(this);
            dialog_title.setText(R.string.dialog_title);
            dialog_title.setGravity(Gravity.CENTER_VERTICAL);
            dialog_title.setPadding(50, 20, 50, 20);
            dialog_title.setTextSize(20);
            dialog_title.setTextColor(ContextCompat.getColor(this, R.color.dialogTitleTextColor)); // Sử dụng màu từ colors.xml
            builder.setCustomTitle(dialog_title);
            builder.setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.setPositiveButton(R.string.visit_web, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent momaIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                    startActivity(momaIntent);
                }
            });

            AlertDialog dialog = builder.show();

            TextView dialog_msg = (TextView) dialog.findViewById(android.R.id.message);
            dialog_msg.setGravity(Gravity.CENTER_HORIZONTAL);
            dialog_msg.setTextSize(14);
            dialog_msg.setPadding(10, 60, 10, 0);

        }
        return super.onOptionsItemSelected(item);
    }
}