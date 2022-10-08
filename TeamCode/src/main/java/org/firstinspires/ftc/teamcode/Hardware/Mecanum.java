package org.firstinspires.ftc.teamcode.Hardware;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static org.firstinspires.ftc.teamcode.Utilities.MathUtils.angleMode.DEGREES;
import static org.firstinspires.ftc.teamcode.Utilities.MathUtils.cos;
import static org.firstinspires.ftc.teamcode.Utilities.MathUtils.sin;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.hardwareMap;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.isActive;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.telemetry;
import static org.firstinspires.ftc.teamcode.Utilities.PIDWeights.derivativeWeight;
import static org.firstinspires.ftc.teamcode.Utilities.PIDWeights.integralWeight;
import static org.firstinspires.ftc.teamcode.Utilities.PIDWeights.proportionalWeight;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.wolfpackmachina.bettersensors.Sensors.Gyro;

import org.firstinspires.ftc.teamcode.Hardware.Sensors.IMU;
import org.firstinspires.ftc.teamcode.Utilities.MathUtils;
import org.firstinspires.ftc.teamcode.Utilities.PID;
import org.opencv.core.Point;

public class Mecanum {
    /**
     * Initializes the robot's necessary subsystems and motors
     */
    //constructor
    public Mecanum(){
        initMecanum();
    }

    private ElapsedTime runtime = new ElapsedTime();
    DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;
    PID rotationalPID;

    public void initMecanum(){

        fl = hardwareMap.get(DcMotor.class, "fl");
        fl.setDirection(DcMotorSimple.Direction.REVERSE);

        fr = hardwareMap.get(DcMotor.class, "fr");
        fr.setDirection(DcMotorSimple.Direction.FORWARD);

        bl = hardwareMap.get(DcMotor.class, "bl");
        bl.setDirection(DcMotorSimple.Direction.REVERSE);

        br = hardwareMap.get(DcMotor.class, "br");
        br.setDirection(DcMotorSimple.Direction.FORWARD);

        rotationalPID = new PID(proportionalWeight, integralWeight , derivativeWeight);

        multTelemetry.addData("Status", "Initialized");
        multTelemetry.update();


    }


    /**
     * Reset the DcMotors using the setMode() method and the STOP_AND_RESET_ENCODERS method
     */
    public void resetMotors(){

        // Example of reseting a motor encoder
        br.setMode(STOP_AND_RESET_ENCODER);
        bl.setMode(STOP_AND_RESET_ENCODER);
        fr.setMode(STOP_AND_RESET_ENCODER);
        fl.setMode(STOP_AND_RESET_ENCODER);

        fr.setMode(RUN_WITHOUT_ENCODER);
        fl.setMode(RUN_WITHOUT_ENCODER);
        br.setMode(RUN_WITHOUT_ENCODER);
        bl.setMode(RUN_WITHOUT_ENCODER);

        // You need to complete these for each of the fr, fl, br, bl motors
    }

    /**
     *
     * @return average of all motor's getCurrentPosition() but each one is absolute valued
     */
    public double getPosition(){
        return (Math.abs(fr.getCurrentPosition()) + Math.abs(fl.getCurrentPosition()) + Math.abs(br.getCurrentPosition()) + Math.abs(bl.getCurrentPosition()))/4.0;
    }


    /**
     * Sets the same power to all four motors
     * @param power
     */
    public void setAllPower(double power){
        /*

                Y O U R   C O D E   H E R E

         */
        fl.setPower(power);
        fr.setPower(power);
        bl.setPower(power);
        br.setPower(power);
    }

    /**
     * Sets the power to the motors for driving with a mecanum drivetrain
     * @param power
     */
    public void setDrivePower(double drive, double strafe, double turn, double power){

        double frPower = (drive - strafe - turn) * power;
        double flPower = (drive + strafe + turn) * power;
        double brPower = (drive + strafe - turn) * power;
        double blPower = (drive - strafe + turn) * power;

        fl.setPower(flPower);
        fr.setPower(frPower);
        bl.setPower(blPower);
        br.setPower(brPower);
    }


    /**
     * Translates the robot autonomously a certain distance known as ticks
     * @param ticks
     */
    public void strafe(double ticks, double acceleration, double deceleration, double maxSpeed, IMU currentAngle, boolean forwards){
        resetMotors();
        double power = 0.0;
        double position = 0.0;
        resetMotors();
        double startingAngle = currentAngle.getAngle();
        while(Math.abs(position) <= Math.abs(ticks) && isActive()){
            position = getPosition();
            double distanceFromEnd = Math.abs(ticks) - Math.abs(position);
            double accel = Math.sqrt(Math.abs(position * acceleration)) + 0.1;
            double decel = Math.sqrt(distanceFromEnd * deceleration) + 0.1;
            if (position > ticks){
                power = Math.max(Math.max(accel*-1, maxSpeed*-1), decel*-1);
            }
            else {
                power = Math.min(Math.min(accel, maxSpeed), decel);
            }
            if (position == ticks){
                setAllPower(0.0);
            }
            if(forwards) {
                setDrivePower(power, 0.0, -rotationalPID.update(startingAngle - currentAngle.getAngle(), false), 1.0);
            }
            else{
                setDrivePower(0.0, power, -rotationalPID.update(startingAngle - currentAngle.getAngle(), false), 0.0); //changed power from 1 to 0 here
            }
            multTelemetry.addData("position", position);
            multTelemetry.addData("distance", ticks);
            multTelemetry.addData("speed", power);
            multTelemetry.addData("flmotorpos", fl.getCurrentPosition());
            multTelemetry.addData("frmotorpos", fr.getCurrentPosition());
            multTelemetry.addData("blmotorpos", bl.getCurrentPosition());
            multTelemetry.addData("brmotorpos", br.getCurrentPosition());
            multTelemetry.update();
        }
        setAllPower(0.0);
    }

    public void basicStrafe(double ticks){
        resetMotors();
        double current_distance = 0.0;
        double power = 0.5;

        if (ticks < 0){ //if ticks are negative
            power = -0.5;
        }

        while (Math.abs(current_distance) < Math.abs(ticks) && isActive()){
            current_distance = getPosition();
            setAllPower(power);

            multTelemetry.addData("current distance", current_distance);
            multTelemetry.update();
        }
        setAllPower(0.0); //stop moving
    }


    /**
     * Rotates the robot autonomously a certain number of degrees with a margin of error
     * @param degrees
     * @param currentAngle
     */
    public void turn(double degrees, IMU currentAngle){ //I think this is ok
        ElapsedTime timer = new ElapsedTime();
        double targetAngle = MathUtils.closestAngle(degrees, currentAngle.getAngle());

        while(timer.seconds() < 1.5){
            setDrivePower(0.0, 0.0, -rotationalPID.update(targetAngle - currentAngle.getAngle(), true), 1.0);
            multTelemetry.addData("target Angle", targetAngle);
            multTelemetry.addData("Angle", currentAngle.getAngle());
            multTelemetry.update();
        }
        setAllPower(0.0);
    }


    /**
     * A mathematical function that optimizes the ramping of power to the motors during autonomous
     * strafes.
     * @param position
     * @param distance
     * @param acceleration
     * @return the coefficient [0, 1] of our power
     */
    public static double powerRamp(double position, double distance, double acceleration, double deceleration, double maxSpeed){

        /*

                Y O U R   C O D E   H E R E

         */
        return 0;
    }
}