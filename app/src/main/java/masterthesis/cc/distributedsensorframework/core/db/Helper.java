package masterthesis.cc.distributedsensorframework.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by luke on 19.04.16.
 */
public class Helper extends SQLiteOpenHelper {


    public static final String DB_NAME = "DistributedSensorFrameworkDB";
    public static final int DB_VERSION = 1;

    public static final String TABLE_MEASUREMENTS = "measurements_raw";

    public static final String COLUM_ID = "_id";
    public static final String COLUM_SENSOR = "sensorid";
    public static final String COLUM_VALUE = "value";
    public static final String COLUM_DEVICE = "device";

    public static final String SQL_CREATE =
            "CREATE TABLE" + TABLE_MEASUREMENTS + "(" + COLUM_ID + "INTEGER PRIMARY AUTOINCREMENT, " +
                    COLUM_SENSOR + "INTEGER NOT NULL" +
                    COLUM_VALUE + "DOUBLE NOT NULL" +
                    COLUM_DEVICE + "TEXT NOT NULL);";



    public Helper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        //DB erzeugt
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE);
        }catch (Exception e){
            Log.e("DatenbankNichtAngelegt", e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
