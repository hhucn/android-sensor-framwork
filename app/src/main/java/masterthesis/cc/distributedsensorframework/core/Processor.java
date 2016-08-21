package masterthesis.cc.distributedsensorframework.core;

import org.opencv.core.Mat;

/**
 * Diese Klasse ist eine minimale Basisklasse, die für den Jeweiligen Anwendungnsfall geerbt werden kann
 */
public class Processor {

    public Processor (){}

    public synchronized void prepareViewSize(int width, int height) {}

    public synchronized void prepareViewSize() {}

    /**
     * verarbeitet die eingehenden Kameraframes als Mat
     **/
    public synchronized Mat processMatrix(Mat inputPicture) {
        return inputPicture;
    }

    public void deliverTouchEvent(int x, int y) {}


}