package org.wheeler.robotics.treadBot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by lucien on 11/20/15.
 */
public class TreadBotDrive extends OpMode {
    //DRIVE
    DcMotor leftMotor;
    double leftMotorSpeed;
    DcMotor rightMotor;
    double rightMotorSpeed;
    double driveGainFactor = 1.5;
    double driveGain = 1/ driveGainFactor;
    String previousDPad="none";

    //ARM
    DcMotor armRaiseMotor;
    double armRaiseMotorSpeed;
    double armRaiseGain = 1;
    DcMotor armRotateMotor;
    double armRotateMotorSpeed;
    double armRotateGain = 0.1;


    public void init() {
        //DRIVE
        leftMotor=hardwareMap.dcMotor.get("left");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor=hardwareMap.dcMotor.get("right");
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        //ARM
        armRaiseMotor=hardwareMap.dcMotor.get("armRaise");
        armRaiseMotor.setDirection(DcMotor.Direction.REVERSE);
        armRotateMotor=hardwareMap.dcMotor.get("armRotate");
        armRotateMotor.setDirection(DcMotor.Direction.REVERSE);
        armRotateMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    public void loop() {
        if (gamepad1.dpad_up) {
            if (!previousDPad.equalsIgnoreCase("up")){
                driveGain = driveGain * driveGainFactor;
                previousDPad = "up";
            }
        } else if (gamepad1.dpad_down) {
            if (!previousDPad.equalsIgnoreCase("down")) {
                driveGain = driveGain / driveGainFactor;
                previousDPad = "down";
            }
        } else {
            previousDPad = "none";
        }

        if (driveGain >1){
            driveGain = 1;
        }

        leftMotorSpeed=gamepad1.left_stick_y * driveGain;
        rightMotorSpeed=gamepad1.right_stick_y * driveGain;

        leftMotor.setPower(leftMotorSpeed);
        rightMotor.setPower(rightMotorSpeed);

        //------------------------------ARM-----------------------------------\\
        armRaiseMotorSpeed=gamepad2.right_stick_y * armRaiseGain;
        armRotateMotorSpeed =gamepad2.left_stick_y * armRotateGain;

        telemetry.addData("raise",armRaiseMotorSpeed);
        telemetry.addData("rotate",armRotateMotorSpeed);

        armRaiseMotor.setPower(armRaiseMotorSpeed);
        armRotateMotor.setPower(armRotateMotorSpeed);
    }
}
