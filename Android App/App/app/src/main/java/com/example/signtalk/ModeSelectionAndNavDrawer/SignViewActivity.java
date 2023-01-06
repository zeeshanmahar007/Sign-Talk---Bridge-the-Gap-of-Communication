package com.example.signtalk.ModeSelectionAndNavDrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.signtalk.CommunicationMode.TextToSignMode;
import com.example.signtalk.R;
import com.google.firebase.storage.FileDownloadTask;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;

public class SignViewActivity extends AppCompatActivity {

    private StorageReference videoRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_view);

        /*
        StorageReference storageRef =
                FirebaseStorage.getInstance().getReference();
        videoRef = storageRef.child("/Signs/69241.mp4");
        try{
            final File localFile = File.createTempFile("1234", "mp4");

            videoRef.getFile(localFile).addOnSuccessListener(
                    new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess
                                (FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(SignViewActivity.this, "Playing....",
                                    Toast.LENGTH_LONG).show();

                            // for video
                            final VideoView videoView=
                                    (VideoView) findViewById(R.id.signvideoview);
                            videoView.setVideoURI(Uri.fromFile(localFile));
                            videoView.start();

                            // for gif
                            /*ImageView imageView = findViewById(R.id.signimageview);
                            Glide.with(SignViewActivity.this)
                                    .load(Uri.fromFile(localFile)).into(imageView);//
                        }

                    }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignViewActivity.this,
                            "Download Failed:" + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(SignViewActivity.this,
                    "Failed to create temp  file;" + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }*/


        try {
            final VideoView videoView=
                    (VideoView) findViewById(R.id.signvideoview);
            videoView.setVideoPath("https://signtalk.blob.core.windows.net/sign/book.mp4"); //"+res[0]+"
            videoView.start();

            videoView.setOnCompletionListener(new   MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    try {
                        while (true)
                        {
                            try {
                                mp.reset();
                                videoView.setVideoPath("https://signtalk.blob.core.windows.net/sign/book.mp4"); //"+res[finalIndex[0]]+"
                                videoView.start();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                //Toast.makeText(TextToSignMode.this, ""+res[finalIndex[0]].charAt(1), Toast.LENGTH_SHORT).show();

                                mp.reset();
                                videoView.setVideoPath("https://signtalk.blob.core.windows.net/sign/book.mp4"); //"+res[finalIndex[0]].charAt(1)+"
                                videoView.start();
                            }

                        }
                    }
                    catch(Exception e){e.printStackTrace();}
                }
            });

        } catch (Exception e)
        {
            Toast.makeText(SignViewActivity.this,
                    "Failed!!!;" + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

}