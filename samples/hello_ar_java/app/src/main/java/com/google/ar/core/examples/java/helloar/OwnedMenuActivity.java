package com.google.ar.core.examples.java.helloar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class OwnedMenuActivity extends Activity implements View.OnClickListener{

    Button menu, shop;
    ListView customizations;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_menu);

        menu = (Button) findViewById(R.id.buttonMenu);
        shop = (Button) findViewById(R.id.buttonShop);
        customizations = (ListView) findViewById(R.id.listView);

        menu.setOnClickListener(this);
        shop.setOnClickListener(this);

        //create the array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.row_item, R.id.listView, list);

        //set the list to the adapter
        final ListView listView = (ListView) findViewById(android.R.id.list);
        customizations.setAdapter(adapter);

        TextView emptyView = (TextView) findViewById(android.R.id.empty);
        customizations.setEmptyView(emptyView);

        //need to set the On Item Click Listener
        customizations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) customizations.getItemAtPosition(position);
                SharedPreferences preferences = getSharedPreferences("StarPower",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("blaster", selectedItem);
                // editor.commit();
                editor.apply();
            }



        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonMenu:
                Intent menuIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(menuIntent);
                break;
            case R.id.buttonShop:
                Intent shopIntent = new Intent(getApplicationContext(), OwnedMenuActivity.class);
                startActivity(shopIntent);
                break;
            case R.id.listView:

        }
    }
}
