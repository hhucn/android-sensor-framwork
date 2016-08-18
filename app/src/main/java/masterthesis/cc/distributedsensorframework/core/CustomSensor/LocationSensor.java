package masterthesis.cc.distributedsensorframework.core.CustomSensor;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import org.slf4j.LoggerFactory;

/**
 * Created by luke on 26.07.16.
 */
public class LocationSensor extends GenericSensor implements LocationListener{

    private String provider;
    private Location location;
    private LocationManager locMgr;


    public LocationSensor(Context context) {

        this.sensorID = 101;
        this.name = "GPS_LOCATION";
        this.LOG = LoggerFactory.getLogger(LocationSensor.class);
        this.TAG = "LocationSensor";
        locMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (! locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
          //  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          //  context.startActivity(intent); //TODO
            LOG.error("LocationSensor nicht verfügbar");
        }

        Criteria criteria = new Criteria();
        provider = locMgr.getBestProvider(criteria, false);
        location = locMgr.getLastKnownLocation(provider);

//        if (location != null) {
//            Log.w("LocationSensor","Provider " + provider + " has been selected.");
//            onLocationChanged(location);
//        }


    }


    public int getSensorId() {
        return this.sensorID;
    }


    public void registerListener(CustomSensorEventListener listener, int samplingPeriodUs) {
        this.sel = listener;
        this.samplingPeriodUs = samplingPeriodUs;
        locMgr.requestLocationUpdates(provider,samplingPeriodUs,0,this);
    }


    public void removeListener(){
        locMgr.removeUpdates(this);
    }



    public float[] getCurrentValue(){
        float[] val = new float[3];
        val[0]= (float)location.getLatitude();
        val[1]= (float)location.getLongitude();
        val[2]= (float)location.getAltitude();
        return  val;
    }

    public void onLocationChanged(Location location){
        this.location = location;

        CustomSensorEvent event = new CustomSensorEvent();

        float[] val = new float[3];
        val[0]= (float)location.getLatitude();
        val[1]= (float)location.getLongitude();
        val[2]= (float)location.getAltitude();

        event.setValues(val);
        event.setAccuracy(location.getAccuracy());
        event.setTimestamp(location.getTime());
        event.setSensorType(this.sensorID);

        onSensorChanged(event);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LOG.info(TAG +"providerStatusChanged: "+provider+"; neuer Status: " + status);
    }


    @Override
    public void onProviderEnabled(String provider) {
        LOG.info(TAG + "providerEnabled: " + provider);
    }


    @Override
    public void onProviderDisabled(String provider) {
        LOG.info(TAG +  "providerDisabled: " + provider);
    }


    @Override
    public void onSensorChanged(CustomSensorEvent event) {
        sel.onSensorChanged(event);
    }

}
