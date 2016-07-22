package com.pelaez.bautista.catadopter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by kurtv on 7/21/2016.
 */
public class CatDialog extends Dialog {

    private Cat cat;
    private TextView catName;
    private TextView uploader;
    private TextView updated;
    private TextView gender;
    private ImageView cat_dialog_image;
    private User u;
    private FirebaseAuth firebase;
    private FirebaseUser user;
    private DatabaseReference mRequest;
    private DatabaseReference mDatabase;

    public CatDialog(Context c, Cat cat) {
        super(c);
        this.cat = cat;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.cat_more_info);

        firebase = FirebaseAuth.getInstance();
        user = firebase.getCurrentUser();
        mRequest = FirebaseDatabase.getInstance().getReference("requests");

        catName = (TextView)findViewById(R.id.name);
        uploader = (TextView)findViewById(R.id.uploader);
        updated = (TextView)findViewById(R.id.date);
        gender = (TextView)findViewById(R.id.gender);
        cat_dialog_image = (ImageView)findViewById(R.id.dialog_pic);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        /*mDatabase.child(cat.getUploaderID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                uploader.setText(u.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        uploader.setText(cat.getUploader());
        catName.setText(cat.getName());
        updated.setText(cat.getLastUpdated());
        gender.setText("(" + String.valueOf(cat.getSex().charAt(0)) + ")");

        cat_dialog_image.setImageBitmap(cat.getBitmap());

        Button b = (Button)findViewById(R.id.adoptButton);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference requests = db.getReference("requests");
                //create request object
                Request r= new Request(cat.getUploaderID(),user.getUid(),cat.getKey());
                String key =mRequest.push().getKey();
                mRequest.child(key).setValue(r);



            }
        });
    }

}
