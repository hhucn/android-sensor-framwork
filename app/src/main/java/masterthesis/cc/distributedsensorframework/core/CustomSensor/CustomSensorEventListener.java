package masterthesis.cc.distributedsensorframework.core.CustomSensor;

/**
 * Created by Christoph Classen
 */
public interface CustomSensorEventListener {

    void onSensorChanged(CustomSensorEvent event);

    void onSensorStatusChanged();
}
