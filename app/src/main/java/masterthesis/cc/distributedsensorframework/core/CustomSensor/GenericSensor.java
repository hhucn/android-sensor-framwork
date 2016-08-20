package masterthesis.cc.distributedsensorframework.core.CustomSensor;

import org.slf4j.Logger;

import masterthesis.cc.distributedsensorframework.core.CustomSensor.CustomSensorEvent;
import masterthesis.cc.distributedsensorframework.core.CustomSensor.CustomSensorEventListener;

/**
 * Created by Christoph Classen
 */
public abstract class GenericSensor {

    protected int                       sensorID            = 0;
    protected String                    name                = "GenericSensor";
    protected CustomSensorEventListener sel                 = null;
    protected int                       samplingPeriodUs    = 0;
    protected Logger                    LOG;
    protected String                    TAG                 = "GenericSensor ";

    /**
     * Abstrakter Konstruktor
     */
    public  GenericSensor(){}

    /**
     * abstrakter Getter für die SensorID
     * @return int Sensor ID
     */
    public abstract int getSensorId();

    /**
     * Abstrakte Methode zum registrieren des Listeners
     * @param sel CustomSensorEventListener
     * @param samplingPeriodUs int Zeitliche Auflösung in Millisekunden
     */
    public abstract void registerListener(CustomSensorEventListener sel, int samplingPeriodUs);

    /**
     * Abstrakte Methode, die bei einem geänderten Sensorwert aufgerufen wird
     * @param event CustomSensorEvent Objekt mit den neuen Messdaten
     */
    public abstract void onSensorChanged(CustomSensorEvent event);


}

