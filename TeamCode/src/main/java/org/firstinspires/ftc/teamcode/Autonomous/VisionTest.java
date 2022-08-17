package org.firstinspires.ftc.teamcode.Autonomous;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.setOpMode;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.currentDuckPos;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision;
import org.firstinspires.ftc.teamcode.Hardware.Robot;
import org.firstinspires.ftc.teamcode.Utilities.Loggers.Side;


@Autonomous(name="VisionTest", group="Autonomous Linear Opmode")
public class VisionTest extends LinearOpMode
{
    // Declare OpMode members.
    public Robot robot;
    private ElapsedTime runtime = new ElapsedTime();



    public void initialize(){
        setOpMode(this);
        robot = new Robot();

        Side.setBlue();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void runOpMode()
    {

        initialize();

        multTelemetry.addLine("Waiting for start");
        multTelemetry.addData("duck position", currentDuckPos);
        multTelemetry.update();

        waitForStart();

        if (opModeIsActive()){

            robot.orientToDuck();

        }
   }
}
