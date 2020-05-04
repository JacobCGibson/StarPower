package com.google.ar.core.examples.java.StarPower;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoreboardDB extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "scoreboard.db";
    private static final String TEXT_TYPE = "TEXT";
    private static final String COMMA_SEPARATOR = ", ";
    private static final String SQL_CREATE_ENTRY = "CREATE TABLE " + ScoreboardAttr.ScoreboardEntry.TABLE_NAME + " (" +
            ScoreboardAttr.ScoreboardEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEPARATOR +
            ScoreboardAttr.ScoreboardEntry.COLUMN_TIME+" "+ TEXT_TYPE + COMMA_SEPARATOR +
            ScoreboardAttr.ScoreboardEntry.COLUMN_SCORE+" "+ "INTEGER" + COMMA_SEPARATOR +
            ScoreboardAttr.ScoreboardEntry.COLUMN_INITIALS+" "+ TEXT_TYPE + COMMA_SEPARATOR +
            ScoreboardAttr.ScoreboardEntry.COLUMN_TARGETS+" "+ TEXT_TYPE + ")";

    //deletion statement
    private static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + ScoreboardAttr.ScoreboardEntry.TABLE_NAME;

    public ScoreboardDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ScoreboardDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRY);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }
    public void refreshScoreboard(SQLiteDatabase db) { db.execSQL(SQL_DELETE); db.execSQL(SQL_CREATE_ENTRY);}
}
