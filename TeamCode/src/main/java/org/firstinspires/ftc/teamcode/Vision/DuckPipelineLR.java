package org.firstinspires.ftc.teamcode.Vision;

import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.BACK_MAX_CB;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.BACK_MAX_CR;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.BACK_MAX_Y;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.BACK_MIN_CB;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.BACK_MIN_CR;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.BACK_MIN_Y;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.DEBUG_MODE;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.currentDuckPos;
import static org.firstinspires.ftc.teamcode.Utilities.VisionUtils.BACK_CAMERA_HEIGHT;
import static org.firstinspires.ftc.teamcode.Utilities.VisionUtils.BACK_IMG_HEIGHT;
import static org.firstinspires.ftc.teamcode.Utilities.VisionUtils.BACK_IMG_WIDTH;

import static org.firstinspires.ftc.teamcode.Utilities.VisionUtils.pixels2Degrees1;
import static org.firstinspires.ftc.teamcode.Utilities.VisionUtils.sortRectsByMaxOption;
import static org.opencv.core.Core.inRange;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;
import static org.opencv.imgproc.Imgproc.COLOR_RGB2YCrCb;
import static org.opencv.imgproc.Imgproc.RETR_TREE;

import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.drawContours;
import static org.opencv.imgproc.Imgproc.findContours;
import static org.opencv.imgproc.Imgproc.rectangle;

import static java.lang.Math.abs;
import static java.lang.Math.log;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision;
import org.firstinspires.ftc.teamcode.Utilities.VisionUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class DuckPipelineLR extends OpenCvPipeline {
    /**
     * this duck pipeline is for determining if the duck is on the L or R barcode at the start of auto
     * uses BACK camera
     */
    private Mat output = new Mat();
    private Mat modified = new Mat();
    private List<MatOfPoint> contours = new ArrayList<>(); //create empty arraylist to store contour points
    private Mat hierarchy = new Mat();
    private Scalar green = new Scalar(0, 255, 0); //this pipeline outlines in GREEN


    public static boolean isDuckFound;


    @Override
    public Mat processFrame(Mat input) {
        BACK_IMG_HEIGHT = input.rows(); //input image height
        BACK_IMG_HEIGHT = input.cols(); //input image width
        input.copyTo(output); //copying input image to output
        cvtColor(input, modified, COLOR_RGB2YCrCb); //call to "convert color" method

        Scalar MAX_THRESH = new Scalar(BACK_MAX_Y, BACK_MAX_CR, BACK_MAX_CB);
        Scalar MIN_THRESH = new Scalar(BACK_MIN_Y, BACK_MIN_CR, BACK_MIN_CB);
        inRange(modified, MIN_THRESH, MAX_THRESH, modified); //threshold image

        contours = new ArrayList<>();
        hierarchy = new Mat();
        findContours(modified, contours, hierarchy, RETR_TREE, CHAIN_APPROX_SIMPLE); //populates contours and hierarchy lists

        List<Rect> rects = new ArrayList<>();

        for (int i = 0; i < contours.size(); i++){

            Rect rect = boundingRect(contours.get(i));
            rects.add(rect);
            /*
             iterates thru each index in the contour arraylist
             adds that contour to the rectangle arraylist
             then a rectangle is drawn around the contour (in this case the duck)
            */
        }

        if (rects.size() < 1){
            isDuckFound = false; //if the camera does not see the duck, the rest of the code will not run
            if (DEBUG_MODE){
                return modified;
            }
            return output;
        }

        isDuckFound = true;

        /**
        looks for the biggest rectangle in the frame (which will be the duck)
         sorts by area
         */
        List<Rect> biggestRect = sortRectsByMaxOption(1, VisionUtils.RECT_OPTION.AREA, rects);
        Rect duckRect = biggestRect.get(0);
        rectangle(output, duckRect, green, 3); //draws a rectangle around the duck


        /**if statement that checks if the duck is on the left barcode (left half of the frame)
         * or right barcode (right half of the frame)
         * sets currentDuckPos equal to whichever is true
         */
        if (duckRect.x < (BACK_IMG_WIDTH - 1) / 2){
            currentDuckPos = Dash_Vision.DuckPosition.LEFT_BARCODE;
        } else if (duckRect.x > (BACK_IMG_WIDTH - 1) / 2){
            currentDuckPos = Dash_Vision.DuckPosition.RIGHT_BARCODE;
        }


        if (DEBUG_MODE){
            return modified;
        }

        return output;


    }

    public Dash_Vision.DuckPosition detectDuckBarcode(){
        return currentDuckPos;
    }
}
