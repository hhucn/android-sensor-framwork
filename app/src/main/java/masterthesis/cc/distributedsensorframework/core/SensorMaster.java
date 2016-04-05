package masterthesis.cc.distributedsensorframework.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 04.04.16.
 */
public class SensorMaster extends Service {

    private final IBinder mBinder = new MyBinder();
    private ArrayList<String> list = new ArrayList<String>();
    private SensorManager mgr;
    private ArrayList<String> primarySensors= new ArrayList<String>();
    private ArrayList<String> secondarySensors= new ArrayList<String>();


    private MainActivity callbackActivity;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.primarySensors = intent.getStringArrayListExtra("primarySensors");
        this.secondarySensors = intent.getStringArrayListExtra("secondarySensors");

        Log.d("Service", "Primary: "  + this.primarySensors.toString());
        Log.d("Service", "Secondrary Sensor: "+ this.secondarySensors.toString());

        mgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mgr.registerListener(listener, mgr.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_UI);
      //  mgr.registerListener(listener, mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);

        //return Service.START_STICKY;
        return Service.START_NOT_STICKY;
    }



    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }


    public class MyBinder extends Binder {
        SensorMaster getService() {
            Log.d("Sercie", "BinderGetServiec");
            return SensorMaster.this;
        }
    }


    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event){
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                float x,y,z;
                x = event.values[0];
                y= event.values[1];
                z= event.values[2];
                list.add(0,event.timestamp + ": "+ x + "|" + y + "|" + z+ " (Acc: "+event.accuracy+")");
                updateCallback();
            }

            if (event.sensor.getType() == Sensor.TYPE_LIGHT){
                float x;
                x = event.values[0];
              //  list.add(0, event.timestamp + ": " +x + "(Acc: "+event.accuracy+")");
                list.add(0, x + "");

                updateCallback();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //unused
        }
    };





    public void updateCallback(){
        if (this.callbackActivity != null) {
            this.callbackActivity.callback(this.list);
        }
    }


    @Override
    public  void onDestroy(){
        super.onDestroy();
        mgr.unregisterListener(listener);
    }


    public void registerCallback(MainActivity m){
        this.callbackActivity = m;
    }

    public List<String> getWordList() {
        return list;
    }































}
