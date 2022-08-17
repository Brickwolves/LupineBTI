package org.firstinspires.ftc.teamcode.Hardware.Sensors;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.hardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Vision.DuckPipelineLR;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera2;
import org.openftc.easyopencv.OpenCvPipeline;

public class CameraBack{
    /*
    DuckPipelineLR back_pipeline = new DuckPipelineLR();

    public CameraBack(String id){

        //creates live viewport
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        id.equals("cameraBack");

        //creates internal camera
        cameraBack = OpenCvCameraFactory.getInstance().createInternalCamera2(OpenCvInternalCamera2.CameraDirection.BACK, cameraMonitorViewId);

        WebcamName webcamNameBack = hardwareMap.get(WebcamName.class, "cameraBack"); //wtf does this even do?



        //start a streaming sesh on the back camera
        cameraBack.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                cameraBack.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                cameraBack.setPipeline(back_pipeline); //set back camera to back pipeline
            }

            @Override
            public void onError(int errorCode) {

            }
        });

    }



     */
}
