package masterthesis.cc.distributedsensorframework.core.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by luke on 19.04.16.
 */
public class Source {

    private SQLiteDatabase database;
    private Helper dbHelper;


    public Source(Context context){
        dbHelper = new Helper(context);
    }

    public void open() {
        Log.d("DATABASE", "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d("DATABASE", "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d("DATABASE", "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }


    public long insert(ContentValues cv){
        return database.insert(Helper.TABLE_MEASUREMENTS,null,cv);
    }
}
