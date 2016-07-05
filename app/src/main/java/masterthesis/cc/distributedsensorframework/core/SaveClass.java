package masterthesis.cc.distributedsensorframework.core;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import masterthesis.cc.distributedsensorframework.core.db.Helper;
import masterthesis.cc.distributedsensorframework.core.db.Measurements;
import masterthesis.cc.distributedsensorframework.core.db.Source;

/**
 * Created by luke on 10.05.16.
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
        values.put(Helper.COLUM_SENSOR, measurements.getSensor());
        values.put(Helper.COLUM_DEVICE, measurements.getDevice());
        values.put(Helper.COLUM_VALUE, measurements.getValue());

        long insertId = source.insert(values);
        LOG.debug("einfügeid ist: "+ insertId);

    }

    public void finalize(){
        LOG.debug("Die Datenquelle wird geschlossen.");
        source.close();
        //DATENBANK ENDE
    }
}
