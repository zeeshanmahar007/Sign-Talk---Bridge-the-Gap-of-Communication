package com.example.signtalk.ModeSelectionAndNavDrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signtalk.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class ShowCategorySignActivity extends AppCompatActivity {

    TextView textView;
    ImageView imageView;
    private StorageReference imgRef;
    ImageButton imageButton;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alphabet);
        textView = findViewById(R.id.txtcategorytext);
        imageView = findViewById(R.id.imgcategoryimage);
        imageButton = findViewById(R.id.btnbackalphabet);

        SharedPreferences sharedPreferences=getSharedPreferences("MyFile",0);
        String Name=sharedPreferences.getString("Name","");
        String id=sharedPreferences.getString("VideoId","");
        String check = sharedPreferences.getString("Check","");
        textView.setText(Name);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.matches("1"))
                {
                    startActivity(new Intent(ShowCategorySignActivity.this, NumberActivity.class));
                    finish();
                }
                else if(check.matches("2"))
                {
                    startActivity(new Intent(ShowCategorySignActivity.this, AlphabetActivity.class));
                    finish();
                }

            }
        });

        if(check.matches("1"))
        {
            StorageReference storageRef =
                    FirebaseStorage.getInstance().getReference();
            imgRef = storageRef.child("/Numbers/"+id);
        }
        else if(check.matches("2"))
        {
            StorageReference storageRef =
                    FirebaseStorage.getInstance().getReference();
            imgRef = storageRef.child("/Alphabets/"+id);
        }

        try{
            final File localFile = File.createTempFile(id, "png");

            imgRef.getFile(localFile).addOnSuccessListener(
                    new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess
                                (FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // for video
                            final ImageView imgview= findViewById(R.id.imgcategoryimage);
                            imgview.setImageURI(Uri.fromFile(localFile));

                        }

                    }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ShowCategorySignActivity.this,
                            "Download Failed:" + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(ShowCategorySignActivity.this,
                    "Failed to create temp  file;" + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}