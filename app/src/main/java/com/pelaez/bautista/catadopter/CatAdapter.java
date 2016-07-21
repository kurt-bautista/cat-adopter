package com.pelaez.bautista.catadopter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by kurtv on 7/21/2016.
 */
public class CatAdapter extends BaseAdapter {

    protected Activity mActivity;
    protected int mLayout;
    FirebaseArray mSnapshots;
    private final Class<Cat> mModelClass;

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
        Cat model = (Cat)getItem(i);
        view = mActivity.getLayoutInflater().inflate(R.layout.cat_info, null);
        view.setTag(model);
//        populateView(view, model, i);
        return view;
    }

//    protected void populateView(View v, Cat model, int i) {
//        v = mActivity.getLayoutInflater().inflate(R.layout.cat_info, null);
//        v.setTag(model);
//    }
}
