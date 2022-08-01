package org.firstinspires.ftc.teamcode.Hardware;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.hardwareMap;
import static org.firstinspires.ftc.teamcode.Utilities.PIDWeights.intakeD;
import static org.firstinspires.ftc.teamcode.Utilities.PIDWeights.intakeI;
import static org.firstinspires.ftc.teamcode.Utilities.PIDWeights.intakeP;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Utilities.PID;

public class Intake {
    String id;
    PID intakePID;
    DcMotor intakeMotor;
    double UP_POSITION = 1.0;
    double DOWN_POSITION = 0.525;


    public Intake(){
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakePID = new PID(intakeP, intakeI, intakeD);

    }

    public void runIntake(){
        intakeMotor.setPower(1.0);
    }

    public void runIntakeBackwards(){
        intakeMotor.setPower(-1.0);
    }


}
