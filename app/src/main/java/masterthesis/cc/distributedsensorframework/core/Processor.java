package masterthesis.cc.distributedsensorframework.core;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * This class is a controller for puzzle game.
 * It converts the image from Camera into the shuffled image
 */
public class Processor {

//    private int mGridSize;
//    private int mGridArea;
//    private int mGridEmptyIndex;

    private static final String TAG = "ImageProcessor";
    private static final Scalar GRID_EMPTY_COLOR = new Scalar(0x33, 0x33, 0x33, 0xFF);

//    private int[]   mIndexes;
//    private int[]   mTextWidths;
//    private int[]   mTextHeights;

    private Mat mRgba15;
    private Mat[] mCells15;
    private boolean mShowTileNumbers = true;



    public Processor (){}


    public Processor(final int gridSize) {
//        this.mGridSize = gridSize;
//        this.mGridArea = this.mGridSize * this.mGridSize;
//        this.mGridEmptyIndex = this.mGridArea - 1;
//
//
//        mTextWidths = new int[mGridArea];
//        mTextHeights = new int[mGridArea];
//
//        mIndexes = new int [mGridArea];
//
//        for (int i = 0; i < mGridArea; i++)
//            mIndexes[i] = i;
    }

//    /* this method is intended to make processor prepared for a new game */
//    public synchronized void prepareNewGame() {
//        do {
//            shuffle(mIndexes);
//        } while (!isPuzzleSolvable());
//    }

    /* This method is to make the processor know the size of the frames that
     * will be delivered via puzzleFrame.
     * If the frames will be different size - then the result is unpredictable
     */
    public synchronized void prepareViewSize(int width, int height) {
//        mRgba15 = new Mat(height, width, CvType.CV_8UC4);
//        mCells15 = new Mat[mGridArea];
//
//        for (int i = 0; i < mGridSize; i++) {
//            for (int j = 0; j < mGridSize; j++) {
//                int k = i * mGridSize + j;
//                mCells15[k] = mRgba15.submat(i * height / mGridSize,
//                        (i + 1) * height / mGridSize,
//                        j * width / mGridSize,
//                        (j + 1) * width / mGridSize);
//            }
//        }
//
//        for (int i = 0; i < mGridArea; i++) {
//            Size s = Imgproc.getTextSize(Integer.toString(i + 1), 3/* CV_FONT_HERSHEY_COMPLEX */, 1, 2, null);
//            mTextHeights[i] = (int) s.height;
//            mTextWidths[i] = (int) s.width;
//        }


    }

    public synchronized void prepareViewSize() {
    }

    /* this method to be called from the outside. it processes the frame and shuffles
     * the tiles as specified by mIndexes array
     */
    public synchronized Mat processMatrix(Mat inputPicture) {

     /*  Imgproc.cvtColor(inputPicture,inputPicture, Imgproc.COLOR_BGR2RGB);
        Scalar lowerb = new Scalar(0,0,100);
        Scalar upperb = new Scalar(80,80,255);
        Mat mask = new Mat();
        Core.inRange(inputPicture, lowerb, upperb, mask);*/
        return inputPicture;




     //   Imgproc.cvtColor(inputPicture,inputPicture,Imgproc.COLOR_BGR2HSV);

    //    Scalar lowerb = new Scalar(50,35,130);
     //   Scalar upperb = new Scalar(115,100,200);
//        Scalar lowerb = new Scalar(130,35,50);
 //       Scalar upperb = new Scalar(200,100,115);


//        for (lower, upper) in bounds:
 //       lower = np.array(lower, dtype ="uint8")
  //      upper = np.array(upper, dtype = "uint8")



        //mask= cv2.inRange(image, lower, upper)



        //output = cv2.bitwise_and(image, image, mask = mask)
     //XXX   Core.bitwise_and(inputPicture, mask,inputPicture);

      //  Core.bitwise_and(inputPicture,inputPicture,inputPicture, mask);
//Core.bitwise_not(inputPicture,inputPicture,mask);
      //  Imgproc.threshold(inputPicture, inputPicture, 3, 255, Imgproc.THRESH_BINARY);
        //ret, thresh = cv2.threshold(output, 3, 255, cv2.THRESH_BINARY)

    //  Core.bitwise_and(inputPicture,mask,inputPicture);


//        cv2.imshow("images", np.hstack([image, thresh]));








    //    return inputPicture;














//        Mat[] cells = new Mat[mGridArea];
//        int rows = inputPicture.rows();
//        int cols = inputPicture.cols();
//
//        rows = rows - rows%4;
//        cols = cols - cols%4;
//
//        for (int i = 0; i < mGridSize; i++) {
//            for (int j = 0; j < mGridSize; j++) {
//                int k = i * mGridSize + j;
//                cells[k] = inputPicture.submat(
//                        i * inputPicture.rows() / mGridSize,
//                        (i + 1) * inputPicture.rows() / mGridSize,
//                        j * inputPicture.cols() / mGridSize,
//                        (j + 1) * inputPicture.cols() / mGridSize);
//            }
//        }
//
//        rows = rows - rows%4;
//        cols = cols - cols%4;
//
//        // copy shuffled tiles
//        for (int i = 0; i < mGridArea; i++) {
//            int idx = mIndexes[i];
//            if (idx == mGridEmptyIndex)
//                mCells15[i].setTo(GRID_EMPTY_COLOR);
//            else {
//                cells[idx].copyTo(mCells15[i]);
//                if (mShowTileNumbers) {
//                    Imgproc.putText(mCells15[i], Integer.toString(1 + idx), new Point((cols / mGridSize - mTextWidths[idx]) / 2,
//                            (rows / mGridSize + mTextHeights[idx]) / 2), 3/* CV_FONT_HERSHEY_COMPLEX */, 1, new Scalar(255, 0, 0, 255), 2);
//                }
//            }
//        }
//
//        for (int i = 0; i < mGridArea; i++)
//            cells[i].release();
//
//        drawGrid(cols, rows, mRgba15);

//        return mRgba15;
    }

