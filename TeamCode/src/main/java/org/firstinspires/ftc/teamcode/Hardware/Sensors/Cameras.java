package org.firstinspires.ftc.teamcode.Hardware.Sensors;

import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.isFrontCamTuning;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.hardwareMap;

import org.firstinspires.ftc.teamcode.Vision.DuckPipelineDetect;
import org.firstinspires.ftc.teamcode.Vision.DuckPipelineLR;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCamera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvInternalCamera2;


public class Cameras { //a class that makes two new cameras
    //NOTE: I think I might get rid of the singular "Camera" class and just create both cams here bc it's easier and it makes more sense
    //bc this is essentially doing what the Camera class is doing, just twice

    public OpenCvCamera cameraFront;
    public OpenCvCamera cameraBack;

    public DuckPipelineLR back_pipeline = new DuckPipelineLR();
    public DuckPipelineDetect front_pipeline = new DuckPipelineDetect();


    // got documentation from https://github.com/OpenFTC/EasyOpenCV/blob/master/doc/user_docs/camera_initialization_overview.md

    public Cameras(){

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        //creates live viewport

        int[] viewportIds = OpenCvCameraFactory.getInstance().splitLayoutForMultipleViewports(cameraMonitorViewId, 2, OpenCvCameraFactory.ViewportSplitMethod.VERTICALLY);

        WebcamName webcamName = hardwareMap.get(WebcamName.class, "NAME_OF_CAMERA_IN_CONFIG_FILE");

        //creates internal cameras
        cameraFront = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        cameraBack = OpenCvCameraFactory.getInstance().createInternalCamera2(OpenCvInternalCamera2.CameraDirection.BACK,cameraMonitorViewId);

        //creates cameras (w/ live preview)
        cameraFront = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        cameraBack = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        //start a streaming sesh on the front camera
        cameraFront.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                cameraFront.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        //start a streaming sesh on the back camera
        cameraBack.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                cameraBack.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        //sets each pipeline to its respective camera
        cameraFront.setPipeline(front_pipeline);
        cameraBack.setPipeline(back_pipeline);

    }

    public void tuneCameras(){
        if (isFrontCamTuning){ //if front camera is the camera being tuned in dashboard, then front cam will start streaming
            cameraFront.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
        } else { //else the back cam will start streaming
            cameraBack.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
        }
    }
}
