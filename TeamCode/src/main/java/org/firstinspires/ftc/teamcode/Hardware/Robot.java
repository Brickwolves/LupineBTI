package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.FRONT_CAMERA_OFFSET;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.BACK_CAMERA_OFFSET;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.DEGREE_RANGE;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.FRONT_CAMERA_OFFSET;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.isActive;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Vision.DuckPipelineLR.isDuckFound;

import org.firstinspires.ftc.teamcode.Hardware.Sensors.BackCamera;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.Cameras;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.FrontCamera;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.IMU;

/**
 * A class for containing an FTC Mecanum robot
 */
public class Robot {

   public static ElapsedTime time = new ElapsedTime();

   public Intake intake;
   public Mecanum drivetrain;
   public IMU gyro;
   public DuckSpinner duck;
   public Robot(){
      initRobot();
   }


   public void initRobot() {
      drivetrain = new Mecanum();
      duck = new DuckSpinner("duck");

      gyro = new IMU( "imu");


      multTelemetry.addData("Status", "Initialized");
      multTelemetry.update();
   }



}