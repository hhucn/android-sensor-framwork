package masterthesis.cc.distributedsensorframework.core.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private SQLiteDatabase  database;
    private Helper          dbHelper;
    private Logger          LOG;


    /**
     * Konstruktor
     * @param context Context Applikationskontext
     */
    public Source(Context context){
        dbHelper = new Helper(context);
        LOG = LoggerFactory.getLogger(Source.class);
    }


    /**
     * Öffnet eine Verbindung zur Datenbank
     */
    public void open() {
        LOG.info("Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        LOG.info("Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }


    /**
     * Schließt die Verbindung zur DB
     */
    public void close() {
        dbHelper.close();
        LOG.info("Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    /**
     * Speichert den Datensatz in der Messdatentabelle
     * @param cv ContentValues zu speichernder Datensatz
     * @return long Insert-ID
     */
    public long insert(ContentValues cv){
        return database.insert(Helper.TABLE_MEASUREMENTS, null, cv);
    }


    /**
     * Lädt eine gewisse Anzahl an Zeilen aus der Messdatentabelle
     * @param limit int Maximale Zeilenzahl
     * @return Cursor Datenbankcursor
     */
    public Cursor load(int limit){
        Cursor cursor  = database.query(Helper.TABLE_MEASUREMENTS,null, null, null, null, null, null, limit+"");
        LOG.info("Anzahl geladener Zeilen: " + cursor.getColumnCount()+"");
        cursor.moveToFirst();
        return cursor;
    }


    /**
     *
     * @param cursor
     * @return
     */
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

        LOG.info("Datenbankergebins: Timepstamp:"+ timestamp);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date;
        try {
            date = df.parse(timestamp);
        }catch(ParseException e){
            LOG.error("Fehler beim parsen der Datumsangaben" + e.getMessage());
            date = null;
        }
        Measurements meas = new Measurements(id,date,sensor,value, device);
        LOG.info(meas.toString());
        return meas;

    }


    /**
     * Speichert den Inhalt der Messdatentabelle in einer CSV Datei
     * @param outFileName String Pfad und Dateiname
     * @return Boolean (Miss-)erfolgsmeldung
     * auf basis von http://stackoverflow.com/questions/14509026/export-sqlite-into-csv
     */
    public Boolean exportCSV(String outFileName) {
        LOG.debug("saving Backup as CSV:");
        Boolean returnCode = false;
        int i = 0;
        String csvHeader = "ID,SensorId,Value,Zeitstempel,GeraeteId";
        String csvValues = "";
        csvHeader += "\n";
        LOG.debug("Header: " + csvHeader);
        try {
            File outFile = new File(outFileName);
            FileWriter fileWriter = new FileWriter(outFile);
            BufferedWriter out = new BufferedWriter(fileWriter);
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
            LOG.error("IOException beim CSV Esport: " + e.getMessage());
        }
        return returnCode;
    }




    /**
     * Update der
     * @param cv
     * @param whereClause
     * @return
     */
    public long upsdate(ContentValues cv, String whereClause){
        return  database.update(Helper.TABLE_MEASUREMENTS, cv, whereClause, null);
    }
}
