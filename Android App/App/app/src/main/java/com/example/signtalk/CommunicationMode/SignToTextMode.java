package com.example.signtalk.CommunicationMode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;

import com.example.signtalk.ModeSelectionAndNavDrawer.ModeSelectionMainActivity;
import com.example.signtalk.R;

public class SignToTextMode extends AppCompatActivity {

    ImageButton BackButton;
    private static int Camera_Permission_Code=100;
    private static int Video_Record_Code=101;
    private static int Image_Capture_Code=102;
    private Uri videoPath;
    VideoView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_to_text_mode);

        BackButton = findViewById(R.id.btnbacksigntotext);
        view = findViewById(R.id.videoview);


        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignToTextMode.this, ModeSelectionMainActivity.class));
                finish();
            }
        });

        if(isCameraPresentInPhone())
        {
            Log.i("Record_Video_Tag","Camera is Detected");
            getCameraPermission();
        }
        else{
            Log.i("Record_Video_Tag","No Camera is detected");
        }
    }
    public void recordVideoButtonPressed(View view){
        RecordVideo();

    }
    private boolean isCameraPresentInPhone()
    {
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        }
        else
        {
            return false;
        }
    }
    private void getCameraPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},Camera_Permission_Code);
        }

    }
    private void RecordVideo(){
        Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        startActivityForResult(intent,Video_Record_Code);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Video_Record_Code)
        {if(resultCode==RESULT_OK)
        {
            videoPath=data.getData();
            view.setVideoPath(String.valueOf(videoPath));
            view.start();
            Log.i("Record_Video_Tag","Video is recorded and available at path" +videoPath);
        }
        else if(resultCode==RESULT_CANCELED)
        {
            Log.i("Record_Video_Tag","Recording Video is cancelled" +videoPath);
        }
        else
        {
            Log.i("Record_Video_Tag","Recording Video has error" +videoPath);
        }

        }
    }
}