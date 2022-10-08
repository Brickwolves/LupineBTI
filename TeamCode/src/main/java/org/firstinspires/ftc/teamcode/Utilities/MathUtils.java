package org.firstinspires.ftc.teamcode.Utilities;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.opencv.core.Point;

import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;
import static java.lang.StrictMath.floorMod;
import static org.firstinspires.ftc.teamcode.Utilities.MathUtils.angleMode.RADIANS;

public class MathUtils {

    /**
     * @param targetAngle
     * @param currentAngle
     * @return the closest relative target angle
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static double closestAngle(double targetAngle, double currentAngle) {
        double simpleTargetDelta = floorMod(Math.round((360 - targetAngle) + currentAngle), 360);
        double alternateTargetDelta = -1 * (360 - simpleTargetDelta);
        return StrictMath.abs(simpleTargetDelta) <= StrictMath.abs(alternateTargetDelta) ? currentAngle - simpleTargetDelta : currentAngle - alternateTargetDelta;
    }

    public static double closestAngle(double targetAngle, double currentAngle, boolean ticks) {
        double simpleTargetDelta = floorModDouble(Math.round((384.5 - targetAngle) + currentAngle), 384);
        double alternateTargetDelta = -1 * (384.5 - simpleTargetDelta);
        return StrictMath.abs(simpleTargetDelta) <= StrictMath.abs(alternateTargetDelta) ? currentAngle - simpleTargetDelta : currentAngle - alternateTargetDelta;
    }

    public static double floorModDouble(double dividend, int divisor){
        return floorMod(Math.round(dividend * 1e6), divisor) / 1e6;
    }

    public static Point unShift(Point sp, double shiftAngle){ //for betterStrafe in Mecanum
        double r = toRadians(shiftAngle);
        double x = sp.x * Math.sin(r) + sp.y * Math.cos(r);
        double y = sp.x * Math.cos(r) - sp.y * Math.sin(r);
        return new Point(x, y);
    }

    /**
     * @param x
     * @param a_min
     * @param a_max
     * @param b_min
     * @param b_max
     * @return x but mapped from [a_min, a_max] to [b_min, b_max]
     */
    public static double map(double x, double a_min, double a_max, double b_min, double b_max){
        return (x - a_min) / (a_max - a_min) * (b_max - b_min) + b_min;
    }

    /*
                        T R I G    E Q U A T I O N S

                                                                        */

    public enum angleMode {
        RADIANS, DEGREES
    }

    public static double cos(double value, angleMode mode){
        return (mode == RADIANS) ? Math.cos(value) : Math.cos(toRadians(value));
    }
    public static double sin(double value, angleMode mode){
        return (mode == RADIANS) ? Math.sin(value) : Math.sin(toRadians(value));
    }
    public static double tan(double value, angleMode mode){
        return (mode == RADIANS) ? Math.tan(value) : Math.tan(toRadians(value));
    }



    /*
                    C O N V E R S I O N S
                                                    */
    public static double lessThan1000TicksToCentimeters(double ticks){
        return (0.0748 * Math.pow(ticks, 2)) + (.677 * ticks) + 87.3;
    }


    public static double centimeters2Ticks(double c){
        return (0.118 * Math.pow(c, 2)) + (3.66 * c) + 7;
    }

    public static double ticks2Centimeters(double ticks){
        double rootTerm = 4 * 0.118 * (7 - ticks);
        if (rootTerm < 0) return 0;

        double numer = sqrt(Math.pow(3.66, 2) - rootTerm);
        double denom = 2 * 0.118;
        double answ1 = (-3.66 + numer) / denom;
        double answ2 = (-3.66 - numer) / denom;
        return (answ1 > 0) ? answ1 : answ2;
    }

    public static double convertInches2Ticks(double ticks){
        return (ticks - 4.38) / 0.0207; // Calculated using desmos
    }

    public static double convertTicks2Inches(double inches){
        return (0.0207 * inches) + 4.38; // Calculated using desmos
    }
}
