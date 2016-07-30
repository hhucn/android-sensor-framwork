package masterthesis.cc.distributedsensorframework.core.CustomSensor;

import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

/**
 * Created by luke on 29.07.16.
 */
public class RssiSensor extends GenericSensor {

    private WifiManager wifi ;
    private Context context;

    public RssiSensor(Context context) {
            this.sensorID = 102;
            this.context = context;
            wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);



    }

    @Override
    public int getSensorId() {
        return this.sensorID;
    }

    public float[] getCurrentValue(){
        float[] var = new float[1];
        var[0]= wifi.getConnectionInfo().getRssi();
        return var;
    }

    /**
     *
     * @param listener
     * @param samplingPeriodUs
     */
    @Override
    public void registerListener(CustomSensorEventListener listener, int samplingPeriodUs) {

        this.sel = listener;

        context.registerReceiver(this.RssiReciever, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
    }

    @Override
    public void onSensorChanged(CustomSensorEvent event) {

    }


    private BroadcastReceiver RssiReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            float[] val = new float[1];
            val[0]= intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI,0);
                    //wifi.getConnectionInfo().getRssi();

            CustomSensorEvent event = new CustomSensorEvent();

            event.setValues(val);
            event.setAccuracy(0);

            event.setTimestamp(0);
            event.setSensorType(102);


            onSensorChanged(event);
        }
    };
}
