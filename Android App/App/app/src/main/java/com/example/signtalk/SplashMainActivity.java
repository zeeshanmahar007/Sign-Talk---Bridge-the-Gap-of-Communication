package com.example.signtalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.signtalk.ModeSelectionAndNavDrawer.ModeSelectionMainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashMainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_main);

        progressBar = findViewById(R.id.splashprogressbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Thread thread=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(1500);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                    {
                        Intent intent=new Intent(SplashMainActivity.this, ModeSelectionMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent=new Intent(SplashMainActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }


                }
            }
        };
        thread.start();
    }
}