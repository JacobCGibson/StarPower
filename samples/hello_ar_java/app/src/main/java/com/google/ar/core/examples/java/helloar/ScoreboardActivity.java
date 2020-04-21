package com.google.ar.core.examples.java.helloar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ScoreboardActivity extends Activity implements View.OnClickListener{

    Button menu;
    View highScoresTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        menu = (Button) findViewById(R.id.mainMenuButton);
        //highScoresTable = (View) findViewById(R.id.highScoresRecycler);

        menu.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainMenuButton:
                Intent menuIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(menuIntent);
                break;
        }
    }
}
