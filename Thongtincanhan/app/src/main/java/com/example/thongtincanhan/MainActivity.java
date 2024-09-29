package com.example.thongtincanhan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText hoten, cmnd;
    RadioGroup trinhdo, sothich;
    Button gui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hoten = findViewById(R.id.txtHoten);
        cmnd = findViewById(R.id.txtCMND);
        trinhdo = findViewById(R.id.rdgTrinhdo);
        sothich = findViewById(R.id.rdgSothich);
        gui = findViewById(R.id.gui);

        gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HienthiThongtin();
            }
        });
    }

    public void HienthiThongtin()
    {
        thongtinClass tt;
        String _hoten = hoten.getText().toString();
        String _cmnd = cmnd.getText().toString();
        String _sothich = "", _trinhdo = "";

        if(trinhdo.getCheckedRadioButtonId() == R.id.rdTrcap) {
            _trinhdo = "Trung cấp";
        }else if(trinhdo.getCheckedRadioButtonId() == R.id.rdCaodang) {
            _trinhdo = "Cao đẳng";
        }else {
            _trinhdo = "Đại học";
        }

        if(sothich.getCheckedRadioButtonId() == R.id.rdDocbao) {
            _sothich = "Đọc báo";
        }else if(sothich.getCheckedRadioButtonId() == R.id.rdDocsach) {
            _sothich = "Đọc sách";
        }else {
            _sothich = "Đọc coding";
        }

        tt = new thongtinClass(_hoten, _cmnd, _sothich, _trinhdo);
        Toast t = Toast.makeText(this, tt.toString(), Toast.LENGTH_LONG);
        t.show();
    }
}