package masterthesis.cc.distributedsensorframework.core.CustomSensor;

import masterthesis.cc.distributedsensorframework.core.CustomSensor.CustomSensorEvent;

/**
 * Created by luke on 28.07.16.
 */
public interface CustomSensorEventListener {

    void onSensorChanged(CustomSensorEvent event);

    void onSensorStatusChanged();
}
