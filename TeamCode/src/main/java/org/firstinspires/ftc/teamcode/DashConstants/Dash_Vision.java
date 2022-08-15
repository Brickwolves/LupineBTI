package org.firstinspires.ftc.teamcode.DashConstants;
import com.acmerobotics.dashboard.config.Config;

import org.opencv.core.Scalar;

@Config
public class Dash_Vision {

    public static boolean isFrontCamTuning = true;
    //(for FTC dashboard) if front camera is being tuned, start streaming the front camera
    //else the back cam should start streaming

    public static double FRONT_MAX_Y = 255; //hardcoded YCrCb values from dashboard
    public static double FRONT_MAX_CR = 170;
    public static double FRONT_MAX_CB = 65;

    public static double BACK_MAX_Y = 255;
    public static double BACK_MAX_CR = 170;
    public static double BACK_MAX_CB = 65;

    public static double FRONT_MIN_Y = 0.0;
    public static double FRONT_MIN_CR = 135;
    public static double FRONT_MIN_CB = 0.0;

    public static double BACK_MIN_Y = 0.0;
    public static double BACK_MIN_CR = 135;
    public static double BACK_MIN_CB = 0.0;

    public static boolean DEBUG_MODE = false;

    public static double FRONT_CAMERA_OFFSET = 0;
    public static double BACK_CAMERA_OFFSET = 0;

    public static double DEGREE_RANGE = 100;


    public static DuckPosition currentDuckPos = DuckPosition.LEFT_BARCODE;

    public enum DuckPosition{
        LEFT_BARCODE, RIGHT_BARCODE
    }





}