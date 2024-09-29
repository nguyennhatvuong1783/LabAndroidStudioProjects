package com.example.contentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText e1, e2;
    ContentValues values = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1 = (EditText) findViewById(R.id.edtName);
        e2 = (EditText) findViewById(R.id.edtJobProfile);
    }

    public void doSaveContent(View view) {
        values.put("emp_name", e1.getText().toString());
        values.put("profile", e2.getText().toString());

        Uri uri = getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
    }

    public void doLoadContent(View view) {
        Cursor cr = getContentResolver().query(MyContentProvider.CONTENT_URI, null, null, null, "_id");
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