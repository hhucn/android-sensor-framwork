package masterthesis.cc.distributedsensorframework.core.CustomSensor;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import org.slf4j.LoggerFactory;

/**
 * Created by Christoph Classen
 */
public class LocationSensor extends GenericSensor implements LocationListener{

    private String provider;
    private Location location;
    private LocationManager locMgr;

    /**
     * Konstruktor, der die Konstanten einrichtet.
     * @param context Context Applikationskontext
     */
    public LocationSensor(Context context) {
        this.sensorID = 101;
        this.name = "GPS_LOCATION";
        this.LOG = LoggerFactory.getLogger(LocationSensor.class);
        this.TAG = "LocationSensor";
        locMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (! locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LOG.error("LocationSensor nicht verfügbar");
        }

        Criteria criteria = new Criteria();
        provider = locMgr.getBestProvider(criteria, false);
        location = locMgr.getLastKnownLocation(provider);
    }


    /**
     * Getter der SensorID
     * @return int SensorID
     */
    public int getSensorId() {
        return this.sensorID;
    }

    /**
     * Setzt den Litener und die zeitliche Sensorauflösung
     * @param listener CustomEvnetListener
     * @param samplingPeriodUs int Zeitliche Auflösung in Millisekunden
     */
    public void registerListener(CustomSensorEventListener listener, int samplingPeriodUs) {
        this.sel = listener;
        this.samplingPeriodUs = samplingPeriodUs;
        locMgr.requestLocationUpdates(provider,samplingPeriodUs,0,this);
    }

    /**
     * abfrage der Geokoordinaten beenden
     */
    public void removeListener(){
        locMgr.removeUpdates(this);
    }


    /**
     * Getter aller Sensorwerte, Latitute, Longitute und Altitude
     * @return Array of float Sensormessdaten
     */
    public float[] getCurrentValue(){
        float[] val = new float[3];
        val[0]= (float)location.getLatitude();
        val[1]= (float)location.getLongitude();
        val[2]= (float)location.getAltitude();
        return  val;
    }


    /**
     * neue Geokoordinaten verfügbar
     * Location wird zu CustomSensorEvent umgeformt und onSensorChanged() aufgerufen
     * @param location Location neue Position
     */
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

    /**
     * Provider enabled, Geokoordinaten können nun empfangen werden
     * @param provider
     */
    @Override
    public void onProviderEnabled(String provider) {
        LOG.info(TAG + "providerEnabled: " + provider);
    }

    /**
     * Provider disabled
     * @param provider Provider, der disabled wurde
     */
    @Override
    public void onProviderDisabled(String provider) {
        LOG.info(TAG +  "providerDisabled: " + provider);
    }

    /**
     * Neue Positionsdaten empfangen
     * @param event CustomSensorEvent Objekt mit den neuen Messdaten
     */
    @Override
    public void onSensorChanged(CustomSensorEvent event) {
        sel.onSensorChanged(event);
    }

}
