package com.pelaez.bautista.catadopter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        email = (EditText)findViewById(R.id.emailTextBox);
        password = (EditText)findViewById(R.id.passwordTextBox);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    //signed in
                    Intent i = new Intent(LoginActivity.this, com.pelaez.bautista.catadopter.MainActivity.class);
                    startActivity(i);
                    finish();
                    //Toast.makeText(LoginActivity.this, "login", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //signed out
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            //mAuth.signOut();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }

    public void login(View v) {

        if(!validateForm()) return;

        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void register(View v) {
        Intent i = new Intent(this, com.pelaez.bautista.catadopter.RegisterActivity.class);
        startActivity(i);
    }

    private boolean validateForm() {
        boolean valid = true;

        String emailAddress = email.getText().toString();
        if (TextUtils.isEmpty(emailAddress)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String password = this.password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            this.password.setError("Required.");
            valid = false;
        } else {
            this.password.setError(null);
        }

        return valid;
    }
}
