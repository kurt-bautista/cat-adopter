package com.pelaez.bautista.catadopter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by AUD_SITE on 7/21/2016.
 */
public class AdoptionAdapter extends BaseAdapter {



    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsers;
    private DatabaseReference mCats;
// ...

    protected Activity mActivity;
    protected int mLayout;
    FirebaseArray mSnapshots;
    private final Class<Request> mModelClass;

    // Create a storage reference from our app
    StorageReference storageRef;
    StorageReference catPictures;


    public AdoptionAdapter(Activity activity, Class modelClass, int modelLayout, Query ref) {
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference("users");
        mCats = mDatabase.getReference("cats");

        mModelClass = modelClass;
        mLayout = modelLayout;
        mActivity = activity;
        mSnapshots = new FirebaseArray(ref);
        mSnapshots.setOnChangedListener(new FirebaseArray.OnChangedListener() {
            @Override
            public void onChanged(EventType type, int index, int oldIndex) {
                notifyDataSetChanged();
            }
        });

        //storage
        FirebaseStorage storage= FirebaseStorage.getInstance();
        storageRef= storage.getReferenceFromUrl("gs://catadopter-9e09d.appspot.com");
    }

    public AdoptionAdapter(Activity activity, Class<Request> modelClass, int modelLayout, DatabaseReference ref) {
        this(activity, modelClass, modelLayout, (Query)ref);
    }

    @Override
    public int getCount() {
        return mSnapshots.getCount();
    }

    @Override
    public Request getItem(int i) {
        return parseSnapshot(mSnapshots.getItem(i));
    }

    protected Request parseSnapshot(DataSnapshot snapshot) {
        Request a = snapshot.getValue(mModelClass);
        return a;
    }

    public DatabaseReference getRef(int position) { return mSnapshots.getItem(position).getRef(); }

    @Override
    public long getItemId(int i) {
        return mSnapshots.getItem(i).getKey().hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final View v =mActivity.getLayoutInflater().inflate(R.layout.cat_user_adoption_request,null);

        Request request=getItem(i);

        final TextView requesterTextView=(TextView)v.findViewById(R.id.username);
        final TextView requesterCatView=(TextView)v.findViewById(R.id.catname);
        final TextView contactInfo=(TextView)v.findViewById(R.id.contact_number);
        final ImageView catImage = (ImageView)v.findViewById(R.id.catImage);

        String requester=request.getRequester();




        mUsers.child(requester).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                requesterTextView.setText(u.getName());
                contactInfo.setText(u.getContactNumber());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final String cat=request.getCat();

        mCats.child(cat).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cat c = dataSnapshot.getValue(Cat.class);
                requesterCatView.setText(c.getName());

                catPictures=storageRef.child(cat);
                catPictures.getBytes(5196*5196).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        catImage.setImageBitmap(bmp);

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return v;




    }
}
