package com.example.intent_bt3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button call_contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        call_contact = findViewById(R.id.call_contact);
        call_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khai bao intent
                Intent myIntent = new Intent(MainActivity.this,MainActivity2.class);
                // khoi dong
                startActivity(myIntent);
            }
        });
    }
}