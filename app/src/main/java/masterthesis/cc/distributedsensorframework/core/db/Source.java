package masterthesis.cc.distributedsensorframework.core.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Christoph Classen on 19.04.16.
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

    public Cursor load(int limit){
        Cursor cursor  = database.query(Helper.TABLE_MEASUREMENTS,null, null, null, null, null, null, limit+"");
        Log.i("Database load query", "geladene Zeilen: " + cursor.getColumnCount()+"");
        cursor.moveToFirst();
        Measurements m = this.cursorToMesswert(cursor);
        cursor.close();
        cursor.moveToFirst();
        return cursor;
    }

    private Measurements cursorToMesswert(Cursor cursor){
        //int idIndex = cursor.getColumnIndex(Helper.COLUM_ID);
        int idValue = cursor.getColumnIndex(Helper.COLUM_VALUE);
        int idDevice = cursor.getColumnIndex(Helper.COLUM_DEVICE);
        int idSensor = cursor.getColumnIndex(Helper.COLUM_SENSOR);
        int idTimestamp = cursor.getColumnIndex(Helper.COLUM_TIMESTAMP);

        //int id = cursor.getInt(idIndex);
        double value = cursor.getDouble(idValue);
        String device = cursor.getString(idDevice);
        int sensor = cursor.getInt(idSensor);
        String timestamp = cursor.getString(idTimestamp);


         Log.e("Datenbankergebins", "Timepstamp:"+ timestamp);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date;
        try {
            date = df.parse(timestamp);
        }catch(ParseException e){
            Log.e("DB Scource", e.getMessage());
            date = null;
        }
        Measurements meas = new Measurements(date,sensor,value, device);
        Log.e("Datenbankergebnis:", meas.toString());
        return meas;

    }
}
