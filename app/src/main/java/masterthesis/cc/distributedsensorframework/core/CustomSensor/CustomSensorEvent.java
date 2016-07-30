package masterthesis.cc.distributedsensorframework.core.CustomSensor;

/**
 * Created by luke on 29.07.16.
 */
public class CustomSensorEvent {

    private int sensorType;
    private float[] values;
    private float accuracy;
    private long timestamp;


    public int getSensorType() {
        return sensorType;
    }

    public void setSensorType(int sensorType) {
        this.sensorType = sensorType;
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getValue(int i){
        return values[i];
    }
}
