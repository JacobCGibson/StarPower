package com.google.ar.core.examples.java.helloar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OwnedMenuActivity extends Activity implements View.OnClickListener{

    Button menu, shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_menu);

        menu = (Button) findViewById(R.id.buttonMenu);
        shop = (Button) findViewById(R.id.buttonShop);

        menu.setOnClickListener(this);
        shop.setOnClickListener(this);

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
        }
    }
}
