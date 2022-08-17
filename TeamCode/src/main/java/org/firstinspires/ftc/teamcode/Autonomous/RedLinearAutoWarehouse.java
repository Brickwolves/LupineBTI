package org.firstinspires.ftc.teamcode.Autonomous;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.DashConstants.Dash_AutoDistances.angle1;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_AutoDistances.dist1;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_AutoDistances.dist2;
import static org.firstinspires.ftc.teamcode.DashConstants.DuckPos.left;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.setOpMode;

import org.firstinspires.ftc.teamcode.Hardware.Robot;
import org.firstinspires.ftc.teamcode.Utilities.Loggers.Side;


@Autonomous(name="Warehouse Red", group="Autonomous Linear Opmode")
public class RedLinearAutoWarehouse extends LinearOpMode {
    Robot robot;
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

        multTelemetry.addLine("Waiting for start");
        multTelemetry.update();

        robot = new Robot();


        waitForStart();

        if (opModeIsActive()){

            if(left){
               robot.scorer.autoLow();
                robot.drivetrain.strafe(.7,500,0,270, robot.gyro);
                robot.drivetrain.strafe(.4,320,10,180, robot.gyro);
                robot.sleep(.5);
                robot.scorer.autoDeposit();
            }else{
                robot.scorer.autoHigh();
                robot.drivetrain.strafe(.7,500,0,270, robot.gyro);
                robot.drivetrain.strafe(.4,320,10,180, robot.gyro);
                robot.sleep(.5);
                robot.scorer.autoDeposit();
                robot.drivetrain.strafe(.5, 700, 90,0,robot.gyro);
                robot.cycle(1);






            }

        }
    }
}

