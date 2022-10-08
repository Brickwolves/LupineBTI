package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.Controls.ButtonControls.ButtonState.DOWN;
import static org.firstinspires.ftc.teamcode.Controls.ButtonControls.ButtonState.TAP;
import static org.firstinspires.ftc.teamcode.Controls.ButtonControls.ButtonState.TOGGLE;
import static org.firstinspires.ftc.teamcode.Controls.ButtonControls.Input.CIRCLE;
import static org.firstinspires.ftc.teamcode.Controls.ButtonControls.Input.DPAD_DN;
import static org.firstinspires.ftc.teamcode.Controls.ButtonControls.Input.DPAD_L;
import static org.firstinspires.ftc.teamcode.Controls.ButtonControls.Input.DPAD_R;
import static org.firstinspires.ftc.teamcode.Controls.ButtonControls.Input.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Controls.ButtonControls.Input.LB1;
import static org.firstinspires.ftc.teamcode.Controls.ButtonControls.Input.RB1;
import static org.firstinspires.ftc.teamcode.Controls.ButtonControls.Input.RB2;
import static org.firstinspires.ftc.teamcode.Controls.ButtonControls.Input.SQUARE;
import static org.firstinspires.ftc.teamcode.Controls.ButtonControls.Input.TRIANGLE;
import static org.firstinspires.ftc.teamcode.Controls.JoystickControls.Input.LEFT;
import static org.firstinspires.ftc.teamcode.Controls.JoystickControls.Input.RIGHT;
import static org.firstinspires.ftc.teamcode.Controls.JoystickControls.Value.INVERT_SHIFTED_Y;
import static org.firstinspires.ftc.teamcode.Controls.JoystickControls.Value.SHIFTED_X;
import static org.firstinspires.ftc.teamcode.Controls.JoystickControls.Value.X;
import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.currentDuckPos;
import static org.firstinspires.ftc.teamcode.DashConstants.PositionsAndSpeeds.rateOfChange;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.setOpMode;
import static org.firstinspires.ftc.teamcode.Utilities.PIDWeights.derivativeWeight;
import static org.firstinspires.ftc.teamcode.Utilities.PIDWeights.integralWeight;
import static org.firstinspires.ftc.teamcode.Utilities.PIDWeights.proportionalWeight;

import org.firstinspires.ftc.teamcode.Controls.ButtonControls;
import org.firstinspires.ftc.teamcode.Controls.Controller;
import org.firstinspires.ftc.teamcode.Hardware.Robot;
import org.firstinspires.ftc.teamcode.Utilities.Loggers.Side;
import org.firstinspires.ftc.teamcode.Utilities.MathUtils;
import org.firstinspires.ftc.teamcode.Utilities.PID;

//@Disabled
@TeleOp(name="Iterative TeleOp", group="Iterative OpMode")
public class IterativeTeleOp extends OpMode {

    // Declare OpMode members.
    private final ElapsedTime runtime = new ElapsedTime();
    private PID pid;
    private double setPoint = 0;
    private boolean wasTurning;
    private boolean pid_on = false;
    private boolean pid_on_last_cycle = false;

    public enum SlidesState {HIGH, MIDDLE, LOW, SHARED, DOWN}

    public SlidesState slidesState = SlidesState.DOWN; //start in the down position

    // Declare OpMode members.
    Robot robot;
    Controller controller;
    Controller controller2;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        setOpMode(this);

        pid = new PID(proportionalWeight, integralWeight, derivativeWeight);


        robot = new Robot();
        controller = new Controller(gamepad1);
        controller2 = new Controller(gamepad2);

