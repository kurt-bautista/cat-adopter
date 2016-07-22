package com.pelaez.bautista.catadopter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewCatActivity extends AppCompatActivity {

    private EditText name;
    private CheckBox neutered;
    private RadioGroup rg;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private StorageReference storageRef;
    private StorageReference catPictures;

    private Bitmap bitmap;

    private Cat c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cat);

        name = (EditText)findViewById(R.id.catNameTextBox);
        neutered = (CheckBox)findViewById(R.id.neuteredCheckBox);
        rg = (RadioGroup)findViewById(R.id.radioGroup2);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }
        };

        FirebaseStorage storage= FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://catadopter-9e09d.appspot.com");

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
    }

    public void saveReview(View v) {
        String sex = ((RadioButton)rg.findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference cats = db.getReference("cats");
        String key = cats.push().getKey();
        Cat c = new Cat(name.getText().toString(), user.getUid(), sex, neutered.isChecked(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()), key);
        cats.child(key).setValue(c);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50, baos);
        byte[] datas = baos.toByteArray();

        catPictures= storageRef.child(key);

        UploadTask uploadTask = catPictures.putBytes(datas);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });

        finish();
    }

    public void newImage(View v){
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmap = ImageUtils.resizeBitmap((new File(uri.getPath())).getAbsolutePath(), 150);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.imageButton);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
