package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.isActive;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;


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

   public Vision vision;


   public void initRobot() {
      drivetrain = new Mecanum();
      duck = new DuckSpinner("duck");

      gyro = new IMU( "imu");

      vision = new Vision(); //please pretty please don't cause disconnects


      multTelemetry.addData("Status", "Initialized");
      multTelemetry.update();
   }



}