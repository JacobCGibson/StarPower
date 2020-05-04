package com.google.ar.core.examples.java.StarPower;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class SettingsMenuActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    Button menu;
    SeekBar volume;
    EditText timer;
    private final int maxVolume = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        menu = (Button) findViewById(R.id.buttonMenu);
        volume = (SeekBar) findViewById(R.id.seekVolume);
        volume.setProgress(getIntent().getIntExtra("volume", maxVolume/2));
        volume.setMax(maxVolume);
        timer = (EditText) findViewById(R.id.timerDuration);

        menu.setOnClickListener(this);
        volume.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonMenu:
                Intent mainIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                mainIntent.putExtra("volume", volume.getProgress());
                if (timer.getText().toString().equals("")) mainIntent.putExtra("time", 50);
                else mainIntent.putExtra("time", Integer.parseInt(timer.getText().toString()));
                startActivity(mainIntent);
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
