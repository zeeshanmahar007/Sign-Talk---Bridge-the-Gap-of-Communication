package com.example.signtalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.signtalk.ModeSelectionAndNavDrawer.ModeSelectionMainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private Button Login;
    private EditText editemail,editpassword;
    FloatingActionButton fb,google;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN=123;
    public FirebaseAuth mAuth;
    private ProgressDialog statusDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //binding sign in with google and facebook
        fb = findViewById(R.id.btn_login_fb);
        google = findViewById(R.id.btn_login_google);

        editemail = findViewById(R.id.edt_email);
        editpassword = findViewById(R.id.edt_password);
        Login = findViewById(R.id.btn_login);
        //btn.setOnClickListener((View.OnClickListener) this);
        mAuth = FirebaseAuth.getInstance();


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = editemail.getText().toString().trim();
                String Password = editpassword.getText().toString().trim();

                if (Email.isEmpty())
                {
                    editemail.setError("Email is Required!");
                    editemail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    editemail.setError("Please enter valid email!");
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
                    editpassword.setError("Min password length is 6 character!");
                    editpassword.requestFocus();
                    return;
                }

                statusDialog = new ProgressDialog(com.example.signtalk.Login.this);
                statusDialog.setMessage("Getting ready...");
                statusDialog.setIndeterminate(false);
                statusDialog.setCancelable(false);
                statusDialog.show();

                editpassword.getText().clear();
                editemail.getText().clear();
                mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            if(mAuth.getCurrentUser().isEmailVerified())
                            {
                                startActivity(new Intent(Login.this,ModeSelectionMainActivity.class));
                                finish();
                            }
                            else
                            {
                                statusDialog.hide();
                                Toast.makeText(Login.this, "please verify your Email First!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            statusDialog.hide();
                            Toast.makeText(Login.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, FacebookAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Configure Google Sign In
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(Login.this, gso);
                signIn();
            }
        });

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //textView.setText(user.getDisplayName());
                            startActivity(new Intent(Login.this,ModeSelectionMainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null && currentUser.isEmailVerified())
        {
            startActivity(new Intent(Login.this, ModeSelectionMainActivity.class));
            finish();
            //Toast.makeText(this, "Already Loged In", Toast.LENGTH_SHORT).show();
            //textView.setText(currentUser.getDisplayName());
        }
    }

    public void backtomain(View view)
    {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    public void gotosinup(View view)
    {
        startActivity(new Intent(this,Signup.class));
        finish();
    }

    public void forgetpassword(View view) {
        EditText reset_Mail=new EditText(view.getContext());
        AlertDialog.Builder password_ResetDialog=new AlertDialog.Builder(view.getContext());
        password_ResetDialog.setTitle("Reset Password");
        password_ResetDialog.setMessage("Enter your email to Recieved Email reset link");
        password_ResetDialog.setView(reset_Mail);
        password_ResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //extract the email send resetlink
                String mail=reset_Mail.getText().toString();
                if(!mail.isEmpty())
                {
                mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Login.this, "Reset Link Sent to Your Email", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String Message = e.getMessage();
                        if(Message.contains("formatted"))
                        {
                            Toast.makeText(Login.this,"Not a Valid Email",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Login.this,"User Not Exist",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
                else
                {
                    Toast.makeText(Login.this,"please enter your email!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        password_ResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //close the dialog
            }
        });
        password_ResetDialog.create().show();
    }
}