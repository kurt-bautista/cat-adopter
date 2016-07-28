package com.pelaez.bautista.catadopter;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import layout.CatsGridFragment;

public class MainActivity extends AppCompatActivity implements CatsGridFragment.OnGridClickListener {

    private String[] screens;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        mTitle = mDrawerTitle = getTitle();

        screens = getResources().getStringArray(R.array.screens_array);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.mainDrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerList = (ListView)findViewById(R.id.mainDrawer);
        if(savedInstanceState == null) selectItem(0);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, R.id.screenLabel, screens));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null);
                else
                {
                    Intent i = new Intent(MainActivity.this, com.pelaez.bautista.catadopter.LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment catGrid = CatsGridFragment.newInstance();
        fragmentManager.beginTransaction()
                .add(R.id.mainContainer, catGrid)
                .commit();
        mDrawerList.setItemChecked(0, true);
        getSupportActionBar().setTitle(screens[0]);
        mDrawerLayout.closeDrawer(mDrawerList);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.search:
                Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;
        }
    }

    //region idk what this does

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return /*super.onCreateOptionsMenu(menu)*/true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onGridClick(Cat cat) {
        CatDialog cd = new CatDialog(MainActivity.this, cat);
        cd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cd.show();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //endregion

    private void selectItem(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position)
        {
            case 0:
                Fragment catGrid = CatsGridFragment.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, catGrid)
                        .commit();
                mDrawerList.setItemChecked(0, true);
                setTitle(screens[0]);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                mAuth.signOut();
                break;
        }
    }
}
