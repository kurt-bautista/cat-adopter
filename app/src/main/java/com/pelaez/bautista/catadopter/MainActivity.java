package com.pelaez.bautista.catadopter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView navMenu;
    private ArrayAdapter<String> navAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navMenu = (ListView)findViewById(R.id.listView);
        navAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] {"one", "two", "three"});
        navMenu.setAdapter(navAdapter);
    }
}
