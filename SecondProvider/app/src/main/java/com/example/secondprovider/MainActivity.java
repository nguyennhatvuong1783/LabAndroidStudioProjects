package com.example.secondprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE1 = 1;
    String AUTHORITY = "com.example.contentprovider";
    Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/emp");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doLoading(View view) {
        Cursor cr = getContentResolver().query(CONTENT_URI, null, null, null, null);
        StringBuilder stringBuilder = new StringBuilder();

        if(cr != null) {
            while(cr.moveToNext()) {
                int id = cr.getInt(0);
                String s1 = cr.getString(1);
                String s2 = cr.getString(2);
                stringBuilder.append(id + "    " + s1 + "    " + s2 + "\n");
            }

            Toast.makeText(this, stringBuilder, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
        }
    }
}