package masterthesis.cc.distributedsensorframework.core;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class MeasurementActivity extends Activity implements CvCameraViewListener, View.OnTouchListener {

    private static final String  TAG = "MeasurementActivity ";

    protected JavaCameraView mOpenCvCameraView;
    protected Processor mProcessor;
    protected SaveClass mSaving;
    protected Logger    LOG;

    private int                  mViewWidth;
    private int                  mViewHeight;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LOG = LoggerFactory.getLogger(MeasurementActivity.class);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        LOG.debug("Creating and setting View");
        //Log.d(TAG, "************************ Creating and seting view");
        mOpenCvCameraView =  new JavaCameraView(this, -1);
        mOpenCvCameraView.enableFpsMeter();
        mOpenCvCameraView.setCameraIndex(JavaCameraView.CAMERA_ID_BACK);


        setContentView(mOpenCvCameraView);
        mOpenCvCameraView.setCvCameraViewListener(this);

        mSaving = SaveClass.getInstance(this.getApplicationContext());

        mProcessor = new Processor(4);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }



    public void onCameraViewStarted(int width, int height) {
        mViewWidth = width;
        mViewHeight = height;
        mProcessor.prepareViewSize(mViewWidth, mViewHeight);
    }

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

    public Mat onCameraFrame(Mat inputFrame) {
        return mProcessor.processMatrix(inputFrame);
    }


    public void startService(){
        Intent i = new Intent(getApplicationContext(), SensorMaster.class);
        startService(i);
    }

    public void stopService(){
        Intent i = new Intent(getApplicationContext(), SensorMaster.class);
        stopService(i);
    }

    public void callback(ArrayList<String> words) {

    }
}