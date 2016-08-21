package masterthesis.cc.distributedsensorframework.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import masterthesis.cc.distributedsensorframework.core.CustomSensor.CustomSensorEvent;
import masterthesis.cc.distributedsensorframework.core.CustomSensor.CustomSensorEventListener;
import masterthesis.cc.distributedsensorframework.core.CustomSensor.LocationSensor;
import masterthesis.cc.distributedsensorframework.core.CustomSensor.RssiSensor;
import masterthesis.cc.distributedsensorframework.core.db.Measurements;

/**
 * Created by Christoph Classen
 */
public class SensorMaster extends Service {


    private final   IBinder             mBinder = new MyBinder();
    private         ArrayList<String>   list = new ArrayList<String>();
    private         SensorManager       mgr;

    protected       SaveClass           saveClass;
    protected       Logger              LOG;
    protected       String              TAG = "SensorMaster ";

    private         MeasurementActivity callbackActivity;

    private         LocationSensor      ls;
    private         RssiSensor          rs;


    /**
     * Konstruktor zur erstellung des Hintergrungservices
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LOG = LoggerFactory.getLogger(SensorMaster.class);
        LOG.info(TAG + "service started");

        saveClass = SaveClass.getInstance(getApplicationContext());

        mgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
       // mgr.registerListener(listener, mgr.getDefaultSensor(Sensor.TYPE_LIGHT),SensorManager.SENSOR_DELAY_UI);
        //mgr.registerListener(listener, mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),1000*30 );

        mgr.registerListener(listener, mgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 1000*10);

        ls = new LocationSensor(getApplicationContext());
        ls.registerListener(customListener, 60000);

        rs = new RssiSensor(getApplicationContext());
        rs.registerListener(customListener,0);

        return Service.START_STICKY;
    }


    @Override
    public  void onDestroy(){
        ls.removeListener();

        Log.e("Sensormaster", System.currentTimeMillis() + ": service gesoppt");
        super.onDestroy();
    }




    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }


    public class MyBinder extends Binder {
        SensorMaster getService() {
            saveClass.writeLog(SaveClass.LogType.INFO, "BinderGetServiec");
            return SensorMaster.this;
        }
    }


    /**
     * Behandelt Änderungen der Sensormesswerte für Android Standard Sensoren
     */
    protected SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event){

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//                float x,y,z;
//                x = event.values[0];
//                y= event.values[1];
//                z= event.values[2];
                saveToDb(Sensor.TYPE_ACCELEROMETER, event);
//                list.add(0,event.timestamp + ": "+ x + "|" + y + "|" + z+ " (Acc: "+event.accuracy+")");
//                updateCallback();
            }

            if (event.sensor.getType() == Sensor.TYPE_LIGHT){
                saveToDb(Sensor.TYPE_LIGHT, event);
                //float x;
//                x = event.values[0];
//              //  list.add(0, event.timestamp + ": " +x + "(Acc: "+event.accuracy+")");
//                list.add(0, x + "");
//
//                updateCallback();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //unused

        }
    };



    /**
     * Behandelt Änderungen der Sensormesswerte für eigene Sensoren
     */
    protected CustomSensorEventListener customListener = new CustomSensorEventListener() {

        @Override
        public void onSensorChanged(CustomSensorEvent event) {
            String devicename = Build.MODEL + " ("+Build.PRODUCT+") " + Build.ID +"|"+ Build.SERIAL;


            if (event.getSensorType()== 101) {
                Measurements messung = new Measurements(0,new Date(),101, event.getValue(0)+" " + event.getValue(1),devicename);
                Log.e("SensorMaster", messung.toString());
                saveClass.saveValue(messung);

            }else if (event.getSensorType() ==102) {
                Log.w("SensorMaster", "Current RSSI Value2: " + rs.getCurrentValue()[0]);
            }else{
                Log.w("SensorMaster", "was anderes");
            }
        }

        @Override
        public void onSensorStatusChanged() {

        }
    };


    /**
     * Speichert einen Sensorwert in die DB
     * @param sensortype int ID des sensors
     * @param event SensorEvent ausgelöstes Event mit Messwerten
     */
    public void saveToDb(int sensortype, SensorEvent event){

        Log.e("Sensorvalues",  event.values[0] +"; " +event.values[1] +"; " +event.values[2] +"; " );
        String devicename = Build.MODEL + " ("+Build.PRODUCT+") " + Build.ID +"|"+ Build.SERIAL;


        Measurements messung = new Measurements(0,new Date(),sensortype, event.values[0]+"",devicename);
        Log.e("SensorMaster", messung.toString());
         saveClass.saveValue(messung);

    }


    /**
     * Liefert die Messdatenliste zurück an die als Callbac eingetragene Klasse
     */
    public void updateCallback(){
        if (this.callbackActivity != null) {
            this.callbackActivity.callback(this.list);
        }
    }

    /**
     * Callback zur GUI setzen
     * @param m MeasurementActivity, die die Callbacks empfangen soll
     */
    public void registerCallback(MeasurementActivity m){
        this.callbackActivity = m;
    }


    /**
     * Getter für die Messdatenliste
     * @return List of String Liste der Messdaten
     */
    public List<String> getWordList() {
        return list;
    }





















}
