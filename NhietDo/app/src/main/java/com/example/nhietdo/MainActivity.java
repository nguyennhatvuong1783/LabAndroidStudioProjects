package com.example.nhietdo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public EditText etF, etC;
    public Button btnF2C, btnC2F, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etF = findViewById(R.id.txtDoF);
        etC = findViewById(R.id.txtDoC);
        btnF2C = findViewById(R.id.btnFtoC);
        btnC2F = findViewById(R.id.btnCtoF);
        btnClear = findViewById(R.id.button2);

        btnF2C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double doF = Double.parseDouble(etF.getText().toString());
                convert cv = new convert();
                cv.setF(doF);
                cv.convertFtoC();
                etC.setText(String.valueOf(cv.getC()));
            }
        });

        btnC2F.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double doC = Double.parseDouble(etC.getText().toString());
                convert cv = new convert();
                cv.setC(doC);
                cv.convertCtoF();
                etF.setText(String.valueOf(cv.getF()));
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etF.setText("");
                etC.setText("");
            }
        });
    }
}