package com.google.ar.core.examples.java.helloar;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class SettingsMenuActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    Button menu, restore;
    SeekBar volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        menu = (Button) findViewById(R.id.buttonMenu);
        restore = (Button) findViewById(R.id.button);
        volume = (SeekBar) findViewById(R.id.seekVolume);
        volume.setProgress(getIntent().getIntExtra("volume", MAX_VOLUME/2));
        volume.setMax(MAX_VOLUME);

        menu.setOnClickListener(this);
        restore.setOnClickListener(this);
        volume.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonMenu:
                Intent mainIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                mainIntent.putExtra("volume", volume.getProgress());
                startActivity(mainIntent);
                break;
            case R.id.button:
                //restore purchases code here
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
