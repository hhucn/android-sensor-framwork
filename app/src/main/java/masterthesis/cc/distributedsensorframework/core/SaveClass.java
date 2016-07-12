package masterthesis.cc.distributedsensorframework.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

import masterthesis.cc.distributedsensorframework.core.db.Helper;
import masterthesis.cc.distributedsensorframework.core.db.Measurements;
import masterthesis.cc.distributedsensorframework.core.db.Source;

/**
 * Created by Christoph Classen on 10.05.16.
 */
public class SaveClass {
    private static SaveClass instance;

    private static final Logger LOG = LoggerFactory.getLogger(MeasurementActivity.class.getSimpleName());
    private Source source;


    private SaveClass(Context context){

        //Datenbankconnection BEGINN
        source = new Source(context);
        LOG.debug("Die Datenquelle wird geöffnet.");
        source.open();

    }



    public static synchronized SaveClass getInstance(Context context){
        if (SaveClass.instance == null ){
            SaveClass.instance = new SaveClass(context.getApplicationContext());  //wichtig, da nur "Context" evtl bei zerzörung der Activity auch verschwindeet
        }
        return instance;
    }



    public enum LogType{TRACE, INFO, WARN, ERROR};

    public void writeLog(LogType typ, String text){
        LOG.trace("text");
    }


    public void saveValue(Measurements measurements){
        //TODO WRITE INTO DB
        LOG.error(measurements.toString());
        LOG.debug(measurements.toString());
        LOG.trace(measurements.toString());
        LOG.info(measurements.toString());
        LOG.warn(measurements.toString());


        ContentValues values = new ContentValues();
        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put(Helper.COLUM_TIMESTAMP, dateFormat.format(measurements.getTimestamp()));
        values.put(Helper.COLUM_SENSOR, measurements.getSensor());
        values.put(Helper.COLUM_DEVICE, measurements.getDevice());
        values.put(Helper.COLUM_VALUE, measurements.getValue());

        long insertId = source.insert(values);
        LOG.debug("einfügeid ist: "+ insertId);
        Log.d("database", "einfügeid ist2:" + insertId + "##" + measurements.toString());
    }

    public Cursor loadValues(int limit){
        return source.load(limit);
    }

    public void finalize(){
        LOG.debug("Die Datenquelle wird geschlossen.");
        source.close();
        //DATENBANK ENDE
    }
}
