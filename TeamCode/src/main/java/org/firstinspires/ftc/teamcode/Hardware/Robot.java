package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.CAMERA_OFFSET;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.DEGREE_RANGE;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.isActive;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Vision.DuckPipeline.isDuckFound;

import org.firstinspires.ftc.teamcode.Hardware.Sensors.Camera;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.IMU;

/**
 * A class for containing an FTC Mecanum robot
 */
public class Robot {

   public Intake intake;
   public Mecanum drivetrain;
   public OdoWheels odoWheels;
   public IMU gyro;
   public DuckSpinner duck;
   public Scoring scorer;
   public Camera webcam;
   public Robot(){
      initRobot();
   }

   public void initRobot() {

      /*
            I N I T   M O T O R S
       */

      //initialized Mecanum
      intake = new Intake();
      drivetrain = new Mecanum();
      duck = new DuckSpinner("duck");
      scorer = new Scoring();
      webcam = new Camera("webcam");//removed viewport ID - idk if that will actually make a difference

      gyro = new IMU( "imu");


      multTelemetry.addData("Status", "Initialized");
      multTelemetry.update();
   }







   public void cycle(int number){

   }


}