package com.google.ar.core.examples.java.helloar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class OwnedMenuActivity extends Activity implements View.OnClickListener{

    Button menu, shop;
    ListView customizations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_menu);

        menu = (Button) findViewById(R.id.buttonMenu);
        shop = (Button) findViewById(R.id.buttonShop);
        customizations = (ListView) findViewById(R.id.listView);

        menu.setOnClickListener(this);
        shop.setOnClickListener(this);
        customizations.setOnClickListener(this);

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
