package com.google.ar.core.examples.java.helloar;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends Activity implements View.OnClickListener{

    Button settings, play, customize;
    public MediaPlayer mp;

    private final static int MAX_VOLUME = 100;
    private int seekVol = 50;
    private float volume = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        settings = (Button) findViewById(R.id.settingsButton);
        play = (Button) findViewById(R.id.playButton);
        customize = (Button) findViewById(R.id.customizeButton);
        mp = MediaPlayer.create(this, R.raw.menu_theme);
        settings.setOnClickListener(this);
        play.setOnClickListener(this);
        customize.setOnClickListener(this);

        mp.start();
        seekVol = getIntent().getIntExtra("volume", 50);
        volume = (float) (1 - (Math.log(MAX_VOLUME - seekVol) / Math.log(MAX_VOLUME)));
        mp.setVolume(volume, volume);
    }

    @Override
    protected void onResume() {
        super.onResume();
        seekVol = getIntent().getIntExtra("volume", 50);
        volume = (float) (1 - (Math.log(MAX_VOLUME - seekVol) / Math.log(MAX_VOLUME)));
        mp.setVolume(volume, volume);
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
                Intent mainIntent = new Intent(getApplicationContext(), ShootingGalleryActivity.class);
                mainIntent.putExtra("volume", getIntent().getIntExtra("volume", 50));
                mp.stop();
                startActivity(mainIntent);
                break;
            case R.id.customizeButton:
                Intent customIntent = new Intent(getApplicationContext(), OwnedMenuActivity.class);
                startActivity(customIntent);
                break;
        }
    }
}
