package org.firstinspires.ftc.teamcode.Hardware.Sensors;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.hardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Vision.DuckPipelineDetect;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

public class CameraFront{
/*
    public OpenCvCamera cameraFront; //front cam
    public DuckPipelineDetect front_pipeline;


    public CameraFront(String id) {
        //creates live viewport
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        id.equals("cameraFront");

        //create internal camera
        cameraFront = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        //assigns the cameras' names to be what i entered into the config
        WebcamName webcamNameFront = hardwareMap.get(WebcamName.class, "cameraFront");

        //creates camera (w/ live preview)
        cameraFront = OpenCvCameraFactory.getInstance().createWebcam(webcamNameFront, cameraMonitorViewId);

        //start a streaming sesh on the front camera
        cameraFront.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                cameraFront.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                cameraFront.setPipeline(front_pipeline); //set front cam to front pipeline
            }

            @Override
            public void onError(int errorCode) {

            }
        });
    }

 */
}
