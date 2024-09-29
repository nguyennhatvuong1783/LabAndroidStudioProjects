package com.example.bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText hoten, chieucao, cannang, chisobmi, chandoan;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hoten = findViewById(R.id.txtTen);
        chieucao = findViewById(R.id.txtChieucao);
        cannang = findViewById(R.id.txtCannang);
        chisobmi = findViewById(R.id.txtBMI);
        chandoan = findViewById(R.id.txtChandoan);
        bt = findViewById(R.id.btnTinh);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tinh();
            }
        });
    }

    public void tinh() {
        double h = Double.parseDouble(chieucao.getText().toString());
        double w = Double.parseDouble(cannang.getText().toString());
        bmiclass obj = new bmiclass(h,w);
        double bmi = obj.getBMI();
        String kq = obj.getChanDoan();
        chisobmi.setText(String.valueOf(bmi));
        chandoan.setText(kq);
    }
}