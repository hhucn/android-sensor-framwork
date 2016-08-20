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


    /**
     * Konstruktor, der alle Attribute setzt.
     * @param id int eindeutige ID
     * @param timestamp Date Messzeitpunkt
     * @param sensor int Sensor-ID
     * @param value String Messwert
     * @param device String Geräte-Identifikation
     */
    public Measurements(int id,Date timestamp, int sensor, String value, String device){
        this.id = id;
        this.timestamp=timestamp;
        this.sensor=sensor;
        this.value=value;
        this.device=device;
    }

    /**
     * Getter der ID
     * @return int ID
     */
    public int getId() {
        return id;
    }

    /**
     * Setter der ID
     * @param id int ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter der Sensor-ID
     * @return int ID
     */
    public int getSensor() {
        return sensor;
    }


    /**
     * Setter der SensorID
     * @param sensor int SensorID
     */
    public void setSensor(int sensor) {
        this.sensor = sensor;
    }


    /**
     * Getter des Messwertes
     * @return String Messwert
     */
    public String getValue() {
        return value;
    }


    /**
     * Setter des Messwertes
     * @param value String Messwert
     */
    public void setValue(String value) {
        this.value = value;
    }


    /**
     * Getter der Gerätebezeichnug
     * @return String Gerätebezeichnung
     */
    public String getDevice() {
        return device;
    }


    /**
     * Setter der Gerätebezeichnung
     * @param device String Gerätebezeichnung
     */
    public void setDevice(String device) {
        this.device = device;
    }


    /**
     * Getter des Messzeitpunktes
     * @return Date Messzeitpunkt
     */
    public Date getTimestamp() {
        return timestamp;
    }


    /**
     * Setter des Messzeitpunktes
     * @param timestamp Date Messzeitpunk
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    /**
     * Debugmethode zur des Objektes als menschenlesbaer String
     * @return String
     */
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
