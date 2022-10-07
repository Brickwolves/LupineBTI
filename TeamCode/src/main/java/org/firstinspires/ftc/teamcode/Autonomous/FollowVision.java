package org.firstinspires.ftc.teamcode.Autonomous;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.setOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.Robot;
import org.firstinspires.ftc.teamcode.Hardware.Vision;
import org.firstinspires.ftc.teamcode.Utilities.Loggers.Side;

public class FollowVision extends LinearOpMode {

    //declare opMode members
    public Robot robot;
    public Vision vision;
    private ElapsedTime runtime = new ElapsedTime();


    public void initialize() {
        setOpMode(this);
        robot = new Robot();

        Side.setBlue();
    }

    public void runOpMode() {

        initialize();

        multTelemetry.addLine("Waiting for start");
        multTelemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            vision.followDuck();
        }
    }

}
