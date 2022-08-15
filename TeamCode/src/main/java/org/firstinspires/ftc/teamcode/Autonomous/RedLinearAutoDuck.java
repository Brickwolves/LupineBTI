package org.firstinspires.ftc.teamcode.Autonomous;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.setOpMode;

import org.firstinspires.ftc.teamcode.Hardware.Robot;
import org.firstinspires.ftc.teamcode.Hardware.Vision;
import org.firstinspires.ftc.teamcode.Utilities.Loggers.Side;


@Autonomous(name="Duck Red", group="Autonomous Linear Opmode")
public class RedLinearAutoDuck extends LinearOpMode
{
    Robot robot;
    Vision vision;
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    public void initialize(){
        setOpMode(this);
        Side.setRed();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void runOpMode()
    {

        initialize();

        vision.cameras.back_pipeline.detectDuckBarcode();

        multTelemetry.addLine("Waiting for start");
        multTelemetry.update();

        robot = new Robot();
        vision = new Vision();


        waitForStart();

        if (opModeIsActive()){

            robot.drivetrain.strafe(50, 0.007, 0.01, 1, robot.gyro, true);
            robot.drivetrain.turn(-90, robot.gyro);
            robot.drivetrain.strafe(-320, 0.007, 0.01, 1, robot.gyro, true);
            ElapsedTime timer = new ElapsedTime();
            while(timer.seconds() < 4){
                robot.duck.spin();
            }
            robot.drivetrain.strafe(-800, 0.007, 0.01, 1, robot.gyro, false);
            robot.drivetrain.strafe(-350, 0.007, 0.01, 1, robot.gyro, true);


            /*
                    Y O U R   C O D E   H E R E
                                                   */

        }
    }
}

