package com.pelaez.bautista.catadopter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by kurtv on 7/21/2016.
 */
public class CatAdapter extends BaseAdapter {

    protected Activity mActivity;
    protected int mLayout;
    FirebaseArray mSnapshots;
    private final Class<Cat> mModelClass;

    StorageReference storageRef;
    StorageReference catPictures;

    public CatAdapter(Activity activity, Class modelClass, int modelLayout, Query ref) {
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

        FirebaseStorage storage= FirebaseStorage.getInstance();
        storageRef= storage.getReferenceFromUrl("gs://catadopter-9e09d.appspot.com");

    }

    public CatAdapter(Activity activity, Class<Cat> modelClass, int modelLayout, DatabaseReference ref) {
        this(activity, modelClass, modelLayout, (Query)ref);
    }

    public void cleanup() {
        mSnapshots.cleanup();
    }

    @Override
    public int getCount() {
        return mSnapshots.getCount();
    }

    @Override
    public Cat getItem(int i) {
        return parseSnapshot(mSnapshots.getItem(i));
    }

    protected Cat parseSnapshot(DataSnapshot snapshot) {
        Cat a = snapshot.getValue(mModelClass);
        return a;
    }

    public DatabaseReference getRef(int position) { return mSnapshots.getItem(position).getRef(); }

    @Override
    public long getItemId(int i) {
        return mSnapshots.getItem(i).getKey().hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) view = mActivity.getLayoutInflater().inflate(mLayout, viewGroup, false);
        System.out.println(getItem(i));
        final Cat model = (Cat)getItem(i);
        String key = model.getKey();

        final View v = mActivity.getLayoutInflater().inflate(R.layout.cat_info, null);
        catPictures=storageRef.child(key);
        catPictures.getBytes(5196*5196).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageView image = (ImageView) v.findViewById(R.id.catThumbnail);
                model.setBitmap(bmp);
                image.setImageBitmap(bmp);

            }
        });



        v.setTag(model);

        return v;
    }

}