        multTelemetry.addData("Status", "Initialized");
        multTelemetry.addLine(":-)");
        multTelemetry.update();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {


        multTelemetry.addData("Status", "InitLoop");
        multTelemetry.addData("Duck Pos", currentDuckPos);
        multTelemetry.update();
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();

        multTelemetry.addData("Status", "Started");
        multTelemetry.update();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        Controller.update();

        double power;

        //PID and Kinetic Turning
        double rotation = controller.get(RIGHT, X); //right joystick

        // Turn off PID if we manually turn
        // Turn on PID if we're not manually turning and the robot's stops rotating
        double currentRateOfChange = robot.gyro.rateOfChange();
        if (!(rotation == 0)){
            pid_on = false;
        }
        else if (currentRateOfChange <= rateOfChange){
            pid_on = true;
        }


        //TURN WRAPPING
        if (controller.get(DPAD_R, TAP)) {
            setPoint = MathUtils.closestAngle(270, robot.gyro.getAngle());
            pid_on = true;
        } else if (controller.get(DPAD_L, TAP)) {
            setPoint = MathUtils.closestAngle(90, robot.gyro.getAngle());
            pid_on = true;
        } else if (controller.get(DPAD_UP, TAP)) {
            setPoint = MathUtils.closestAngle(0, robot.gyro.getAngle());
            pid_on = true;
        } else if (controller.get(DPAD_DN, TAP)) {
            setPoint = MathUtils.closestAngle(180, robot.gyro.getAngle());
            pid_on = true;
        }


        // Lock the heading if we JUST turned PID on
        // Correct our heading if the PID has and is still on
        if (pid_on && !pid_on_last_cycle) setPoint = robot.gyro.getAngle();
        else if (pid_on) rotation = pid.update(robot.gyro.getAngle() - setPoint, true);

        // Update whether the PID was on or not
        pid_on_last_cycle = pid_on;


        //DUCKWHEEL CODE
        if (controller2.get(CIRCLE, TOGGLE)) {
            if (Side.red) {
                robot.duck.spin();
            } else {
                robot.duck.backspin();
            }
        } else {
            robot.duck.stop();
        }

        //INTAKE CODE
        if (controller.get(RB2, ButtonControls.ButtonState.DOWN)) {
            robot.intake.runIntake();
        } else if (controller.get(RB1, ButtonControls.ButtonState.DOWN)) {
            robot.intake.runIntakeBackwards();
        } else {
            robot.intake.stopIntake(false);
        }


        //DEPOSITOR CODE
        if (controller2.get(DPAD_UP, TAP) && slidesState != SlidesState.HIGH) {
            slidesState = SlidesState.HIGH;
            robot.scorer.time.reset();
        }

        if (controller2.get(DPAD_L, TAP) && slidesState != SlidesState.MIDDLE) {
            slidesState = SlidesState.MIDDLE;
            robot.scorer.time.reset();
        }

        if (controller2.get(DPAD_R, TAP) && slidesState != SlidesState.LOW) {
            slidesState = SlidesState.LOW;
            robot.scorer.time.reset();
        }

        if (controller2.get(DPAD_DN, TAP) && slidesState != SlidesState.SHARED) {
            slidesState = SlidesState.SHARED;
            robot.scorer.time.reset();
        }

        if (controller2.get(SQUARE, TAP) && slidesState != SlidesState.DOWN) {
            slidesState = SlidesState.DOWN;
            robot.scorer.time.reset();
        }

        switch (slidesState) {
            case HIGH:
                robot.scorer.scoreHigh();
                break;
            case MIDDLE:
                robot.scorer.scoreMid();
                break;
            case LOW:
                robot.scorer.scoreLow();
                break;
            case SHARED:
                robot.scorer.scoreShared();
                break;
            case DOWN:
                robot.scorer.deposit();
                break;
        }


        //DRIVING
        controller.setJoystickShift(LEFT, robot.gyro.getAngle());

        double drive = controller.get(LEFT, INVERT_SHIFTED_Y);
        double strafe = controller.get(LEFT, SHIFTED_X);

        if(controller.get(LB1, ButtonControls.ButtonState.DOWN)){
            power = 0.3;
        }else{
            power = 0.8;
        }

        robot.drivetrain.setDrivePower(drive, strafe, rotation, power);
    /*
         ----------- L O G G I N G -----------
                                            */
        multTelemetry.addData("Intake Speed", robot.intake.getTPS());
        multTelemetry.addData("Rate Of Change", robot.gyro.rateOfChange());
        multTelemetry.update();
    }


    @Override
    public void stop(){

        /*
                    Y O U R   C O D E   H E R E
                                                   */

    }
}
