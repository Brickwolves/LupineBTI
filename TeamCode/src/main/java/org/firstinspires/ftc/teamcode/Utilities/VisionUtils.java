package org.firstinspires.ftc.teamcode.Utilities;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.contourArea;

public class VisionUtils {

    /**
     * Static constants related to the camera's specifications
     * The VisionUtils functions require these constants for its calculations
     */
    public static double IMG_WIDTH = 432;
    public static double IMG_HEIGHT = 240;
    public static final double X_FOV = 45.25;
    public static final double Y_FOV = 34.75;
    // WORKING IN INCHES
    public static final double CAMERA_HEIGHT = 10.5;

    // Note: All measurements in CM

    /**
     * an enum for representing the aspects of a rectangle
     */
    public enum RECT_OPTION {
        AREA, WIDTH, HEIGHT, X, Y
    }

    /**
     * an enum for representing the camera's X and Y axes
     */
    public enum AXES {
        X, Y
    }

    /**
     * Converts a width of pixels into degrees
     * @param pixels - a double representing the number of pixels to convert
     * @param axe - an enum of type AXES. Specifies whether we're converting pixels
     *              along the Y or X axe.
     * @return  a double representing the distance of pixels as a number of degrees spanning the
     *          camera lens
     */

    public static double pixels2Degrees(double pixels, AXES axe){
        return (axe == AXES.X) ? pixels * (X_FOV / IMG_WIDTH) : pixels * (Y_FOV / IMG_HEIGHT);
    }

    /**
     * retrieves the index of the rectangle with the maximum of the given option
     * e.g. retrieves the index of the rectangle with the greatest WIDTH, or AREA, etc
     * @param rects - a List<Rect> representing the list of rectangles to choose from
     * @param option - a RECT_OPTION representing the option to sort by
     * @return an int representing the index of the rectangle with the greatest option
     */
    public static int getMaxIndex(List<Rect> rects, RECT_OPTION option){
        int alpha_index = 0;
        double max = Integer.MIN_VALUE;
        double cur = 0;
        for (int i=0; i < rects.size(); i++){

            switch (option){
                case X:
                    cur = rects.get(i).x;
                    break;

                case Y:
                    cur = rects.get(i).y;
                    break;

                case WIDTH:
                    cur = rects.get(i).width;
                    break;

                case HEIGHT:
                    cur = rects.get(i).height;
                    break;

                case AREA:
                    cur = rects.get(i).width * rects.get(i).height;
                    break;

            }
            if (cur > max) {
                max = cur;
                alpha_index = i;
            }
        }
        return alpha_index;
    }

    /**
     * retrieves the index of the rectangle with the minimum of the given option
     * e.g. retrieves the index of the rectangle with the least WIDTH, or AREA, etc
     * @param rects - a List<Rect> representing the list of rectangles to choose from
     * @param option - a RECT_OPTION representing the option to sort by
     * @return an int representing the index of the rectangle with the least option
     */
    public static int getMinIndex(List<Rect> rects, RECT_OPTION option){
        int beta_index = 0;
        double min = Integer.MAX_VALUE;
        double cur = 0;
        for (int i=0; i < rects.size(); i++){

            switch (option){
                case X:
                    cur = rects.get(i).x;
                    break;

                case Y:
                    cur = rects.get(i).y;
                    break;

                case WIDTH:
                    cur = rects.get(i).width;
                    break;

                case HEIGHT:
                    cur = rects.get(i).height;
                    break;

                case AREA:
                    cur = rects.get(i).width * rects.get(i).height;
                    break;
            }
            if (cur < min) {
                min = cur;
                beta_index = i;
            }
        }
        return beta_index;
    }


    /**
     * Sorts a list of rectangles according to the given RECT_OPTION in ascending order
     * @param n - an integer, the first n rectangles to return in ascending order
     * @param option - a RECT_OPTION to sort by. e.g. AREA, WIDTH, HEIGHT
     * @param rects - a list of Rect rectangles to sort through
     * @return a List<Rect>, a list of n rectangles sorted in ascending order by RECT_OPTION
     */
    public static List<Rect> sortRectsByMinOption(int n, RECT_OPTION option, List<Rect> rects){
        List<Rect> sorted_rects = new ArrayList<>();
        for (int j=0; j < n; j++){
            int beta_index = getMinIndex(rects, option);
            sorted_rects.add(rects.get(beta_index));

            rects.remove(beta_index);
            if (rects.size() == 0) break;
        }
        return sorted_rects;
    }

