package com.google.ar.core.examples.java.helloar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends Activity implements View.OnClickListener{

    Button settings, play, customize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        settings = (Button) findViewById(R.id.settingsButton);
        play = (Button) findViewById(R.id.playButton);
        customize = (Button) findViewById(R.id.customizeButton);

        settings.setOnClickListener(this);
        play.setOnClickListener(this);
        customize.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingsButton:
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsMenuActivity.class);
                settingsIntent.putExtra("volume", getIntent().getIntExtra("volume", 50));
                startActivity(settingsIntent);
                break;
            case R.id.playButton:
                Intent mainIntent = new Intent(getApplicationContext(), HelloArActivity.class);
                mainIntent.putExtra("volume", getIntent().getIntExtra("volume", 50));
                startActivity(mainIntent);
                break;
            case R.id.customizeButton:
                Intent customIntent = new Intent(getApplicationContext(), OwnedMenuActivity.class);
                startActivity(customIntent);
                break;
        }
    }
}
