package org.firstinspires.ftc.teamcode.Vision;

import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.DEBUG_MODE;

import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.DEGREE_RANGE;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.FRONT_MAX_CB;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.FRONT_MAX_CR;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.FRONT_MAX_Y;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.FRONT_MIN_CB;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.FRONT_MIN_CR;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.FRONT_MIN_Y;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.currentDuckPos;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.isFrontCamTuning;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.isActive;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.VisionUtils.FRONT_CAMERA_HEIGHT;
import static org.firstinspires.ftc.teamcode.Utilities.VisionUtils.FRONT_IMG_HEIGHT;
import static org.firstinspires.ftc.teamcode.Utilities.VisionUtils.FRONT_IMG_WIDTH;
import static org.firstinspires.ftc.teamcode.Utilities.VisionUtils.pixels2Degrees2;
import static org.firstinspires.ftc.teamcode.Utilities.VisionUtils.sortRectsByMaxOption;
import static org.firstinspires.ftc.teamcode.Vision.DuckPipelineLR.isDuckFound;
import static org.opencv.core.Core.inRange;

import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.FRONT_CAMERA_OFFSET;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;
import static org.opencv.imgproc.Imgproc.COLOR_RGB2YCrCb;
import static org.opencv.imgproc.Imgproc.RETR_TREE;
import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.findContours;
import static org.opencv.imgproc.Imgproc.rectangle;
import static java.lang.Math.abs;
import static java.lang.Math.log;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.Cameras;
import org.firstinspires.ftc.teamcode.Utilities.VisionUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class DuckPipelineDetect extends OpenCvPipeline {
    /**
     * this duck pipeline is for detecting ducks whenever
     * uses FRONT camera
     */
    private Mat output = new Mat();
    private Mat modified = new Mat();
    private List<MatOfPoint> contours = new ArrayList<>(); //create empty arraylist to store contour points
    private Mat hierarchy = new Mat();
    private Scalar red = new Scalar(255, 0, 0); //this pipeline outlines in RED

    public double pixelsOfDep;
    public double degreesOfDepression;
    public double distanceToDuck2;
    public double pixMid2Mid2;
    public double disMid2Mid2;

    public static boolean isDuckFound;



    @Override
    public Mat processFrame(Mat input) {
        FRONT_IMG_HEIGHT = input.rows(); //input image height
        FRONT_IMG_HEIGHT = input.cols(); //input image width
        input.copyTo(output); //copying input image to output
        cvtColor(input, modified, COLOR_RGB2YCrCb); //call to "convert color" method

        Scalar MAX_THRESH = new Scalar(FRONT_MAX_Y, FRONT_MAX_CR, FRONT_MAX_CB);
        Scalar MIN_THRESH = new Scalar(FRONT_MIN_Y, FRONT_MIN_CR, FRONT_MIN_CB);
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
        rectangle(output, duckRect, red, 3); //draws a rectangle around the duck

        //finds the angle of depression by finding the distance from midpoint of frame to the bottom width of the duckRect
        //finds the pixels first and then converts to degrees to find the angle
        pixelsOfDep = (duckRect.height + duckRect.y) - ((FRONT_IMG_HEIGHT - 1)/2.0);
        degreesOfDepression = pixels2Degrees2(pixelsOfDep, VisionUtils.AXES.Y);
        double radiansOfDepression = abs(toRadians(degreesOfDepression));
        distanceToDuck2 = FRONT_CAMERA_HEIGHT / Math.tan(radiansOfDepression); //calculates distance to the duck
        distanceToDuck2 = specialSauce(distanceToDuck2);

        pixMid2Mid2 = ((FRONT_IMG_WIDTH - 1)/2.0) - (duckRect.x + (duckRect.width/2.0));
        disMid2Mid2 = pixels2Degrees2(pixMid2Mid2, VisionUtils.AXES.X);
        //calculates distance from midpoint of frame (horizontal) to midpoint of duckRect (horizontal)


        if (DEBUG_MODE){
            return modified;
        }

        return output;


    }


    /**
     * Don't ask
     * @param distance - double, distance to the duck
     * @return eh
     */
    public double specialSauce(double distance){
        double quantum_flux_coefficient_of_the_universe = 0.3 * sqrt(50*distance);
        double bob = 10*log(3*distance);
        double ave = (quantum_flux_coefficient_of_the_universe + bob) / 2.0;
        return ave;
    }


    //methods that get how much farther the robot has to drive/turn to reach the duck
    public double getDistanceToDuck2(){
        return distanceToDuck2;
    }

    public double degreeError2Duck(){
        return disMid2Mid2;
    }


}
