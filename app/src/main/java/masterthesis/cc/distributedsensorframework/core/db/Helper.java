package masterthesis.cc.distributedsensorframework.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import masterthesis.cc.distributedsensorframework.core.MeasurementActivity;

/**
 * Created by luke on 19.04.16.
 */
public class Helper extends SQLiteOpenHelper {

    private static final Logger LOG = LoggerFactory.getLogger(Helper.class.getSimpleName());


    public static final String DB_NAME = "sensordata.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_MEASUREMENTS = "measurements_raw";

    public static final String COLUM_ID = "_id";
    public static final String COLUM_SENSOR = "sensorid";
    public static final String COLUM_VALUE = "messwert";
    public static final String COLUM_DEVICE = "device";
    public static final String COLUM_TIMESTAMP = "created_at";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_MEASUREMENTS + " ( " + COLUM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUM_SENSOR + " INTEGER NOT NULL, " +
                    COLUM_VALUE + " DOUBLE NOT NULL, " +
                    COLUM_TIMESTAMP + " DATE NOT NULL, " +
                    COLUM_DEVICE + " TEXT NOT NULL ) ; ";



    public Helper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        //DB erzeugt
        Log.d("DATABASE", "DB Helper erstellt");
        LOG.debug("DB Helper erstellt2");
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE);
            Log.d("DATABASE", "Datenbank angelegt");
            LOG.debug("Datenban angelegt2");
        }catch (Exception e){
            Log.e("DATABASE", "Datenbank nicht angelegt: " + e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
