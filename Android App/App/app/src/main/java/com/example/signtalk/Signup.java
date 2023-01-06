package com.example.signtalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button Btnsignup;
    private EditText editfullname,editemail,editpassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        Btnsignup = findViewById(R.id.btnsignup);

        editfullname = findViewById(R.id.edtfullname);
        editemail = findViewById(R.id.edtemailaddress);
        editpassword = findViewById(R.id.edtpassword);
        progressBar = findViewById(R.id.progressbar);

        Btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FullName = editfullname.getText().toString().trim();
                String Email = editemail.getText().toString().trim();
                String Password = editpassword.getText().toString().trim();

                if(FullName.isEmpty())
                {
                    editfullname.setError("Full Name is Required!");
                    editfullname.requestFocus();
                    return;
                }
                if(!FullName.matches("^[A-Za-z ]+$"))
                {
                    editfullname.setError("Full Name should contain only Alphabet");
                    editfullname.requestFocus();
                    return;
                }
                if(Email.isEmpty())
                {
                    editemail.setError("Email is Required!");
                    editemail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    editemail.setError("Please Provide Valid Email!");
                    editemail.requestFocus();
                    return;
                }
                if(Password.isEmpty())
                {
                    editpassword.setError("Password is Required!");
                    editpassword.requestFocus();
                    return;
                }
                if(Password.length() < 6)
                {
                    editpassword.setError("Min Password Length should be 6 characters!");
                    editpassword.requestFocus();
                    return;
                }

                editpassword.getText().clear();
                editemail.getText().clear();
                editfullname.getText().clear();
                progressBar.setVisibility(View.VISIBLE);


                mAuth.createUserWithEmailAndPassword(Email,Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    User user = new User(FullName,Email);

                                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(firebaseUser.getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {

                                                Toast.makeText(Signup.this, "User has been Registered Successfully!", Toast.LENGTH_LONG).show();
                                                //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                //user.sendEmailVerification();

                                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(Signup.this, "please check email for verification.", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Toast.makeText(Signup.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                                //startActivity(new Intent(Signup.this,GreetingAndModeActivity.class));
                                                // sign in and redirect to profile
                                            }
                                            else
                                            {
                                                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("TAG", "User deleted.");
                                                        }
                                                    }
                                                });
                                                Toast.makeText(Signup.this, "Failed to Register! Try Again!", Toast.LENGTH_LONG).show();
                                            }
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Signup.this, "User Already Registered with this Email!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });



            }
        });


    }

    public void backtomain(View view) {startActivity(new Intent(this,MainActivity.class));}
    public void gotologin(View view) {startActivity(new Intent(this,Login.class));}
}