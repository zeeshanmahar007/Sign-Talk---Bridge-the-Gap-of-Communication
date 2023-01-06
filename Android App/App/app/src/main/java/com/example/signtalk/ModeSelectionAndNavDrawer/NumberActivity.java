package com.example.signtalk.ModeSelectionAndNavDrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.signtalk.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NumberActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    MyAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<CategoryClass> NumberList;
    ImageButton BackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        BackButton = findViewById(R.id.numberbackbtn);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("MyFile", 0);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("Check","1");
        editor.commit();

        NumberList = new ArrayList<CategoryClass>();
        AllData();
        recyclerView = findViewById(R.id.numberrecyclerview);
        adapter = new MyAdapter(NumberList,NumberActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NumberActivity.this, ModeSelectionMainActivity.class));
                finish();
            }
        });
    }

    public void AllData() {
        databaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (int i = 1; i <= 10; i++)
                {
                    CategoryClass categoryClass = new CategoryClass();
                    categoryClass.setName(String.valueOf(i));
                    categoryClass.setVideoid(i +".png");
                    NumberList.add(categoryClass);
                }

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}