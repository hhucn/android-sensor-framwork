package masterthesis.cc.distributedsensorframework.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by luke on 04.04.16.
 */
public class StartServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("StartRec", "StartServiceReciever on receive");
        Intent service = new Intent(context, SensorMaster.class);
        context.startService(service);
    }
}