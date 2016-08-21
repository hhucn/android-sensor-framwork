package masterthesis.cc.distributedsensorframework.core;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


/**
 * Created by Christoph Classen
 * Basisklasse für Messungen mit Cameraview und OpenCV
 */
public class MeasurementActivity extends Activity implements CvCameraViewListener2, View.OnTouchListener {

    private static final    String          TAG = "MeasurementActivity ";
    protected               JavaCameraView  mOpenCvCameraView;
    protected               Processor       mProcessor;
    protected               SaveClass       mSaving;
    protected               Logger          LOG;

    private                 int             mViewWidth;
    private                 int             mViewHeight;


    protected BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    LOG.info(TAG +  "OpenCV loaded successfully");

                    /* Now enable camera view to start receiving frames */
                    mOpenCvCameraView.setOnTouchListener(MeasurementActivity.this);
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    /**
     * Activity erstellen
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LOG = LoggerFactory.getLogger(MeasurementActivity.class);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        LOG.debug("Creating and setting View");
        mOpenCvCameraView =  new JavaCameraView(this, -1);

        mOpenCvCameraView.enableFpsMeter();
        mOpenCvCameraView.setCameraIndex(JavaCameraView.CAMERA_ID_BACK);

        setContentView(mOpenCvCameraView);
        mOpenCvCameraView.setCvCameraViewListener(this);

        mSaving = SaveClass.getInstance(this.getApplicationContext());

        mProcessor = new Processor();
    }


    /**
     * Pausierung der Activity Behandeln und Kamerazugriff und View beenden
     */
    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }


    /**
     * Bei (Re-)Start der Activity OpenCV laden
     */
    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
    }


    /**
     * Zerstörung der Activity behandleln und Kameraview(Kamerazugriff) beenden
     */
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }


    /**
     * Kamerazugirff starten
     * @param width -  the width of the frames that will be delivered
     * @param height - the height of the frames that will be delivered
     */
    public void onCameraViewStarted(int width, int height) {
        mViewWidth = width;
        mViewHeight = height;
        mProcessor.prepareViewSize(mViewWidth, mViewHeight);
    }


    /**
     * Kamerazugriff beenden
     */
    public void onCameraViewStopped() {
    }



    public boolean onTouch(View view, MotionEvent event) {

      int xpos, ypos;

        xpos = (view.getWidth() - mViewWidth) / 2;
        xpos = (int)event.getX() - xpos;

        ypos = (view.getHeight() - mViewHeight) / 2;
        ypos = (int)event.getY() - ypos;

        if (xpos >=0 && xpos <= mViewWidth && ypos >=0  && ypos <= mViewHeight) {
            // click is inside the picture. Deliver this event to processor
            mProcessor.deliverTouchEvent(xpos, ypos);
        }


        return false;
    }


    /**
     * Verarbeitet das Kamerabild
     * @param inputFrame Mat neues Kamerabild
     * @return Mat bearbeitetes Kamerabild zur Anzeige durch die CameraView
     */
    public Mat onCameraFrame(Mat inputFrame) {
        return mProcessor.processMatrix(inputFrame);
    }


    /**
     * Verarbeitet das Kamerabild
     * @param inputframe CvCameraViewFrame neues Kamerabild
     * @return Mat bearbeitetes Kamerabild zur Anzeige durch die CameraView
     */
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputframe){
        return mProcessor.processMatrix(inputframe.rgba());
    }


    /**
     * startet Hintergrund Sensorservice
     */
    public void startService(){
        Intent i = new Intent(getApplicationContext(), SensorMaster.class);
        startService(i);
    }


    /**
     * stoppt Hintergrund-Sensorservice
     */
    public void stopService(){
        Intent i = new Intent(getApplicationContext(), SensorMaster.class);
        stopService(i);
    }

    /**
     * DummyCallback zum Datenempfang des Hintergrundservices
     * @param words Sensordeaten vom Service
     */
    public void callback(ArrayList<String> words) {

    }
}