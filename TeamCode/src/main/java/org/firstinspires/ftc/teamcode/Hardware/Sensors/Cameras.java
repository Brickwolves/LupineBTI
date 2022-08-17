package org.firstinspires.ftc.teamcode.Hardware.Sensors;

import static org.firstinspires.ftc.teamcode.DashConstants.Dash_Vision.isFrontCamTuning;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.hardwareMap;

import org.firstinspires.ftc.teamcode.Vision.DuckPipelineDetect;
import org.firstinspires.ftc.teamcode.Vision.DuckPipelineLR;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvInternalCamera2;
import org.openftc.easyopencv.OpenCvWebcam;


public class Cameras { //a class that makes two new cameras
    //NOTE: I think I might get rid of the singular "Camera" class and just create both cams here bc it's easier and it makes more sense
    //bc this is essentially doing what the Camera class is doing, just twice

    public Camera cameraFront; //front cam
    public Camera cameraBack; //back cam

    public DuckPipelineLR back_pipeline = new DuckPipelineLR(); //duck barcode pipeline
    public DuckPipelineDetect front_pipeline = new DuckPipelineDetect(); //duck finder pipeline

    // I got documentation from https://github.com/OpenFTC/EasyOpenCV/blob/master/doc/user_docs/camera_initialization_overview.md

    public Cameras() {

        cameraFront = new Camera("cameraFront");
        cameraBack = new Camera("cameraBack");

        cameraBack.setPipeline(back_pipeline);
        cameraFront.setPipeline(front_pipeline);

        //FIX THIS - supposed to split the camera view but it's not that important (i don't need to see it as long as the code works
       // int[] viewportContainerIds = OpenCvCameraFactory.getInstance().splitLayoutForMultipleViewports(cameraMonitorViewId, 2, OpenCvCameraFactory.ViewportSplitMethod.VERTICALLY);

    }



}
