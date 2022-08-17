package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.DashConstants.PositionsAndSpeeds.lipPos;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.hardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.Sensors.Color_Sensor;
import org.firstinspires.ftc.teamcode.Utilities.Freight;

public class Scoring {
    public DcMotor spool;
    public Servo bucket;
    public Servo lip;
    public Color_Sensor bucketColor;
    public ElapsedTime time = new ElapsedTime();


    public Scoring(){
        spool = hardwareMap.get(DcMotor.class, "spool");
        bucket = hardwareMap.get(Servo.class, "bucket");
        lip = hardwareMap.get(Servo.class, "lip");
        bucketColor = new Color_Sensor();

        bucketColor.init("bucketColor");
        spool.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spool.setTargetPosition(0);
        spool.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        spool.setPower(1);

        lip.setPosition(lipPos);
        bucket.setPosition(1);

    }



    public void scoreHigh(){

        if (time.seconds() < .5) {
            lip.setPosition(0.28);
        }
        if (time.seconds() < 1 && time.seconds() > 0.5) {
            bucket.setPosition(.9);
            spool.setTargetPosition(1800);
        }
        if(time.seconds()>1 && time.seconds()<2)
        bucket.setPosition(0.15);


    }

    public void scoreMid(){

        if (time.seconds() < .5) {
            lip.setPosition(0.28);
        }
        if (time.seconds() < 1 && time.seconds() > 0.5) {
            bucket.setPosition(.9);
            spool.setTargetPosition(800);
        }
        if(time.seconds()>1 && time.seconds()<2)
            bucket.setPosition(0.15);


    }

    public void scoreLow(){

        if (time.seconds() < .5) {
            lip.setPosition(0.28);
        }
        if (time.seconds() < 1 && time.seconds() > 0.5) {
            bucket.setPosition(.9);
            spool.setTargetPosition(200);
        }
        if(time.seconds()>1 && time.seconds()<2)
            bucket.setPosition(0.15);


    }

    public void scoreShared(){

        if (time.seconds() < .5) {
            lip.setPosition(0.28);
        }
        if (time.seconds() < 1 && time.seconds() > 0.5) {
            bucket.setPosition(.9);
            spool.setTargetPosition(100);
        }
        if(time.seconds()>1 && time.seconds()<2)
            bucket.setPosition(0.15);


    }

    public void deposit(){
        if(time.seconds() < .6) {
            lip.setPosition(0);
        }
        if(time.seconds() > .6 && time.seconds() < 1){
            bucket.setPosition(.9);
            spool.setTargetPosition(0);
        }
        if(time.seconds() > 1 && time.seconds() < 2){
            bucket.setPosition(1);
            lip.setPosition(lipPos);
        }
    }

    public void autoHigh(){
        lip.setPosition(0.28);
        bucket.setPosition(.9);
        spool.setTargetPosition(1800);
        while(spool.getCurrentPosition() < 1700){

        }
        bucket.setPosition(0.15);
    }
    public void autoLow(){
        lip.setPosition(0.28);
        bucket.setPosition(.9);
        spool.setTargetPosition(200);
        while (spool.getCurrentPosition() < 200){}
        bucket.setPosition(0.15);

    }

    public void autoDeposit(){
        lip.setPosition(0);
        time.reset();
        while(time.seconds() < .8){

        }
        bucket.setPosition(0.9);
        spool.setTargetPosition(0);
        while(spool.getCurrentPosition() > 50){
        }
        bucket.setPosition(1);
        lip.setPosition(lipPos);
    }

    public boolean isLoaded(){
        return ((bucketColor.updateRed() > 50) && bucketColor.updateGreen() > 50 && bucketColor.updateBlue() > 20) && ((bucketColor.getRedCacheValue() > 103 && bucketColor.getRedCacheValue() < 813) && (bucketColor.getGreenCacheValue() > 75 && bucketColor.getGreenCacheValue() < 954) && (bucketColor.getBlueCacheValue() > 46 && bucketColor.getBlueCacheValue() < 866));
    }

}
