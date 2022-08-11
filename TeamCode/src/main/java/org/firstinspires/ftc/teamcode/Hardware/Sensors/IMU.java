package org.firstinspires.ftc.teamcode.Hardware.Sensors;

import static org.firstinspires.ftc.teamcode.DashConstants.PositionsAndSpeeds.momentOfInertia;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Utilities.MathUtils;
import org.firstinspires.ftc.teamcode.Utilities.OpModeUtils;

public class IMU {
    private BNO055IMU imu;
    private Double previousAngle;
    private double deltaAngle;
    private double startAngle;
    private double offsetAngle;

    private long previousTime = System.currentTimeMillis();
    double previousChange = 0;

    public IMU(String deviceName) {

        imu = OpModeUtils.hardwareMap.get(BNO055IMU.class, deviceName);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        imu.initialize(parameters);

        startAngle = getAngle();
        previousAngle = null;
        deltaAngle = 0;
        offsetAngle = 0;

    }

    public double rateOfChange(){
        double change = getAngle();
        double deltaTime = (System.currentTimeMillis() - previousTime) / 1000.0;
        double deltaChange = change - previousChange;
        double rateOfChange = deltaChange/deltaTime;
        previousTime = System.currentTimeMillis();
        previousChange = change;
        return Math.abs(rateOfChange);
    }

    public double getUnwrappedAngle(){
        // Get the current angle
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double currentAngle = angles.firstAngle;

        return currentAngle;

    }

    public double getAngularKineticEnergy(){
        return MathUtils.pow(rateOfChange(), 2) * .5 * momentOfInertia;
    }

    public double absAngularDist(double compareAngle){
        return Math.abs(compareAngle - getAngle());
    }

    /**
     * @return the wrapped angle
     */
    public double getAngle(){

        // Get the current angle
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double currentAngle = angles.firstAngle;

        // Update how many times we have wrapped
        deltaAngle = updateWraps(previousAngle, currentAngle, deltaAngle);

        // Update the previous angle
        previousAngle = currentAngle;
        return currentAngle + deltaAngle + offsetAngle;
    }

    public double getModAngle(){
        return getAngle() % 360;
    }

    public double getAngleActual(){
        // Get the current angle
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double currentAngle = angles.firstAngle;

        // Update how many times we have wrapped
        deltaAngle = updateWraps(previousAngle, currentAngle, deltaAngle);

        // Update the previous angle
        previousAngle = currentAngle;
        return currentAngle + deltaAngle;
    }

    /**
     * @param {Double} previousAngle
     * @param {double} currentAngle
     * @param {double} deltaAngle
     * @return {double} the value to add to the currentAngle to get the currentWrappedAngle
     */
    static double updateWraps(Double previousAngle, double currentAngle, double deltaAngle){
        if (previousAngle != null){
            if (currentAngle - previousAngle >= 180){
                deltaAngle -= 360;
            }
            else if (currentAngle - previousAngle <= -180){
                deltaAngle += 360;
            }
        }
        return deltaAngle;
    }

    public double getDeltaAngle(){
        return deltaAngle;
    }

    public double getStartAngle(){
        return startAngle;
    }
    public void setOffsetAngle(double offsetAngle) { this.offsetAngle = offsetAngle; }
    public double getOffsetAngle() { return offsetAngle; }
    public void resetDeltaAngle() { deltaAngle = 0; }
}