 //   public void toggleTileNumbers() {
//        mShowTileNumbers = !mShowTileNumbers;
 //   }

    public void deliverTouchEvent(int x, int y) {

//        Log.e("SEONSOR", "Touch Oouhh");
//        int rows = mRgba15.rows();
//        int cols = mRgba15.cols();
//
//        int row = (int) Math.floor(y * mGridSize / rows);
//        int col = (int) Math.floor(x * mGridSize / cols);
//
//        if (row < 0 || row >= mGridSize || col < 0 || col >= mGridSize) {
//            Log.e(TAG, "It is not expected to get touch event outside of picture");
//            return ;
//        }
//
//        int idx = row * mGridSize + col;
//        int idxtoswap = -1;
//
//        // left
//        if (idxtoswap < 0 && col > 0)
//            if (mIndexes[idx - 1] == mGridEmptyIndex)
//                idxtoswap = idx - 1;
//        // right
//        if (idxtoswap < 0 && col < mGridSize - 1)
//            if (mIndexes[idx + 1] == mGridEmptyIndex)
//                idxtoswap = idx + 1;
//        // top
//        if (idxtoswap < 0 && row > 0)
//            if (mIndexes[idx - mGridSize] == mGridEmptyIndex)
//                idxtoswap = idx - mGridSize;
//        // bottom
//        if (idxtoswap < 0 && row < mGridSize - 1)
//            if (mIndexes[idx + mGridSize] == mGridEmptyIndex)
//                idxtoswap = idx + mGridSize;
//
//        // swap
//        if (idxtoswap >= 0) {
//            synchronized (this) {
//                int touched = mIndexes[idx];
//                mIndexes[idx] = mIndexes[idxtoswap];
//                mIndexes[idxtoswap] = touched;
//            }
//        }
    }

//    private void drawGrid(int cols, int rows, Mat drawMat) {
//        for (int i = 1; i < mGridSize; i++) {
//            Imgproc.line(drawMat, new Point(0, i * rows / mGridSize), new Point(cols, i * rows / mGridSize), new Scalar(0, 255, 0, 255), 3);
//            Imgproc.line(drawMat, new Point(i * cols / mGridSize, 0), new Point(i * cols / mGridSize, rows), new Scalar(0, 255, 0, 255), 3);
//        }
//    }

//    private static void shuffle(int[] array) {
//        for (int i = array.length; i > 1; i--) {
//            int temp = array[i - 1];
//            int randIx = (int) (Math.random() * i);
//            array[i - 1] = array[randIx];
//            array[randIx] = temp;
//        }
//    }

    /**
     * The algorithm below is not correct for various configuration of the grid.
     * We need to fix it! Let us use the algorithm explained here:
     * http://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html
     *
     * @return <code>true</code> if the puzzle is solvable, 
     *   	<code>false</code> otherwise.
     */
//    private boolean isPuzzleSolvable() {
//
//        // this works for 4x4, but not for 3x3 or 5x5 (!)
//        int sum = 0;
//        for (int i = 0; i < mGridArea; i++) {
//            if (mIndexes[i] == mGridEmptyIndex)
//                sum += (i / mGridSize) + 1;
//            else {
//                int smaller = 0;
//                for (int j = i + 1; j < mGridArea; j++) {
//                    if (mIndexes[j] < mIndexes[i])
//                        smaller++;
//                }
//                sum += smaller;
//            }
//        }
//        return sum % 2 == 0;
//    }
}