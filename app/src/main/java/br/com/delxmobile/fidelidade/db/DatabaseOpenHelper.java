package br.com.delxmobile.fidelidade.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Guilherme on 26/09/2017.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    protected static final String TAG = "DB_DEBUG";
    private static DatabaseOpenHelper sInstance;

    public static final String DATABASE_NAME = "fidelidade";
    protected static final int DATABASE_VERSION = 1;

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    public static synchronized DatabaseOpenHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DatabaseOpenHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private Context context;
    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String[] table_creator_commands = GenericSQLFileReader.getCommands(context, "table_creator.sql");
        for(String command : table_creator_commands) {
            db.execSQL(command);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");



    }

}