    /**
     * Sorts a list of rectangles according to the given RECT_OPTION in descending order
     * @param n - an integer, the first n rectangles to return in descending order
     * @param option - a RECT_OPTION to sort by. e.g. AREA, WIDTH, HEIGHT
     * @param rects - a list of Rect rectangles to sort through
     * @return a List<Rect>, a list of n rectangles sorted in descending order by RECT_OPTION
     */
    public static List<Rect> sortRectsByMaxOption(int n, RECT_OPTION option, List<Rect> rects){
        List<Rect> sorted_rects = new ArrayList<>();
        for (int j=0; j < n; j++){
            int alpha_i = getMaxIndex(rects, option);
            sorted_rects.add(rects.get(alpha_i));

            rects.remove(alpha_i);
            if (rects.size() == 0) break;
        }
        return sorted_rects;
    }

    /**
     * Finds the index of the contour that is farthest to the
     * left on the screen, relative to the camera
     * @param contours - List<MatOfPoint>, a list of contours
     * @return an integer specifying the index of the left most contour in a List of contours
     */
    public static int findLeftMostContourIndex(List<MatOfPoint> contours){
        int index = 0;
        double minX = Integer.MAX_VALUE;
        for (int i=0; i < contours.size(); i++){
            MatOfPoint cnt = contours.get(i);
            double x = boundingRect(cnt).x;
            if (x < minX) {
                minX = x;
                index = i;
            }
        }
        return index;
    }

    /**
     * Sorts n number of contours by x-coordinate in ascending order
     * @param n -   an integer, the number of contours to return, must be less than the size of the
     *              contour list
     * @param contours - List<MatOfPoint>, a list of contours
     * @return List<MatOfPoint>, a list of n contours sorted by x-coordinate in ascending order
     */
    public static List<MatOfPoint> findNLeftMostContours(int n, List<MatOfPoint> contours){
        List<MatOfPoint> widest_contours = new ArrayList<>();
        for (int j=0; j < n; j++){
            int largest_index = findLeftMostContourIndex(contours);
            widest_contours.add(contours.get(largest_index));

            contours.remove(largest_index);
            if (contours.size() == 0) break;
        }

        for (MatOfPoint cnt : contours){
            cnt.release();
        }

        return widest_contours;
    }

    /**
     * Finds the index of widest contour
     * @param contours - List<MatOfPoint>, a list of contours
     * @return the index of the widest contour in a list of contours
     */
    public static int findWidestContourIndex(List<MatOfPoint> contours){
        int index = 0;
        double maxWidth = 0;
        for (int i=0; i < contours.size(); i++){
            MatOfPoint cnt = contours.get(i);
            double width = boundingRect(cnt).width;
            if (width > maxWidth) {
                maxWidth = width;
                index = i;
            }
        }
        return index;
    }

    /**
     * Sorts contours by width in descending order and returns the n first contours
     * @param n - an int, the number of contours to return
     * @param contours - List<MatOfPoint>, a list of contours
     * @return List<MatOfPoint>, a list of the n widest contours
     */
    public static List<MatOfPoint> findNWidestContours(int n, List<MatOfPoint> contours){
        List<MatOfPoint> widest_contours = new ArrayList<>();
        for (int j=0; j < n; j++){
            int largest_index = findWidestContourIndex(contours);
            widest_contours.add(contours.get(largest_index));

            contours.remove(largest_index);
            if (contours.size() == 0) break;
        }

        for (MatOfPoint cnt : contours){
            cnt.release();
        }

        return widest_contours;
    }

    /**
     * Sorts contours by area in descending order and returns the n first contours
     * @param contours - List<MatOfPoint>, a list of contours
     * @return int, the index of the contour with the greatest area
     */
    public static int findLargestContourIndex(List<MatOfPoint> contours) {
        int index = 0;
        double maxArea = 0;
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint cnt = contours.get(i);
            double area = contourArea(cnt);
            if (area > maxArea) {
                maxArea = area;
                index = i;
            }
        }
        return index;
    }


    /**
     * Sorts contours by area in descending order and returns the n first contours
     * @param n - int, the number of contours to return
     * @param contours - List<MatOfPoint>, a list of contours
     * @return List<MatOfPoint>, a list of the n largest contours
     */
    public static List<MatOfPoint> findNLargestContours(int n, List<MatOfPoint> contours) {
        List<MatOfPoint> new_contours = new ArrayList<>();

        for (int j = 0; j < n; j++) {
            int largest_index = findLargestContourIndex(contours);
            new_contours.add(contours.get(largest_index));

            contours.remove(largest_index);
            if (contours.size() == 0) break;
        }

        for (MatOfPoint cnt : contours){
            cnt.release();
        }

        return new_contours;
    }
}