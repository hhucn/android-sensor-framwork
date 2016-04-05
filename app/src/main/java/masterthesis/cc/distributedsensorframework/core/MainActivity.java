package masterthesis.cc.distributedsensorframework.core;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import masterthesis.cc.distributedsensorframework.R;


public class MainActivity extends ListActivity {
    private ArrayAdapter<String> adapter;
    private List<String> wordList;
    private SensorMaster service;
    private GraphView graph;
    private LineGraphSeries<DataPoint> series1;
    private LineGraphSeries<DataPoint> series2;


    private  boolean reg = false;

    private int x=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                wordList);

        setListAdapter(adapter);


    //   this.listAllSensors();
        this.startSensorMasterService();
        this.initGraph();
        this.doCameraStuff();
    }


    private void listAllSensors(){
        //Get a list of all availible sensors
        SensorManager mgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = mgr.getSensorList(Sensor.TYPE_ALL);
        Log.e("SensorsAv", "List of Available sensors:");
        for (Sensor sensor : sensors) {
            Log.e("SensorsAV", "" + sensor.getName() + ";"
                    //+ sensor.getStringType() + ";"
                    + sensor.getType() + ";" + sensor.getPower() + ";" );
        }
        Log.e("SensorsAv", "List END");
    }

    private void startSensorMasterService(){
        //Service starten
        Intent i = new Intent(this, SensorMaster.class);
        ArrayList<String> primSensors = new ArrayList<String>();
        primSensors.add("camera");
        i.putStringArrayListExtra("primarySensors", primSensors);
        ArrayList<String> sndSensors = new ArrayList<String>();
        sndSensors.add("light");
        i.putStringArrayListExtra("secondarySensors", sndSensors);
        this.startService(i);
    }

    private void initGraph(){
        //graph zum anzeigen der Werte initialisieren
        graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(1000.0);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxX(40.0);
        graph.getViewport().setMinX(0.0);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] { });
        series1.setColor(Color.RED);
        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] { });
        graph.addSeries(series1);
        graph.addSeries(series2);
        //  series.appendData(new DataPoint(11,3),true,40);
    }



    @Override
    protected void onResume() {
        super.onResume();
        Intent intent= new Intent(this, SensorMaster.class);
        bindService(intent, mConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }



    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            SensorMaster.MyBinder b = (SensorMaster.MyBinder) binder;
            service = b.getService();
            Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            Log.d("ServiceConnection", "service disconnected");
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d("ServiceConnection","service disconnected");
            service = null;
        }
    };




    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                if (service != null) {
                    List<String> l = service.getWordList();
               Toast.makeText(this, "Number of elements " + l.size(),
                      Toast.LENGTH_SHORT).show();
                    wordList.clear();
                    wordList.addAll(l);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.button2:
                mCamera.takePicture(null, null, mPicture);
             //   this.registerCallback();
                break;
            case R.id.button3:
                Log.d(TAG, "button3");
                 mCamera.takePicture(null, null, mPicture);
                break;
        }

    }





    public void registerCallback(){
        if (!reg) {
            service.registerCallback(this);
            reg = true;
        }else{
            service.registerCallback(null);
            reg = false;
        }
        Log.e("MainActivity", "Callback Registred: " + reg);
    }


    public void callback(ArrayList<String> words){
     ///   series.appendData(new DataPoint(x,Double.parseDouble(words.get(0))),true,400);
        x++;
        wordList.clear();
        wordList.addAll(words);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





























    /** A safe way to get an instance of the Camera object.
     * Now th */
    public static Camera getCameraInstance(boolean front){


        Camera c = null;
        if (front) {
            int cameraCount = 0;

            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount = Camera.getNumberOfCameras();
            Log.e(TAG, "Number of cameras: " + cameraCount);
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                Log.e(TAG, "CameraInfo: face"+ cameraInfo.facing);
               // if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                  if (cameraInfo.facing == 1) {
                        try {
                        Log.e(TAG, "Open camera: " + cameraInfo.facing);
                        c = Camera.open(camIdx); // attempt to get a Camera instance
                    } catch (Exception e) {
                        // Camera is not available (in use or does not exist)
                    }
                }
            }
        }else{
            try {
                c = Camera.open(); // attempt to get a Camera instance
            }
            catch (Exception e){
                // Camera is not available (in use or does not exist)
            }
        }
        return c; // returns null if camera is unavailable
    }



private static String TAG = "Camera";

    private Camera mCamera;
    private CameraPreview mPreview;

    public void doCameraStuff() {


        // Add a listener to the Capture button
    //    Button captureButton = (Button) findViewById(R.id.button_capture);
    //    captureButton.setOnClickListener(
     //           new View.OnClickListener() {
      //              @Override
       //             public void onClick(View v) {
                        // get an image from the camera
      //                  Log.d("Cam", "start taking picture");
                       // mCamera.takePicture(null, null, mPicture);
      //              }
       //         }
       // );

        // Create an instance of front Camera
        mCamera = getCameraInstance(true);

        Camera.Parameters cp = mCamera.getParameters();

        //Autoanpassungen unterdrücken
        cp.setExposureCompensation(0);
        cp.setAutoExposureLock(true);
        cp.setAutoWhiteBalanceLock(true);
        cp.setColorEffect(Camera.Parameters.EFFECT_NONE);

        mCamera.setParameters(cp);


        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }



    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //  savePictureAsFile(data);
            calculateBrightness(data);
        }
    };



    public void calculateBrightness(byte[] image){
        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);

        int r=0;
        int g=0;
        int b=0;
        for (int x =0; x<bmp.getWidth()-1;x++) {
            for (int y = 0; y < bmp.getHeight(); y++) {
              //  Log.i(TAG, "Helligkeit: " + Color.red(bmp.getPixel(x, y)) + ";" + Color.green(bmp.getPixel(x, y)) + ";" + Color.blue(bmp.getPixel(x, y)));
                r += Color.red(bmp.getPixel(x,y));
                g += Color.green(bmp.getPixel(x,y));
                b += Color.blue(bmp.getPixel(x,y));
            }
        }
        int pixcount = bmp.getWidth()*bmp.getHeight();

        double mr = r/pixcount;
        double mg = g/pixcount;
        double mb = b/pixcount;

        Log.d(TAG, "Durchschnittswert:" + mr + ";" + mg + ";" + mb + " -> " + (mr+mg+mb)/3);
        double d = (mr+mg+mb)*13.0718;
        Log.d(TAG, "Normalisiert: " + d);



        series1.appendData(new DataPoint(x, (mr+mg+mb)/3), true, 400);


        //Referenzwert
        if (service != null) {
            List<String> l = service.getWordList();
            wordList.clear();
            wordList.addAll(l);
            adapter.notifyDataSetChanged();
        }
        Log.d(TAG, "Lichtsensor:" + wordList.get(0));
        series2.appendData(new DataPoint(x, Double.parseDouble(wordList.get(0))*0.255), true, 400);

        x++;
    }


    public void savePictureAsFile(byte[] data){
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null){
            Log.d(TAG, "Error creating media file, check storage permissions: ");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Log.d(TAG, "Bild gespeichert");
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }





    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
            Log.d(TAG, "mediafile: "+ mediaFile.getAbsolutePath());
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
