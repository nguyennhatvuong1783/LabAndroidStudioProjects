package com.example.namamlich;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btnChuyen);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XemNamAL(view);
            }
        });
    }
    public void XemNamAL(View v) {
        EditText namDL = findViewById(R.id.txtNamDL);
        int nam = Integer.parseInt(namDL.getText().toString());
        Amlich al = new Amlich(nam);
        String amlich = al.getNamAL();
        TextView tv = findViewById(R.id.txtKetqua);
        tv.setText(amlich);
    }
}