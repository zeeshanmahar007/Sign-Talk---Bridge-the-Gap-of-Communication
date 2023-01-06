package com.example.signtalk.CommunicationMode;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.signtalk.ModeSelectionAndNavDrawer.ModeSelectionMainActivity;
import com.example.signtalk.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TextToSignMode extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 10000;
    ImageButton VoiceButton,TextSendButton,BackButton;
    TextView ShowText;
    EditText inputmessage;
    private StorageReference videoRef;
    String inputfromuser;
    PyObject obj;
    String[] res;
    int check=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_sign_mode);

        VoiceButton = findViewById(R.id.voice_btn);
        //TextSendButton = findViewById(R.id.btnsend);
        ShowText = findViewById(R.id.inputtext);
        inputmessage = findViewById(R.id.input_message);
        BackButton = findViewById(R.id.btnbacktexttosign);

        // "context" must be an Activity, Service or Application object from your app.
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(TextToSignMode.this));
        } // this will start Python


        // now create Python instances
        final Python Py = Python.getInstance();
        // now create Python object
        //PyObject Pyobj = Py.getModule("preprocessfile"); // give Python scriPt name


        // now set return value into textview



        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_DENIED)
        {
            int RECORD_AUDIO_Permission_Code = 100;
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_Permission_Code);
        }


        VoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak() ;
            }
        });

