package masterthesis.cc.distributedsensorframework.core.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        return database.insert(Helper.TABLE_MEASUREMENTS, null, cv);

    }




    public Cursor load(int limit){
        Cursor cursor  = database.query(Helper.TABLE_MEASUREMENTS,null, null, null, null, null, null, limit+"");
        Log.e("Database load query", "geladene Zeilen: " + cursor.getColumnCount()+"");
        cursor.moveToFirst();
       // Measurements m = this.cursorToMesswert(cursor);
        //cursor.close();
        //cursor.moveToFirst();
        return cursor;
    }

    private Measurements cursorToMesswert(Cursor cursor){
        int idIndex = cursor.getColumnIndex(Helper.COLUM_ID);
        int idValue = cursor.getColumnIndex(Helper.COLUM_VALUE);
        int idDevice = cursor.getColumnIndex(Helper.COLUM_DEVICE);
        int idSensor = cursor.getColumnIndex(Helper.COLUM_SENSOR);
        int idTimestamp = cursor.getColumnIndex(Helper.COLUM_TIMESTAMP);

        int id = cursor.getInt(idIndex);
        String value = cursor.getString(idValue);
        String device = cursor.getString(idDevice);
        int sensor = cursor.getInt(idSensor);
        String timestamp = cursor.getString(idTimestamp);


         Log.i("Datenbankergebins", "Timepstamp:"+ timestamp);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date;
        try {
            date = df.parse(timestamp);
        }catch(ParseException e){
            Log.e("DB Scource", e.getMessage());
            date = null;
        }
        Measurements meas = new Measurements(id,date,sensor,value, device);
        Log.i("Datenbankergebnis:", meas.toString());
        return meas;

    }



    //auf basis von http://stackoverflow.com/questions/14509026/export-sqlite-into-csv


    public Boolean exportCSV(String outFileName) {
        Log.d("Source", "backupDatabaseCSV");
        Boolean returnCode = false;
        int i = 0;
        String csvHeader = "ID,SensorId,Value,Zeitstempel,GeraeteId";
        String csvValues = "";
       /* for (i = 0; i < Helper.CURCOND_COLUMN_NAMES.length; i++) {
            if (csvHeader.length() > 0) {
                csvHeader += ",";
            }
            csvHeader += "\"" + GC.CURCOND_COLUMN_NAMES[i] + "\"";
        }*/

        csvHeader += "\n";
        Log.d("source", "header=" + csvHeader);
        //open();
        try {
            File outFile = new File(outFileName);
            FileWriter fileWriter = new FileWriter(outFile);
            BufferedWriter out = new BufferedWriter(fileWriter);
            //Cursor cursor = dbAdapter.getAllRows();
            Cursor cursor = load(1000000);
            if (cursor != null) {
                out.write(csvHeader);
                while (cursor.moveToNext()) {
                    Measurements m = cursorToMesswert(cursor);
                    csvValues = Integer.toString(m.getId())  + ",";
                    csvValues += Integer.toString(m.getSensor()) + ",";
                    csvValues += "\"" + m.getValue() + "\",";
                    csvValues += m.getTimestamp().toString()  + ",";
                    csvValues += "\"" + m.getDevice() + "\""
                            + "\n";
                    out.write(csvValues);
                }
                cursor.close();
            }
            out.close();
            returnCode = true;
        } catch (IOException e) {
            returnCode = false;
            Log.d("Source", "IOException: " + e.getMessage());
        }
       // close();
        return returnCode;
    }




    /**
     * Funktion prüfen
     * @param cv
     * @param whereClause
     * @return
     */
    public long update(ContentValues cv, String whereClause){
        return  database.update(Helper.TABLE_MEASUREMENTS, cv, whereClause, null);
    }
}
