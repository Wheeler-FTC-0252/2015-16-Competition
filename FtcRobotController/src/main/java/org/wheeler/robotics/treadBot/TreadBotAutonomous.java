package org.wheeler.robotics.treadBot;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.wheeler.robotics.library.dcmotor.DcMotorSync;
import org.wheeler.robotics.library.ultrasoundSensor.MaxSonarSensor;

/**
 * Created by lucien on 1/16/16.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class TreadBotAutonomous extends OpMode {
    //------------------------------CONSTANTS
    //  INITAL DRIVE
    private double INITIAL_SPEED = 0.25;
    private int INITAL_DISTANCE = 100;


    //  DS DRIVE
    double OPTIMAL_RATIO = 214./325.; // 214/325 (L / R)
    int MAX_DISTANCE = 100;
    double MOTOR_GAIN = 3.5;

    //  COLOR SENSOR
    int LOWER_BOUND = 100;

    //  ROTATE DRIVE
    int ROTATE_MAX_DISTANCE = 100;
    double ROTATE_SPEED = 0.25;

    //  SHORT DS
    double OPTIMAL_DISTANCE=10.0;
    double DISTANCE_PRECISION =10.0;

    //------------------------------OTHER
    //DRIVE
    DcMotor leftDrive;
    DcMotor rightDrive;
    DcMotorSync leftRightSync;

    //DISTANCE SENSORS
    MaxSonarSensor dsLeft;
    MaxSonarSensor dsRight;

    //COLOR SENSORS
    ColorSensor frontColor;
    ColorSensor middleColor;

    //  SHORT DISTANCE SENSORS
    UltrasonicSensor dsFront;



    public void init() {
        //DRIVE
        leftDrive = hardwareMap.dcMotor.get("leftDrive");
        leftDrive.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        leftDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        rightDrive = hardwareMap.dcMotor.get("rightDrive");
        rightDrive.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        leftRightSync = new DcMotorSync(leftDrive, rightDrive);

        //DISTANCE SENSORS
        dsLeft = new MaxSonarSensor(hardwareMap, "dsLeft", 0xE0);
        dsRight = new MaxSonarSensor(hardwareMap, "dsRight", 0xE6);

        //COLOR SENSORS
        frontColor = hardwareMap.colorSensor.get("frontColor");
        middleColor = hardwareMap.colorSensor.get("middleColor");

        //  SHORT DISTANCE SENSORS
        dsFront = hardwareMap.ultrasonicSensor.get("dsFront");

        Log.d("TBAutoStatus", "Initialized variables");
    }




    public void start() {
        //------------------------------INITIAL DRIVE (not guided)
        initialDrive();
        leftRightSync.close();

        //  DEBUG WAIT
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        Log.d("TBAutoStatus", "Starting wait");
        try {
            wait(1000); //For testing purposes
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //------------------------------LONG RANGE ULTRASOUND ENABLE
        feedbackDrive();

        Log.d("TBAutoStatus", "leftRightSync.close()");

        //------------------------------ROTATE TO FACE WALL
        Log.d("TBAutoStatus", "Resetting motor encoders");
        leftDrive.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        leftDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightDrive.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        Log.d("TBAutoStatus", "Syncing motors (reverse)");
        leftRightSync = new DcMotorSync(leftDrive,rightDrive, true);

        rotateToWall();

        //------------------------------CHECK FRONT DISTANCE
        frontDistanceCheck();

        //------------------------------DUMP CLIMBERS
    }

    private void initialDrive(){
        Log.d("TBAutoStatus", "Started INITIAL DRIVE");
        leftDrive.setTargetPosition(INITAL_DISTANCE);
        leftDrive.setPower(INITIAL_SPEED);
        while (leftDrive.getMode() == DcMotorController.RunMode.RUN_TO_POSITION) {
            Log.d("TBAutoStatus", "leftDrive.getMode() == DcMotorController.RunMode.RUN_TO_POSITION");
        }
        Log.d("TBAutoStatus", "leftDrive.getMode() == " + String.valueOf(leftDrive.getMode()));
    }

    private void feedbackDrive(){
        Log.d("TBAutoStatus", "Starting Long Range US navigating");
        while ((middleColor.red()<LOWER_BOUND
                && middleColor.green()<LOWER_BOUND
                && middleColor.blue()<LOWER_BOUND) && leftDrive.getCurrentPosition()<MAX_DISTANCE)
        {
            Log.d("TBAutoStatus", "middleColor.red() == " + String.valueOf(middleColor.red()));
            Log.d("TBAutoStatus", "middleColor.green() == " + String.valueOf(middleColor.green()));
            Log.d("TBAutoStatus", "middleColor.blue() == " + String.valueOf(middleColor.blue()));
            int leftDistance = dsLeft.getDistance();
            int rightDistance = dsRight.getDistance();

            double dsRatio = (double) leftDistance / (double) rightDistance;
            double currentRatio = OPTIMAL_RATIO / dsRatio;
            double leftSpeed = currentRatio*MOTOR_GAIN;
            double rightSpeed = 1/leftSpeed;

            telemetry.addData("leftDistance", leftDistance);
            Log.d("TBAutoStatus", "leftDistance == " + String.valueOf(leftDistance));

            telemetry.addData("rightDistance", rightDistance);
            Log.d("TBAutoStatus", "rightDistance == " + String.valueOf(rightDistance));

            telemetry.addData("ratio", dsRatio);
            Log.d("TBAutoStatus", "dsRatio == " + String.valueOf(dsRatio));

            telemetry.addData("optimalRatio / ratio", OPTIMAL_RATIO / dsRatio);
            Log.d("TBAutoStatus", "optimalRatio / dsRatio == " + String.valueOf(currentRatio));

            telemetry.addData("leftSpeed", leftSpeed);
            Log.d("TBAutoStatus", "leftSpeed == " + String.valueOf(leftSpeed));

            telemetry.addData("rightSpeed", rightSpeed);
            Log.d("TBAutoStatus", "rightSpeed == " + String.valueOf(rightSpeed));
        }
        Log.d("TBAutoStatus", "Middle sensor on the LINE!");
    }

    private void rotateToWall() {
        Log.d("TBAutoStatus", "Facing wall");
        leftDrive.setPower(ROTATE_SPEED);
        while ((middleColor.red()<LOWER_BOUND
                && middleColor.green()<LOWER_BOUND
                && middleColor.blue()<LOWER_BOUND) && leftDrive.getCurrentPosition()<ROTATE_MAX_DISTANCE)
        {
            Log.d("TBAutoStatus", "frontColor.red() == " + String.valueOf(frontColor.red()));
            Log.d("TBAutoStatus", "frontColor.green() == " + String.valueOf(frontColor.green()));
            Log.d("TBAutoStatus", "frontColor.blue() == " + String.valueOf(frontColor.blue()));
        }
        Log.d("TBAutoStatus", "Front sensor is aligned!");
    }

    private void frontDistanceCheck(){
        double distance = dsFront.getUltrasonicLevel();
        while (Math.abs(distance-OPTIMAL_DISTANCE)> DISTANCE_PRECISION) {
            // DRIVE TO FIX
        }
    }

    public void loop() {}

    public void stop(){
        //DRIVE
        leftRightSync.close();

        //LONG RANGE US
        dsLeft.close();
        dsRight.close();
    }
}
