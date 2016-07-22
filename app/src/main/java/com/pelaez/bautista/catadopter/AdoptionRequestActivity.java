package com.pelaez.bautista.catadopter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdoptionRequestActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption_request);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseAuth firebase= FirebaseAuth.getInstance();
        FirebaseUser user=firebase.getCurrentUser();

        ListView lv=(ListView)findViewById(R.id.listView2);
        AdoptionAdapter a=new AdoptionAdapter(this,com.pelaez.bautista.catadopter.Request.class,R.layout.activity_adoption_request,mDatabase.child("requests").equalTo(user.getUid()));

        lv.setAdapter(a);
    }




}
