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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {

    private FirebaseUser user;
    private EditText newName;
    private EditText newContact;
    private EditText newPass;
    private EditText repeatNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        newName = (EditText)findViewById(R.id.newNameBox);
        newContact = (EditText)findViewById(R.id.newContactBox);
        newPass = (EditText)findViewById(R.id.newPassBox);
        repeatNew = (EditText)findViewById(R.id.repeatNewBox);

        user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                newName.setText(u.getName());
                newContact.setText(u.getContactNumber());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void confirm(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveAccount();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                .setTitle("Confirm");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveAccount() {

        if(!validateForm()) return;

        user.updatePassword(newPass.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) Toast.makeText(AccountActivity.this, "Password updated", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(AccountActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }

    private boolean validateForm() {
        boolean valid = true;

        String name = newName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            newName.setError("Required.");
            valid = false;
        } else {
            newName.setError(null);
        }

        String number = newContact.getText().toString();
        if (number.length() != 11 && number.length() != 7) {
            newContact.setError("Please enter a valid phone number.");
            valid = false;
        } else {
            newContact.setError(null);
        }

        String password2 = newPass.getText().toString();
        if (TextUtils.isEmpty(password2)) {
            newPass.setError("Required.");
            valid = false;
        } else {
            newPass.setError(null);
        }

        String password3 = repeatNew.getText().toString();
        if (!password3.equals(password2)) {
            repeatNew.setError("Passwords do not match.");
            valid = false;
        } else {
            repeatNew.setError(null);
        }

        return valid;
    }

}
