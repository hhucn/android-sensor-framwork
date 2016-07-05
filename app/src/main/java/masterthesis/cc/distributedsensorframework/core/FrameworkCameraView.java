package masterthesis.cc.distributedsensorframework.core;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;

import org.opencv.android.JavaCameraView;

/**
 * Created by luke on 30.06.16.
 */
public class FrameworkCameraView extends JavaCameraView {




    public FrameworkCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameworkCameraView(Context context, int cameraId) {
        super(context, cameraId);
    }


     public void setZoom(int zoomlevel){
         Camera.Parameters param = mCamera.getParameters();
         param.setZoom(zoomlevel);
         mCamera.setParameters(param);
     }

    public int getZoom(){
        Camera.Parameters param = mCamera.getParameters();
        return param.getZoom();
    }




    public void setFocus(String focus){
        Camera.Parameters param = mCamera.getParameters();
        param.setFocusMode(focus);
        mCamera.setParameters(param);
    }

    public String getFocus(){
        Camera.Parameters param = mCamera.getParameters();
        return param.getFocusMode();
    }





    public void setColorEffect(String colorEffect ){
        Camera.Parameters param = mCamera.getParameters();
        param.setColorEffect(colorEffect);
        mCamera.setParameters(param);
    }

    public String getColorEffect(){
        Camera.Parameters param = mCamera.getParameters();
        return param.getColorEffect();
    }





    public void setExposureCompensation(int exposurecompensation ){
        Camera.Parameters param = mCamera.getParameters();
        param.setExposureCompensation(exposurecompensation);
        mCamera.setParameters(param);
    }

    public int getExposureCompensation(){
        Camera.Parameters param = mCamera.getParameters();
        return param.getExposureCompensation();

    }


    public int getMinExposureCompensation(){
        Camera.Parameters param = mCamera.getParameters();
        return param.getMinExposureCompensation();
    }

    public int getMaxExposureCompensation(){
        Camera.Parameters param = mCamera.getParameters();
        return param.getMaxExposureCompensation();
    }





    public void setLockWhiteBalance(boolean lockWhiteBalance ){
        Camera.Parameters param = mCamera.getParameters();
        param.setAutoWhiteBalanceLock(lockWhiteBalance);
        mCamera.setParameters(param);
    }

    public boolean getLockWhiteBalance(){
        Camera.Parameters param = mCamera.getParameters();
        return param.getAutoWhiteBalanceLock();
    }




    public void setWhiteBalance(String whiteBalance ){
        Camera.Parameters param = mCamera.getParameters();
        param.setWhiteBalance(whiteBalance);
        mCamera.setParameters(param);
    }

    public String getWhiteBalance(){
        Camera.Parameters param = mCamera.getParameters();
        return param.getWhiteBalance();

    }

    public void setKeyValue(String key, String value){
        Camera.Parameters param = mCamera.getParameters();
        param.set(key, value);
        mCamera.setParameters(param);
    }

    public void setKeyValue(String key, int value){
        Camera.Parameters param = mCamera.getParameters();
        param.set(key, value);
        mCamera.setParameters(param);
    }





}
