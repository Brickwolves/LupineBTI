package org.firstinspires.ftc.teamcode.Hardware;

import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.CAMERA_OFFSET;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.DEGREE_RANGE;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.isActive;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Vision.DuckPipeline.isDuckFound;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.Sensors.Camera;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.IMU;

public class Vision {

    public Robot robot = new Robot();
    public Camera webcam;
    public Mecanum drivetrain;
    public IMU gyro;
    public Intake intake;

    public void initRobot() {

      /*
            I N I T   M O T O R S
       */

        //initialized Mecanum
        intake = new Intake();
        drivetrain = new Mecanum();
        webcam = new Camera("webcam");//removed viewport ID - idk if that will actually make a difference

        gyro = new IMU( "imu");


        multTelemetry.addData("Status", "Initialized");
        multTelemetry.update();
    }


    /**
     * a method that will search for, orient to, and lock on to a duck
     */
    public void blindOrientToDuck(){
        ElapsedTime timer = new ElapsedTime();
        timer.reset(); //timer is 0/robot's current angle BEFORE the loop begins
        double theta = gyro.getAngle(); //robot's current angle BEFORE the loop begins
        boolean reachedAng1 = false;
        boolean reachedAng2 = false;
        double angle1 = theta + DEGREE_RANGE; // can modify thru dashboard
        double angle2 = theta - DEGREE_RANGE;
        double degreeMOE = 3;
        double timeout = 3;

        while (isActive()){
            if (isDuckFound){ //if the camera sees the duck
                //timeout = 3;
                //setPoint = target angle
                double setPoint = CAMERA_OFFSET + webcam.duckPipeline.degreeError2Duck() + gyro.getAngle();
                //calculates the difference between robot's current angle and target angle
                //finds how much farther robot has to turn
                double correction = drivetrain.rotationalPID.update(gyro.getAngle() - setPoint, true);
                // turns the robot so that degreeError2Duck = 0 (robot is exactly on target)
                drivetrain.setDrivePower(0, 0, correction, 0.8);

                multTelemetry.addData("SetPoint", setPoint);
                multTelemetry.addData("Correction", correction);
                multTelemetry.addData("isDuckFound", isDuckFound);

            } else { //if the camera does not see the duck
                double currentAngle = gyro.getAngle(); //robot's current angle (constantly resetting after each setDrivePower is supplied)
                if (!reachedAng1){ // if the robot has not reached angle 1 yet
                    drivetrain.setDrivePower(0, 0, 0.3, -1);
                    timeout = 5;
                }

                if (currentAngle < (angle1 + degreeMOE) && currentAngle > (angle1 - degreeMOE)){ //if the robot has reached (around) angle 1 and still no duck
                    reachedAng1 = true;
                    timeout = 9;
                }

                if (reachedAng1 && !reachedAng2){ //if the robot has reached angle 1 but not angle 2 and still no duck
                    drivetrain.setDrivePower(0, 0, 0.3, 1); //spin opposite direction
                }

                if (currentAngle > (angle2 - degreeMOE) && currentAngle < (angle2 + degreeMOE)){ // if the robot has reached (around) angle 2 and still no duck
                    reachedAng2 = true;
                }

                if (reachedAng1 && reachedAng2){ // if the robot has reached both angle 1 and angle 2 and still no duck
                    drivetrain.setDrivePower(0, 0, 0.3, 1); //start spinning in a circle
                }
            }
        }
        drivetrain.setDrivePower(0, 0, 0, 0); //stop moving, we didn't find the duckie :(
    }




    public void followDuck(){

    }






    //essentially an extension of the orient to duck method(s)
    //a method that prompts the robot to drive to and intake the duck
    public void intakeDuck(){
        double distance2Duck2 = webcam.duckPipeline.getDistanceToDuck2();
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (isDuckFound && timer.seconds() < 3){
            intake.runIntake();
            drivetrain.strafe(distance2Duck2 * 41.5, 0.1, 0.1, 0.5, gyro, true); //drives to duck
        }
    }
}
