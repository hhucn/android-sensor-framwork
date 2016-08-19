package masterthesis.cc.distributedsensorframework.core;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import masterthesis.cc.distributedsensorframework.R;
import masterthesis.cc.distributedsensorframework.customImpl.MyProcessor;

public class PhotoPostProd extends ActionBarActivity implements View.OnTouchListener {

    private final int SELECT_PHOTO = 1;
    private ImageView imageView;
    private Mat                  mRgba;
    private Scalar mBlobColorHsv;
    private Scalar               mBlobColorRgba;
    private MyProcessor mProcessor;
    private Mat                  mSpectrum;
    private Size SPECTRUM_SIZE;
    private boolean              mIsColorSelected = false;
    private Scalar               CONTOUR_COLOR;
    private Bitmap mBitmap;
    protected Logger LOG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_post_prod);
        LOG = LoggerFactory.getLogger(MeasurementActivity.class);

        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnTouchListener(this);
        Button pickImage = (Button) findViewById(R.id.btn_pick);
        Button refreshImage = (Button) findViewById(R.id.btn_refresh);
        refreshImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshImg();
            }
        });


        pickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });


        mRgba = new Mat(imageView.getHeight(), imageView.getWidth(), CvType.CV_8UC4);
        mProcessor = new MyProcessor();
        mSpectrum = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
        SPECTRUM_SIZE = new Size(200, 64);
        CONTOUR_COLOR = new Scalar(0,255,0,255);
    }


    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            LOG.debug("Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mLoaderCallback); //3.1.0
        } else {
            LOG.debug("OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);
                        mBitmap = selectedImage.copy(Bitmap.Config.ARGB_8888,false);
                        LOG.debug("lese nur matrix ein");
                        Utils.bitmapToMat(selectedImage, mRgba);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public boolean onTouch (View v, MotionEvent e) {
        int cols = mRgba.cols();
        int rows = mRgba.rows();
        int x =(int)  ((cols / (imageView.getWidth () * 1.0)) * e.getX());
        int y = (int) ((rows / (imageView.getHeight()* 1.0)) * e.getY());

        if ((x < 0) || (y < 0) || (x > cols) || (y > rows)){
            //alles was außerhalb der View liegt, ignorieren
        }else {

            Rect touchedRect = new Rect();

            touchedRect.x = (x > 4) ? x - 4 : 0;
            touchedRect.y = (y > 4) ? y - 4 : 0;
            LOG.debug("touched Rect: "+ touchedRect.toString());

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

            mBlobColorRgba = ((MyProcessor) mProcessor).converScalarHsv2Rgba(mBlobColorHsv);

            LOG.debug(" Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                       ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");

            ((MyProcessor) mProcessor).setHsvColor(mBlobColorHsv);
            Imgproc.resize(((MyProcessor) mProcessor).getSpectrum(), mSpectrum, SPECTRUM_SIZE);
            mIsColorSelected = true;
            touchedRegionRgba.release();
            touchedRegionHsv.release();
        }

        refreshImg();
        return false;
    }


    private void refreshImg(){
        LOG.debug("refresh img");

        if (mIsColorSelected) {
            ((MyProcessor)mProcessor).process(mRgba);
            List<MatOfPoint> contours = ((MyProcessor)mProcessor).getContours();
            LOG.info("Contours count: " + contours.size());

            Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);

            //ausgewählte Farbe einblenden
            Mat colorLabel = mRgba.submat(4, 68, 4, 68);
            colorLabel.setTo(mBlobColorRgba);

            //zugehöriges Farbsprektrum ebenfalls einblenden
            Mat spectrumLabel = mRgba.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols());
            mSpectrum.copyTo(spectrumLabel);
        }
        Utils.matToBitmap(mRgba,mBitmap);
        imageView.setImageBitmap(mBitmap);
    }


    protected BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    LOG.info("OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

}
