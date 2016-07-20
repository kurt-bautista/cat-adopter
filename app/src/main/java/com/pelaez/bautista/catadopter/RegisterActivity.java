package com.pelaez.bautista.catadopter;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText contact;
    private EditText password;
    private EditText repeat;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Create a new account");

        name = (EditText)findViewById(R.id.newNameTextBox);
        email = (EditText)findViewById(R.id.newEmailTextBox);
        contact = (EditText)findViewById(R.id.newContactTextBox);
        password = (EditText)findViewById(R.id.newPasswordTextBox);
        repeat = (EditText)findViewById(R.id.repeatPasswordTextBox);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Toast.makeText(RegisterActivity.this, "register", Toast.LENGTH_SHORT).show(); //start MainActivity
                    User u = new User(user.getUid(), user.getEmail(), name.getText().toString(), contact.getText().toString());
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference users = db.getReference("users");
                    users.child(user.getUid()).setValue(u);
                    //users.child(user.getUid()).child()
                }
                else; //signed out
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
        if(mAuthListener != null) mAuth.removeAuthStateListener(mAuthListener);
        //mAuth.signOut();
    }

    public void confirm(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createAccount();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setTitle("Confirm");
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createAccount() {

        if(!validateForm()) return;

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) Toast.makeText(RegisterActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                        else {
                            /*FirebaseDatabase db = FirebaseDatabase.getInstance();
                            DatabaseReference users = db.getReference("users");
                            users.push();*/
                            finish();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = this.name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            this.name.setError("Required.");
            valid = false;
        } else {
            this.name.setError(null);
        }

        String emailAddress = email.getText().toString();
        if (TextUtils.isEmpty(emailAddress)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String number = contact.getText().toString();
        if (number.length() != 11 && number.length() != 7) {
            contact.setError("Please enter a valid phone number.");
            valid = false;
        } else {
            contact.setError(null);
        }

        String password = this.password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            this.password.setError("Required.");
            valid = false;
        } else {
            this.password.setError(null);
        }

        String password2 = repeat.getText().toString();
        if (!password.equals(password2)) {
            repeat.setError("Passwords do not match.");
            valid = false;
        } else {
            repeat.setError(null);
        }

        return valid;
    }
}
