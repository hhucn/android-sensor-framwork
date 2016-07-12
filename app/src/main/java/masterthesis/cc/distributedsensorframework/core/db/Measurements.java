package masterthesis.cc.distributedsensorframework.core.db;

import java.util.Date;

/**
 * Created by Christoph Classen on 19.04.16.
 * Diese Klasse repräsentiert genau einen Messwert, wie er auch in der DB gespeichert wird.
 */
public class Measurements {

    private Date timestamp;
    private int sensor;
    private double value;
    private String device;

    public Measurements(Date timestamp, int sensor, double value, String device){
        this.timestamp=timestamp;
        this.sensor=sensor;
        this.value=value;
        this.device=device;
    }


    public int getSensor() {
        return sensor;
    }

    public void setSensor(int sensor) {
        this.sensor = sensor;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return "Measurements{" +
                "timestamp=" + timestamp.toString() +
                ", sensor=" + sensor +
                ", value=" + value +
                ", device='" + device + '\'' +
                '}';
    }
}
