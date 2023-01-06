package com.example.signtalk.ModeSelectionAndNavDrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.signtalk.CommunicationMode.SignToTextMode;
import com.example.signtalk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlphabetActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    MyAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<CategoryClass> AlphabetsList;
    ImageButton BackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabet);
        BackButton = findViewById(R.id.alphabetbackbtn);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("MyFile", 0);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("Check","2");
        editor.commit();

        AlphabetsList = new ArrayList<CategoryClass>();
        AllData();
        recyclerView = findViewById(R.id.alphabetrecyclerview);
        adapter = new MyAdapter(AlphabetsList,AlphabetActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlphabetActivity.this, ModeSelectionMainActivity.class));
                finish();
            }
        });
    }

    public void AllData() {

        databaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (char i = 'A'; i <= 'Z'; i++)
                {
                    CategoryClass categoryClass = new CategoryClass();
                    categoryClass.setName(String.valueOf(i));
                    categoryClass.setVideoid(String.valueOf(i).toLowerCase()+".png");
                    AlphabetsList.add(categoryClass);
                }

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}