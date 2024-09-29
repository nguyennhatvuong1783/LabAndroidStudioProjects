package com.example.intent_bt3;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private LayoutInflater inflater;

    public ContactAdapter(Context context, List<Contact> contactList) {
        super(context, 0, contactList);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.list_item_contact, parent, false);
        }

        Contact contact = getItem(position);

        TextView nameTextView = itemView.findViewById(R.id.nameTextView);
        TextView phoneTextView = itemView.findViewById(R.id.phoneTextView);

        nameTextView.setText(contact.getName());
        phoneTextView.setText(contact.getPhoneNumber());

        nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khai bao intent
                Intent myIntent2 = new Intent(getContext(),MainActivity3.class);
                // khoi dong
                getContext().startActivity(myIntent2);

            }
        });


        return itemView;
    }
}


