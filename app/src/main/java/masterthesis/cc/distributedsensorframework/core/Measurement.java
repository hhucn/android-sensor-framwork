package masterthesis.cc.distributedsensorframework.core;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by luke on 11.05.16.
 */
public abstract class Measurement {

    private ArrayList<SensorType> av_sensors;
    private ArrayList<SensorType> primary_sensors;
    private ArrayList<SensorType> meta_sensors;

    public enum SensorType{LIGHT, PRESSURE, CAMERA, PROXIMITY}

    public Measurement(){
        //TODO verfügbare Sensoren auslesen
        //this.av_sensors =.....
    }

    public abstract void beginSession();

    public abstract void pauseSession();

    public abstract void restartSession();

    public abstract void endSession();

    public ArrayList<SensorType> getSensors(){
        return this.primary_sensors;
    }

    public void setSensors(ArrayList<SensorType> sensors){
        this.primary_sensors = sensors;
    }

    public void setMetaSensors(ArrayList<SensorType> sensors){
        this.meta_sensors = sensors;
    }

    public ArrayList<SensorType> getMetasensors(){
        return this.meta_sensors;
    }

    public ArrayList<SensorType> getAvailibleSensors(){
        return this.av_sensors;
    }

}
