package masterthesis.cc.distributedsensorframework.customImpl;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import masterthesis.cc.distributedsensorframework.R;
import masterthesis.cc.distributedsensorframework.core.CustomSensor.RssiSensor;
import masterthesis.cc.distributedsensorframework.core.MeasurementActivity;
import masterthesis.cc.distributedsensorframework.core.SensorMaster;
import masterthesis.cc.distributedsensorframework.core.db.Measurements;

/**
 * Created by Christoph Classen on 11.05.16.
 */
public class MyMeasurementActivity extends MeasurementActivity implements View.OnClickListener {

    private static final String  TAG              = "MyMeasurement::Activity ";

    private boolean              mIsColorSelected = false;
    private Mat                  mRgba;
    private Scalar               mBlobColorRgba;
    private Scalar               mBlobColorHsv;
    private Mat                  mSpectrum;
    private Size                 SPECTRUM_SIZE;
    private Scalar               CONTOUR_COLOR;
    private int                  mBlobCount;
    private Button               mBtnCapture;
    private Button               mBtnCaptureEntropie;
    private Button               mBtnExport;
    private Button               mBtnNotice;
    private TextView            mTxtNotice;

    private String               devicename;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  ((MyProcessor)mProcessor).setMinContourArea(3.0);
        /*this.mOpenCvCameraView.setColorEffect(Camera.Parameters.EFFECT_NONE);
        mOpenCvCameraView.setColorEffect(Camera.Parameters.EFFECT_NONE);
        mOpenCvCameraView.setFocus(Camera.Parameters.FOCUS_MODE_FIXED);
        mOpenCvCameraView.setLockWhiteBalance(true);
        mOpenCvCameraView.setZoom(2);*/
        //Log.e("MYmeasuerment", "-##########################started###########################");
        LOG.info(TAG + " started");

        devicename = Build.MODEL + " ("+Build.PRODUCT+") " + Build.ID +"|"+ Build.SERIAL;


        //Contentview der Superklasse überschreiben
        setContentView(R.layout.measurement_activity);

        mBtnCapture = (Button) findViewById(R.id.btn_capture);
        mBtnCapture.setOnTouchListener(this);

        mBtnCaptureEntropie = (Button) findViewById(R.id.btn_capture_entropie);
        mBtnCaptureEntropie.setOnTouchListener(this);

        mBtnNotice = (Button) findViewById(R.id.btn_savenotice);
        mBtnNotice.setOnTouchListener(this);

        mBtnExport = (Button) findViewById(R.id.btn_export);
        mBtnExport.setOnTouchListener(this);


