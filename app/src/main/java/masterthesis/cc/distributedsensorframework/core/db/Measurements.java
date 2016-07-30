package masterthesis.cc.distributedsensorframework.core.db;

import java.util.Date;

/**
 * Created by Christoph Classen on 19.04.16.
 * Diese Klasse repräsentiert genau einen Messwert, wie er auch in der DB gespeichert wird.
 */
public class Measurements {
    private int id =0;
    private Date timestamp;
    private int sensor;
    private String value;
    private String device;

    public Measurements(int id,Date timestamp, int sensor, String value, String device){
        this.id = id;
        this.timestamp=timestamp;
        this.sensor=sensor;
        this.value=value;
        this.device=device;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getSensor() {
        return sensor;
    }

    public void setSensor(int sensor) {
        this.sensor = sensor;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
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
