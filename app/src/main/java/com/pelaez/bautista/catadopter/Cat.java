package com.pelaez.bautista.catadopter;

import android.graphics.Bitmap;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by kurtv on 7/21/2016.
 */
public class Cat {

    private String name;
    private String uploaderID;
    private String uploader;
    private String sex;
    private boolean neutered;
    private String lastUpdated;
    private String key;
    private Bitmap bitmap;


    public Cat() {

    }

    public Cat(String n, String u, String s, boolean ne, String l, String k) {
        name = n;
        uploaderID = u;
        sex = s;
        neutered = ne;
        lastUpdated = l;
        key = k;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(u).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                uploader = u.getName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploaderID() {
        return uploaderID;
    }

    public void setUploaderID(String uploaderID) {
        this.uploaderID = uploaderID;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean getNeutered() {
        return neutered;
    }

    public void setNeutered(boolean neutered) {
        this.neutered = neutered;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
