package com.example.maplocation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button btn;
    EditText edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        edt = findViewById(R.id.edt);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String ad = edt.getText().toString();
                    ad = ad.replace(" ", "+");

                    Intent geo = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + ad));

                    startActivity(geo);
                } catch (Exception e) {
                    Log.e("TAG", e.toString());
                }
            }
        });
    }
}