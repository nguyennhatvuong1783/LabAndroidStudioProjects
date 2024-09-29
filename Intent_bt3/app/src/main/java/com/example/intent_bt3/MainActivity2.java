package com.example.intent_bt3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private ListView contactsListView;
    private ContactAdapter contactAdapter;
    private List<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        contactsListView = findViewById(R.id.contactsListView);

        contactList = new ArrayList<>();
        contactList.add(new Contact("Nam", "0123456789"));
        contactList.add(new Contact("Lan", "9876543210"));
        contactList.add(new Contact("Diễm", "0123456289"));
        contactList.add(new Contact("Hiếu", "09876543230"));

        contactAdapter = new ContactAdapter(this, contactList);
        contactsListView.setAdapter(contactAdapter);
    }
}
