package com.example.where;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class New_toilet extends AppCompatActivity {

    private static final String LOG_TAG = New_toilet.class.getName();

    DatabaseReference mMarkers;
    long maxId=0;
    String[] countries = {"Автомат для питьевой воды", "Магазин", "Аптека", "Туалет", "Круглосуточный магазин"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_toilet);

        TextView categories_text = (TextView) findViewById(R.id.categories_text);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String)adapterView.getItemAtPosition(i);
                categories_text.setText(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

        Button add_new_toilet = (Button) findViewById(R.id.add_toilet);
        mMarkers = FirebaseDatabase.getInstance().getReference().child("Markers");
        mMarkers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxId = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        add_new_toilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText Address_edit_text = (EditText) findViewById(R.id.Address_edit_text);
                String Address = Address_edit_text.getText().toString();

                EditText description_text_view = (EditText) findViewById(R.id.Description_edit_text);
                String description = description_text_view.getText().toString();

                TextView categories_view = (TextView) findViewById(R.id.categories_text);
                String categories = categories_view.getText().toString();

                AddMarker(Address, categories, description);
            }
        });
    }

    private void AddMarker(String address, String categories, String description) {
        HashMap hashMap = new HashMap();
        hashMap.put("address", address);
        hashMap.put("categories", categories);
        hashMap.put("description", description);
        mMarkers.child(String.valueOf(maxId + 1)).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(New_toilet.this, "Маркер добавлен", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(New_toilet.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(New_toilet.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
