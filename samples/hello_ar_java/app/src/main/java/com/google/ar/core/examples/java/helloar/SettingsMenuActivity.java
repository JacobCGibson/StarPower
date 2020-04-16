package com.google.ar.core.examples.java.helloar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsMenuActivity extends Activity implements View.OnClickListener{

    Button menu, restore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        menu = (Button) findViewById(R.id.buttonMenu);
        restore = (Button) findViewById(R.id.button);

        menu.setOnClickListener(this);
        restore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonMenu:
                Intent mainIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.button:
                //restore purchases code here
                break;
        }
    }
}
