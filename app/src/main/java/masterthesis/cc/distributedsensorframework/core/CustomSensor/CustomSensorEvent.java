package masterthesis.cc.distributedsensorframework.core.CustomSensor;

/**
 * Created by Christoph Classen
 */
public class CustomSensorEvent {

    private int     sensorType;
    private float[] values;
    private float   accuracy;
    private long    timestamp;


    /**
     * Getter für den Sensortyp
     * @return int Sensortyp
     */
    public int getSensorType() {
        return sensorType;
    }

    /**
     * Setter für den Sensortyp
     * @param sensorType int Sensortyp
     */
    public void setSensorType(int sensorType) {
        this.sensorType = sensorType;
    }

    /**
     * Getter für die Messwerte
     * @return Array of float Messwerte
     */
    public float[] getValues() {
        return values;
    }


    /**
     * Setter für die Messwerte
     * @param values Array of float Messwerte
     */
    public void setValues(float[] values) {
        this.values = values;
    }


    /**
     * Getter für die Sensorgenauigkeit
     * @return float Sensorgenauigkeit
     */
    public float getAccuracy() {
        return accuracy;
    }


    /**
     * Setter für die Sensorgenauigkeit
     * @param accuracy float Sensorgenauigkeit
     */
    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * Getter für den Messzeitpunkt
     * @return long Messzeitpunkt
     */
    public long getTimestamp() {
        return timestamp;
    }


    /**
     * Setter für den Messzeitpunkt
     * @param timestamp long Messzeitpunkt
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    /**
     * Getter für einen Teil eines mehrteiligen Messwert (z.B. GeoKoordinaten)
     * @param i int Position des Messwertteils
     * @return float Messwert
     */
    public float getValue(int i){
        return values[i];
    }
}
