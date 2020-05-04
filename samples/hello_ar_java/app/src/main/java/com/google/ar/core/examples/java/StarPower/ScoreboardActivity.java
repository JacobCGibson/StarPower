package com.google.ar.core.examples.java.StarPower;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreboardActivity extends Activity implements View.OnClickListener {
    //define activity's interactive elements
    EditText initials;
    TextView scoreText;
    TextView timeText;
    TextView targetsText;

    Button save;
    Button clear;
    Button mainMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scoreboard);

        //reference text fields
        initials = (EditText) findViewById(R.id.initials);
        scoreText = (TextView) findViewById(R.id.score);
        timeText = (TextView) findViewById(R.id.time);
        targetsText = (TextView) findViewById(R.id.targets);

        //reference buttons
        save = (Button) findViewById(R.id.save);
        mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        clear = (Button) findViewById(R.id.clear);

        ScoreboardDB dbhelper = new ScoreboardDB(getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        String scoreString = getIntent().getStringExtra("score");
        int time = getIntent().getIntExtra("time", 50);
        String targets = getIntent().getStringExtra("NUMTARGETS");

        scoreText.setText(scoreString + " points");
        timeText.setText(time + " seconds");
        targetsText.setText(targets + " targets");
        mainMenuButton.setOnClickListener(this);

        int scoreInt = Integer.parseInt(scoreString);

        runQuery(db);

        save.setOnClickListener(v -> {

            //ScoreboardDB helper created whenever user clicks on the save button
            ContentValues values = new ContentValues();

            //time
            values.put(ScoreboardAttr.ScoreboardEntry.COLUMN_TIME, time);

            //score
            values.put(ScoreboardAttr.ScoreboardEntry.COLUMN_SCORE, scoreInt);

            //initials
            values.put(ScoreboardAttr.ScoreboardEntry.COLUMN_INITIALS, initials.getText().toString());

            //targets
            values.put(ScoreboardAttr.ScoreboardEntry.COLUMN_TARGETS, targets);

            //insert the values into the database
            long newRowId = db.insert(
                    ScoreboardAttr.ScoreboardEntry.TABLE_NAME,  //table name for insert
                    null,  //null is all columns
                    values);  //values for the insert

            //set up toast for saved data
            int duration = Toast.LENGTH_SHORT;

            String result;

            //check if the values were inserted into the database and change result accordingly
            if (newRowId != -1) result = "New Entry Added!";

            else result = "ERROR";

            //display the toast with the result of the database insertion
            Toast toast = Toast.makeText(getApplicationContext(), result, duration);
            toast.show();

            //reset the input fields
            initials.setText("");

            runQuery(db);

            save.setEnabled(false);
            initials.setEnabled(false);
        });

        clear.setOnClickListener(v -> {
            clearScoreboard(db);
            runQuery(db);
        });
    }
    /*
        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            save.setEnabled(false);
            initials.setEnabled(false);
        }
    */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainMenuButton:
                Intent menuIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                menuIntent.putExtra("volume", getIntent().getIntExtra("volume", 50));
                menuIntent.putExtra("time", getIntent().getIntExtra("time", 50));
                startActivity(menuIntent);
                break;
        }
    }
    public void runQuery(SQLiteDatabase db){
        String[] projection = {
                ScoreboardAttr.ScoreboardEntry.COLUMN_INITIALS,
                ScoreboardAttr.ScoreboardEntry.COLUMN_SCORE,
                ScoreboardAttr.ScoreboardEntry.COLUMN_TIME,
                ScoreboardAttr.ScoreboardEntry.COLUMN_TARGETS
        };

        String[] bind = {
                ScoreboardAttr.ScoreboardEntry._ID,
                ScoreboardAttr.ScoreboardEntry.COLUMN_INITIALS,
                ScoreboardAttr.ScoreboardEntry.COLUMN_SCORE,
                ScoreboardAttr.ScoreboardEntry.COLUMN_TIME,
                ScoreboardAttr.ScoreboardEntry.COLUMN_TARGETS
        };

        Cursor cursor = db.query(ScoreboardAttr.ScoreboardEntry.TABLE_NAME, //table to query
                bind,
                null, //columns for where, Null will return all rows
                null, //values for where
                null, //Group By, null is no group by
                null, //Having, null says return all rows
                ScoreboardAttr.ScoreboardEntry.COLUMN_SCORE + " DESC",
                "10"
        );

        //array containing the 3 player attributes
        int[] attr = new int[]{R.id.highInitials,  R.id.highScore, R.id.highTime, R.id.numTargets};

        //cursor adapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.row_item, cursor, projection, attr, 0);

        //set the list to the adapter
        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        //prepare "empty" TextLayout
        TextView emptyView = (TextView) findViewById(android.R.id.empty);
        listView.setEmptyView(emptyView);
    }

    public void clearScoreboard(SQLiteDatabase db)
    {
        db.delete("player",null,null);
        db.execSQL("delete from "+ "player");
    }
}