//        TextSendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                inputfromuser = inputmessage.getText().toString();
//                if(!inputfromuser.isEmpty())
//                {
//                    //before
//                    // now call this function
//                    /*PyObject Pyobj = Py.getModule("preprocessfile");
//                    obj = Pyobj.callAttr("preprocess",inputfromuser);
//                    ShowText.setText(obj.toString());
//                    int i =0;
//                    while (i!=3)
//                    {
//                        Toast.makeText(TextToSignMode.this, ""+i, Toast.LENGTH_SHORT).show();
//                        i++;
//                    }*/
//
//
//                    //after
//                    List<PyObject> Pyobj = Py.getModule("preprocessfile").callAttr("preprocess",inputfromuser).asList();
//                    res = Pyobj.get(0).toJava(String[].class);
//                    ShowText.setText(res[0]);
//
//
//                    int i = 0;
//                    try {
//                        while (res[i]!=null)
//                        {
//                            Toast.makeText(TextToSignMode.this, ""+res[i], Toast.LENGTH_SHORT).show();
//                            ShowText.setText(res[i]);
//                            showsign(res[i]);
//                            //Thread.sleep(2000);
//                            i++;
//                        }
//
//                    }catch (Exception e){
//                        Toast.makeText(TextToSignMode.this,
//                            "Failed!!!;" + e.getLocalizedMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//
//                    //getvideoid();
//                }
//                else
//                {
//                    inputmessage.setError("Enter Some Text!");
//                    inputmessage.requestFocus();
//                    return;
//                }
//            }
//        });

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TextToSignMode.this, ModeSelectionMainActivity.class));
                finish();
            }
        });

    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        final Python Py = Python.getInstance();
        TextSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputfromuser = inputmessage.getText().toString();
                if(!inputfromuser.isEmpty())
                {
                    //before
                    // now call this function
                    /*PyObject Pyobj = Py.getModule("preprocessfile");
                    obj = Pyobj.callAttr("preprocess",inputfromuser);
                    ShowText.setText(obj.toString());
                    int i =0;
                    while (i!=3)
                    {
                        Toast.makeText(TextToSignMode.this, ""+i, Toast.LENGTH_SHORT).show();
                        i++;
                    } //


                    //after
                    List<PyObject> Pyobj = Py.getModule("preprocessfile").callAttr("preprocess",inputfromuser).asList();
                    res = Pyobj.get(0).toJava(String[].class);
                    ShowText.setText(res[0]);
                    showsign(res);

//                    int i = 0;
//                    try {
//                        while (res[i]!=null)
//                        {
//                            Toast.makeText(TextToSignMode.this, ""+res[i], Toast.LENGTH_SHORT).show();
//                            ShowText.setText(res[i]);
//                            showsign(res[i]);
//                            //Thread.sleep(2000);
//                            i++;
//                        }
//
//                    }catch (Exception e){
//                        Toast.makeText(TextToSignMode.this,
//                                "Failed!!!;" + e.getLocalizedMessage(),
//                                Toast.LENGTH_LONG).show();
//                    }

                    //getvideoid();
                }
                else
                {
                    inputmessage.setError("Enter Some Text!");
                    inputmessage.requestFocus();
                    return;
                }
            }
        });
    }
    */

    private void getvideoid()
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Signs");
        Query query = mDatabase.orderByChild("gloss").equalTo(inputfromuser);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null )
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        String Value = snapshot.getKey();
                        DatabaseReference Database = FirebaseDatabase.getInstance().getReference("Signs/"+Value+"/instances/0/video_id");
                        Database.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Toast.makeText(TextToSignMode.this, ""+snapshot.getValue(), Toast.LENGTH_LONG).show();
                                //showsign(snapshot.getValue().toString());
                                Log.d("TAG"," "+ Value);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Toast.makeText(getApplicationContext(), "Failed to read value." + databaseError.toException(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkpresentword(String Str)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Signs");
        Query query = mDatabase.orderByChild("Name").equalTo(Str);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null )
                {
                    //for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    //{
                        //String Value = snapshot.getKey();
                        Toast.makeText(getApplicationContext(), "Data Present " , Toast.LENGTH_LONG).show();
                        check = 1;
                   // }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                    check = 0;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Toast.makeText(getApplicationContext(), "Failed to read value." + databaseError.toException(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkpass(String str)
    {
        final VideoView videoView=
                (VideoView) findViewById(R.id.texttosignvideoview);
        videoView.setVideoPath("https://signtalk.blob.core.windows.net/sign/"+str+".mp4");
        videoView.start();

        videoView.setOnCompletionListener(new   MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                        try {
                                mp.reset();
                                videoView.setVideoPath("https://signtalk.blob.core.windows.net/sign/"+str+".mp4");
                                videoView.start();
                        }catch (Exception e)
                        {

                        }
                    }
        });
    }

    private void checkfailed(String str)
    {

    }

    private void showsign(String[] id)
    {

        /*
        StorageReference storageRef =
                FirebaseStorage.getInstance().getReference();
        String[] str = new String[res.length];
        final int[] finalIndex = {0};

        try {
            while (res[finalIndex[0]]!=null)
            {
                Toast.makeText(TextToSignMode.this, ""+res[finalIndex[0]], Toast.LENGTH_SHORT).show();
                //Log.d("TAG",res[i]);
                //ShowText.setText(res[i]);
                //Thread.sleep(2000);


        videoRef = storageRef.child("/VideoSigns/"+id[finalIndex[0]]+".mp4");
        try{
            if(id[finalIndex[0]].length()<3)
            {
                id[finalIndex[0]] = id[finalIndex[0]]+id[finalIndex[0]]+id[finalIndex[0]];
            }

            final File localFile = File.createTempFile(id[finalIndex[0]], "mp4");
            str[finalIndex[0]] = String.valueOf(localFile);

            videoRef.getFile(localFile).addOnSuccessListener(
                    new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess
                                (FileDownloadTask.TaskSnapshot taskSnapshot) {

//                            Toast.makeText(TextToSignMode.this, ""+localFile,
//                                    Toast.LENGTH_SHORT).show();


                            final VideoView videoView=
                                    (VideoView) findViewById(R.id.texttosignvideoview);
                            videoView.setVideoPath("https://signtalk.blob.core.windows.net/sign/"+res[finalIndex[0]]+".mp4");
                            videoView.start();
                            Log.d("TAG", String.valueOf(localFile));
                            // for video

//                            final VideoView videoView=
//                                    (VideoView) findViewById(R.id.texttosignvideoview);
//                            videoView.setVideoURI(Uri.fromFile(localFile));
//                            videoView.start();

                        }

                    }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TextToSignMode.this,
                            "Download Failed:" + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(TextToSignMode.this,
                    "Failed to create temp file;" + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
                finalIndex[0]++;
            }

        }catch (Exception e){

//            Toast.makeText(TextToSignMode.this,
//                    "Failed!!!;" + e.getLocalizedMessage(),
//                    Toast.LENGTH_LONG).show();
        }
*/

        try {
            final VideoView videoView=
                    (VideoView) findViewById(R.id.texttosignvideoview);
            videoView.setVideoPath("https://signtalk.blob.core.windows.net/sign/"+res[0]+".mp4");
            videoView.start();

            final int[] finalIndex = {0};
            finalIndex[0]++;
            videoView.setOnCompletionListener(new   MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    try {
                        while (res[finalIndex[0]]!=null)
                        {
                            try {
                                mp.reset();
                                videoView.setVideoPath("https://signtalk.blob.core.windows.net/sign/"+res[finalIndex[0]]+".mp4");
                                videoView.start();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                Toast.makeText(TextToSignMode.this, ""+res[finalIndex[0]].charAt(1), Toast.LENGTH_SHORT).show();

                                mp.reset();
                                videoView.setVideoPath("https://signtalk.blob.core.windows.net/sign/"+res[finalIndex[0]].charAt(1)+".mp4");
                                videoView.start();
                            }

                            finalIndex[0]++;
                        }
                   }
                    catch(Exception e){e.printStackTrace();}
                }
            });

        } catch (Exception e)
        {
            Toast.makeText(TextToSignMode.this,
                    "Failed!!!;" + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void speak() {
        //intent to show audio to text
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak Something....");
        //start intent
        try{
            startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e)
        { //show error or exception
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //recieve voice input

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:
            {
                if(resultCode==RESULT_OK&&data!=null)
                {     //getTextArrayfrom intent
                    ArrayList<String> myList=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    //setToTextView
                    inputfromuser = myList.get(0).toLowerCase();
                    Toast.makeText(this, " "+inputfromuser, Toast.LENGTH_SHORT).show();
                    String[] replace = inputfromuser.split("\\s");

                    final Python Py = Python.getInstance();
                    Toast.makeText(this, "Starting nltk file with "+inputfromuser, Toast.LENGTH_SHORT).show();
                    List<PyObject> Pyobj = Py.getModule("preprocessfile").callAttr("preprocess", inputfromuser).asList();
                    res = Pyobj.get(0).toJava(String[].class);

                    Toast.makeText(this, "back with "+ Arrays.toString(res), Toast.LENGTH_SHORT).show();

                    showsign(res);

                }
                break;
            }
            default:
                break;
        }
    }


    public void PlayVideo(View view) {
        final Python Py = Python.getInstance();
        inputfromuser = inputmessage.getText().toString();
        if(!inputfromuser.isEmpty())
        {
            ProgressDialog statusDialog = new ProgressDialog(TextToSignMode.this);
            statusDialog.setMessage("Preprocessing...");
            statusDialog.setIndeterminate(false);
            statusDialog.setCancelable(false);
            statusDialog.show();

            //after
            List<PyObject> Pyobj = Py.getModule("preprocessfile").callAttr("preprocess",inputfromuser).asList();
            res = Pyobj.get(0).toJava(String[].class);

            statusDialog.hide();
            int i=0;
            try {
                while (res[i]!=null)
                {
                    checkpresentword(res[i]);
                  //  SystemClock.sleep(20000);

                //    Toast.makeText(TextToSignMode.this, "while", Toast.LENGTH_SHORT).show();

                    if(check == 1)
                    {
                    //    Toast.makeText(TextToSignMode.this, "checkpass start", Toast.LENGTH_SHORT).show();
                        checkpass(res[i]);
                    //    Toast.makeText(TextToSignMode.this, "checkpass end", Toast.LENGTH_SHORT).show();
                    }
                    else if (check == 0)
                    {
                        checkfailed(res[i]);
                    }

                 //   Toast.makeText(TextToSignMode.this, "While End "+i + check, Toast.LENGTH_SHORT).show();

                    i++;
                }
            }catch (Exception e)
            {

            }

            checkpresentword(res[0]);
            showsign(res);
        }
        else
        {
            inputmessage.setError("Enter Some Text!");
            inputmessage.requestFocus();
            return;
        }
    }
}