package masterthesis.cc.distributedsensorframework.core.CustomSensor;

import org.slf4j.Logger;

import masterthesis.cc.distributedsensorframework.core.CustomSensor.CustomSensorEvent;
import masterthesis.cc.distributedsensorframework.core.CustomSensor.CustomSensorEventListener;

/**
 * Created by luke on 26.07.16.
 */
public abstract class GenericSensor {

    protected int sensorID = 0;
    protected String name = "GenericSensor";
    protected CustomSensorEventListener sel = null;
    protected int samplingPeriodUs = 0;
    protected Logger LOG;
    protected String TAG = "GenericSensor ";

    public  GenericSensor(){}

    public abstract int getSensorId();

    public abstract void registerListener(CustomSensorEventListener sel, int samplingPeriodUs);

    public abstract void onSensorChanged(CustomSensorEvent event);


}

