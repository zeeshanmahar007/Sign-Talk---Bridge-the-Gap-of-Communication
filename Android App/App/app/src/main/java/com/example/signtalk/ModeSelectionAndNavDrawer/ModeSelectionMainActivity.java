package com.example.signtalk.ModeSelectionAndNavDrawer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DecorToolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.signtalk.CommunicationMode.SignToTextMode;
import com.example.signtalk.CommunicationMode.TextToSignMode;
import com.example.signtalk.Login;
import com.example.signtalk.MainActivity;
import com.example.signtalk.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ModeSelectionMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    //86CEB8 - Light Green
    //0E5E73 - Blue
    //F1F0F0 - White
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_selection_activity_main);


        //Log.d("TAG","In Mode Selection");
        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        ((TextView) nav_header.findViewById(R.id.name)).setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        ((TextView) nav_header.findViewById(R.id.email)).setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        Picasso
                .get()
                .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                .into((CircleImageView) nav_header.findViewById(R.id.account_icon));

        /*
        if((FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).isEmpty())
        {
            DatabaseReference Database = FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/fullName");
            Database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String str = dataSnapshot.getValue().toString();
                    ((TextView) nav_header.findViewById(R.id.name)).setText(str);
                    //return;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }

        if((FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())==null)
        {
            Picasso.get().load(R.drawable.img)
                    .into((CircleImageView) nav_header.findViewById(R.id.account_icon));
        }


        navigationView1.addHeaderView(nav_header);*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new TrySystemFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_try_system);
            setActionBar("Home");
        }
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_try_system:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TrySystemFragment()).commit();
                setActionBar("Home");
                break;
            case R.id.nav_objective:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ObjectiveFragment()).commit();
                setActionBar("Objective");
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ModeSelectionMainActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.quit:
                ExitAlert();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void VideoViewActivity(View view) {
        startActivity(new Intent(ModeSelectionMainActivity.this, SignViewActivity.class));
    }

    public void signtotext(View view){
        startActivity(new Intent(ModeSelectionMainActivity.this, SignToTextMode.class));
    }

    public void texttosign(View view){
        startActivity(new Intent(ModeSelectionMainActivity.this, TextToSignMode.class));
    }

    public void alphabets(View view){
        startActivity(new Intent(ModeSelectionMainActivity.this, AlphabetActivity.class));
    }

    public void numbers(View view){
        startActivity(new Intent(ModeSelectionMainActivity.this, NumberActivity.class));
    }

    public void setActionBar(String heading) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(heading);
        actionBar.show();

    }

    public void ExitAlert()
    {
        AlertDialog.Builder Exit_Dialog=new AlertDialog.Builder(ModeSelectionMainActivity.this);
        Exit_Dialog.setTitle("Exit SignTalk");
        Exit_Dialog.setMessage("Are you sure!");
        Exit_Dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(1);
            }
        });

        Exit_Dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        Exit_Dialog.create().show();
    }
}