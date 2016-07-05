package masterthesis.cc.distributedsensorframework.customImpl;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;

import masterthesis.cc.distributedsensorframework.core.Measurement;
import masterthesis.cc.distributedsensorframework.core.MeasurementActivity;
import masterthesis.cc.distributedsensorframework.core.SaveClass;
import masterthesis.cc.distributedsensorframework.core.db.Measurements;

/**
 * Created by luke on 11.05.16.
 */
public class MyMeasurementActivity extends MeasurementActivity {




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mProcessor = new MyProcessor(4);

//  ((MyProcessor)mProcessor).setMinContourArea(3.0);




   /*     this.mOpenCvCameraView.setColorEffect(Camera.Parameters.EFFECT_NONE);
        mOpenCvCameraView.setColorEffect(Camera.Parameters.EFFECT_NONE);
        mOpenCvCameraView.setFocus(Camera.Parameters.FOCUS_MODE_FIXED);
        mOpenCvCameraView.setLockWhiteBalance(true);
        mOpenCvCameraView.setZoom(2);*/
        Log.e("MYmeasuerment", "-##########################started###########################");

mSaving.writeLog(SaveClass.LogType.WARN, "heyho");
        Measurements meas = new Measurements(1,2,3.0,"Whatever");
        mSaving.saveValue(meas);
    }
/*
    @Override
    public void beginSession() {

    }

    @Override
    public void pauseSession() {

    }

    @Override
    public void restartSession() {

    }

    @Override
    public void endSession() {

    }*/
}
