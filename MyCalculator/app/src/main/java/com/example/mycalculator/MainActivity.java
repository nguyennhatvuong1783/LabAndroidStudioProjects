package com.example.mycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText soA;
    EditText soB;
    TextView ketQua;
    Button cong;
    Button tru;
    Button nhan;
    Button chia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soA = findViewById(R.id.soa);
        soB = findViewById(R.id.sob);
        ketQua = findViewById(R.id.ketqua);
        cong = findViewById(R.id.cong);
        tru = findViewById(R.id.tru);
        nhan = findViewById(R.id.nhan);
        chia = findViewById(R.id.chia);

        cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(soA.getText().toString()) || TextUtils.isEmpty(soB.getText().toString())){
                    ketQua.setText("Kết quả: Vui lòng nhập số");
                } else {
                    float kq = Float.parseFloat(soA.getText().toString()) + Float.parseFloat(soB.getText().toString());
                    ketQua.setText("Kết quả: " + kq);
                }
            }
        });

        tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(soA.getText().toString()) || TextUtils.isEmpty(soB.getText().toString())){
                    ketQua.setText("Kết quả: Vui lòng nhập số");
                } else {
                    float kq = Float.parseFloat(soA.getText().toString()) - Float.parseFloat(soB.getText().toString());
                    ketQua.setText("Kết quả: " + kq);
                }
            }
        });

        nhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(soA.getText().toString()) || TextUtils.isEmpty(soB.getText().toString())){
                    ketQua.setText("Kết quả: Vui lòng nhập số");
                } else {
                    float kq = Float.parseFloat(soA.getText().toString()) * Float.parseFloat(soB.getText().toString());
                    ketQua.setText("Kết quả: " + kq);
                }
            }
        });

        chia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(soA.getText().toString()) || TextUtils.isEmpty(soB.getText().toString())){
                    ketQua.setText("Kết quả: Vui lòng nhập số");
                } else {
                    float kq = Float.parseFloat(soA.getText().toString()) / Float.parseFloat(soB.getText().toString());
                    ketQua.setText("Kết quả: " + kq);
                }
            }
        });
    }
}