        mTxtNotice = (TextView) findViewById(R.id.text_notice);


        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.OpenCvCameraView);
        mOpenCvCameraView.setCvCameraViewListener(this);


        startService();



    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        stopService();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            LOG.debug(TAG +"Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        } else {
            LOG.debug(TAG +  "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }



    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mProcessor = new MyProcessor();
        mSpectrum = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
        SPECTRUM_SIZE = new Size(200, 64);
        CONTOUR_COLOR = new Scalar(0,255,0,255);
        mBlobCount=0;
    }

    public void onCameraViewStopped() {
        mRgba.release();
        stopService();
    }


    /**
     * OnTouchListener für die Interaktion mit der CameraView
     * @param v
     * @param event
     * @return
     */
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()){
            case R.id.OpenCvCameraView:
                //Klick auf die CameraView setzt einen Farbraum als Blobwert
                handlePreviewTab(event);
                break;
        }

        return false; // don't need subsequent touch events
    }


    /**
     * OnClick Listner für Butten Clicks
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_capture:
                //Capturebutton speichert die Anzahl der aktuellen blobs
                this.saveBlobCount(this.mBlobCount);
                this.saveMatToFile();
                break;
            case R.id.btn_capture_entropie:
                //Capturebutton speichert die Entropie der aktuellen blobs
                this.saveBlobEntropie(this.mBlobCount);
                break;
            case R.id.btn_savenotice:
                Measurements m = new Measurements(0,new Date(),0,mTxtNotice.getText().toString(),devicename);
                mSaving.saveValue(m);
                Toast.makeText(this, "Notiz gespeichert:"+ m.toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_export:

                File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                mSaving.exportCSV(f.getAbsolutePath() + "/sfw_measuerments.csv");
                Toast.makeText(this, "Datenbank nach Downloads/swf_measuerments.csv Exportiert", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_rssi:
                RssiSensor rs = new RssiSensor(getApplicationContext());

                Measurements meas = new Measurements(0, new Date(),rs.getSensorId(),rs.getCurrentValue()[0] +"", devicename);
                LOG.info(TAG + "RssiValue: "+ meas.getValue());
                mSaving.saveValue(meas);
                break;
        }
    }


    /**
     * Wertet das TouchEvent auf die Cameraview aus. An der Berührten Koordinate wird ein 8*8 großes
     * Rechteck verwendet, um einen Durchschnittswert der dortigen Farbe als Blobfarbe zu bestimmen.
     * @param event TouchEvent
     */
    private void handlePreviewTab(MotionEvent event){
        int cols = mRgba.cols();
        int rows = mRgba.rows();

        int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;
        int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2;

        int x = (int)event.getX() - xOffset;
        int y = (int) event.getY() - yOffset;

        LOG.info(TAG + " Touch image coordinates: (" + x + ", " + y + ")");

        if ((x < 0) || (y < 0) || (x > cols) || (y > rows)){
            //alles was außerhalb der View liegt, ignorieren
        }else {

            Rect touchedRect = new Rect();

            touchedRect.x = (x > 4) ? x - 4 : 0;
            touchedRect.y = (y > 4) ? y - 4 : 0;

            touchedRect.width = (x + 4 < cols) ? x + 4 - touchedRect.x : cols - touchedRect.x;
            touchedRect.height = (y + 4 < rows) ? y + 4 - touchedRect.y : rows - touchedRect.y;

            Mat touchedRegionRgba = mRgba.submat(touchedRect);

            Mat touchedRegionHsv = new Mat();
            Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

            // Calculate average color of touched region
            mBlobColorHsv = Core.sumElems(touchedRegionHsv);
            int pointCount = touchedRect.width * touchedRect.height;
            for (int i = 0; i < mBlobColorHsv.val.length; i++)
                mBlobColorHsv.val[i] /= pointCount;

            mBlobColorRgba = ((MyProcessor)mProcessor).converScalarHsv2Rgba(mBlobColorHsv);

            LOG.debug(TAG + " Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                    ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");

            ((MyProcessor) mProcessor).setHsvColor(mBlobColorHsv);
            Imgproc.resize(((MyProcessor) mProcessor).getSpectrum(), mSpectrum, SPECTRUM_SIZE);
            mIsColorSelected = true;
            touchedRegionRgba.release();
            touchedRegionHsv.release();
        }
    }


    /**
     * Callback, welches die eingehenden Kameraframes bearbeitet, sobald eine Blobfarbe ausgewählt ist.
     * In diesem Fall wird in dem Frame mit Hilfe des MyProcessors die Blobs der gewählten Farbe mit einer Kontur
     * versehen. Zudem wird im Oberen linken Framerand die gewählte Blob-Farbe und das zugehörige Spektrum eingeblendet
     * @param inputFrame einzelner Kameraframe
     * @return Mat, welches im preview angezeigt wird
     */
    public Mat onCameraFrame(Mat inputFrame) {
        inputFrame.copyTo(mRgba);

     /*   if (mIsColorSelected) {
            ((MyProcessor)mProcessor).process(inputFrame);
            List<MatOfPoint> contours = ((MyProcessor)mProcessor).getContours();
            LOG.info(TAG + "Contours count: " + contours.size());
            mBlobCount = contours.size();
            Imgproc.drawContours(inputFrame, contours, -1, CONTOUR_COLOR);

            //ausgewählte Farbe einblenden
            Mat colorLabel = inputFrame.submat(4, 68, 4, 68);
            colorLabel.setTo(mBlobColorRgba);

            //zugehöriges Farbsprektrum ebenfalls einblenden
            Mat spectrumLabel = inputFrame.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols());
            mSpectrum.copyTo(spectrumLabel);
        }*/

        return inputFrame;
    }




    private void saveMatToFile(){

        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mRgba, bmp);
        } catch (CvException e) {
            LOG.debug("Bitmap konnte nicht aus Mat erzeugt werden: ", e.getMessage());
        }

        FileOutputStream out = null;


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

        String filename =  "cameraframe_"+dateFormat.format(new Date())+".png";


        File sd = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) , "/sensorframework");
        boolean success = true;
        if (!sd.exists()) {
            success = sd.mkdir();
        }
        if (success) {
            File dest = new File(sd, filename);

            try {
                out = new FileOutputStream(dest);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored

            } catch (Exception e) {
                e.printStackTrace();
                LOG.debug(TAG + e.getMessage());
            } finally {
                try {
                    if (out != null) {
                        out.close();
                        //Log.d(TAG, "OK!!");
                        LOG.debug("Cameraframe gespeichert!");
                    }
                } catch (IOException e) {
                    //Log.d(TAG, e.getMessage() + "Error");
                    LOG.error("Fehler beim Speichern des Bildes:"+ e.getMessage());
                    e.printStackTrace();
                }
            }
        }

    }





















    /**
     * Speichert die Anzahl der Aktuellen Blobs in der Datenbank
     * @param anzahl Blobanzahl
     */
    private void saveBlobCount(int anzahl){

        //final TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //String devicename =tm.getDeviceId()+" ist deviceID:"+ Build.MODEL + " ("+Build.PRODUCT+") " + Build.ID +"|"+ Build.SERIAL;

        Measurements messung = new Measurements(0,new Date(),55,anzahl+"",devicename);
        Toast.makeText(this, messung.toString(), Toast.LENGTH_LONG).show();
        mSaving.saveValue(messung);
    }




    /**
     * Speichert die Anzahl der Aktuellen Blobs in der Datenbank
     * @param anzahl Blobanzahl
     */
    private void saveBlobEntropie(int anzahl){

        //final TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //String devicename =tm.getDeviceId()+" ist deviceID:"+ Build.MODEL + " ("+Build.PRODUCT+") " + Build.ID +"|"+ Build.SERIAL;

        Measurements messung = new Measurements(0,new Date(),55,anzahl+"",devicename);
        Toast.makeText(this, messung.toString(), Toast.LENGTH_LONG).show();
        mSaving.saveValue(messung);
    }





    public void startService(){
        Log.e("Mymeasurmentactivity", System.currentTimeMillis() + ": start service");
        Intent i = new Intent(getApplicationContext(), SensorMaster.class);
        startService(i);
    }

    public void stopService(){
        Intent i = new Intent(getApplicationContext(), SensorMaster.class);
        stopService(i);
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
