package masterthesis.cc.distributedsensorframework.core.db;

/**
 * Created by luke on 19.04.16.
 */
public class Measurements {

    private int time;
    private int sensor;
    private double value;
    private String device;

    public Measurements(int time, int sensor, double value, String device){
        this.time=time;
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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Measurements{" +
                "time=" + time +
                ", sensor=" + sensor +
                ", value=" + value +
                ", device='" + device + '\'' +
                '}';
    }
}
