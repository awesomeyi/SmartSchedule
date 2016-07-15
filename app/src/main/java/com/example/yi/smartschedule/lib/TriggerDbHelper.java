package com.example.yi.smartschedule.lib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.yi.smartschedule.lib.TriggerContract.TriggerEntry;

/**
 * Created by jackphillips on 7/15/16.
 */
public class TriggerDbHelper extends SQLiteOpenHelper {


    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TriggerEntry.TABLE_NAME + " (" +
                    TriggerEntry._ID + " INTEGER PRIMARY KEY," +
                    TriggerEntry.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
                    TriggerEntry.COLUMN_NAME_ADTIONAL_INFO + TEXT_TYPE + COMMA_SEP +
                    TriggerEntry.COLUMN_NAME_ACTIONS + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TriggerEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public TriggerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
