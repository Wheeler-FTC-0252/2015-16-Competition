package org.wheeler.robotics.treadBot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

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
    double driveGain = 1 / driveGainFactor;
    String previousDPad="none";

    //ARM
    DcMotor armRaiseMotor;
    double armRaiseMotorSpeed;
    double armRaiseGain = 1;
    DcMotor armRotateMotor;
    double armRotateMotorSpeed;
    double armRotateGainFactor=1.5;
    double armRotateGain = 1/(armRotateGainFactor*4);
    String previousDPad2 = "none";

    //RELEASE
    Servo releaseLeftServo;
    boolean leftBumper = false;
    boolean previousLeftBumper = false;
    Servo releaseRightServo;
    boolean rightBumper = false;
    boolean previousRightBumper = false;
    double releaseServoStart = 0;
    double releaseServoExtended = 0.85;
    double releaseServoMax = 1;

    public void init() {
        //DRIVE
        leftMotor = hardwareMap.dcMotor.get("left");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        rightMotor = hardwareMap.dcMotor.get("right");
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        //ARM
        armRaiseMotor = hardwareMap.dcMotor.get("armRaise");
        armRaiseMotor.setDirection(DcMotor.Direction.REVERSE);

        armRotateMotor = hardwareMap.dcMotor.get("armRotate");
        armRotateMotor.setDirection(DcMotor.Direction.REVERSE);
        armRotateMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        //RELEASE
        releaseLeftServo = hardwareMap.servo.get("releaseLeft");
        releaseLeftServo.setDirection(Servo.Direction.FORWARD);
        releaseLeftServo.scaleRange(releaseServoStart, releaseServoMax);
        releaseLeftServo.setPosition(releaseServoStart);

        releaseRightServo = hardwareMap.servo.get("releaseRight");
        releaseRightServo.setDirection(Servo.Direction.REVERSE);
        releaseRightServo.scaleRange(releaseServoStart, releaseServoMax);
        releaseRightServo.setPosition(releaseServoStart);
    }

    public void loop() {

        //-----------------------DRIVE------------------------------\\
        if (gamepad1.dpad_up) {
            if (!previousDPad.equalsIgnoreCase("up")) {
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

        if (driveGain > 1){
            driveGain = 1;
        }

        leftMotorSpeed=gamepad1.left_stick_y * driveGain;
        rightMotorSpeed=gamepad1.right_stick_y * driveGain;

        leftMotor.setPower(leftMotorSpeed);
        rightMotor.setPower(rightMotorSpeed);

        //------------------------------ARM-----------------------------------\\
        if (gamepad2.dpad_up) {
            if (!previousDPad2.equalsIgnoreCase("up")) {
                armRotateGain = armRotateGain * armRotateGainFactor;
                previousDPad2 = "up";
            }
        } else if (gamepad2.dpad_down) {
            if (!previousDPad2.equalsIgnoreCase("down")) {
                armRotateGain = armRotateGain / armRotateGainFactor;
                previousDPad2 = "down";
            }
        } else {
            previousDPad2 = "none";
        }

        if (armRotateGain > 1){
            armRotateGain = 1;
        }

        armRaiseMotorSpeed = gamepad2.right_stick_y * armRaiseGain;
        armRotateMotorSpeed = gamepad2.left_stick_y * armRotateGain;

        telemetry.addData("raise",armRaiseMotorSpeed);
        telemetry.addData("rotate", armRotateMotorSpeed);

        armRaiseMotor.setPower(armRaiseMotorSpeed);
        armRotateMotor.setPower(armRotateMotorSpeed);

        //------------------------------RELEASE-------------------------------\\
        leftBumper = gamepad2.left_bumper;
        telemetry.addData("leftBumper", leftBumper);
        telemetry.addData("previousLeftBumper", previousLeftBumper);
        if (leftBumper && previousLeftBumper!= leftBumper) {
            telemetry.addData("left", "is different");
            if (releaseLeftServo.getPosition() != releaseServoStart) {
                telemetry.addData("left", "to start");
                releaseLeftServo.setPosition(releaseServoStart);
            } else {
                telemetry.addData("left", "to extend");
                releaseLeftServo.setPosition(releaseServoExtended);
            }
        }
        telemetry.addData("leftPos", releaseLeftServo.getPosition());
        previousLeftBumper=leftBumper;

        rightBumper = gamepad2.right_bumper;
        if (rightBumper && previousRightBumper != rightBumper) {
            if (releaseRightServo.getPosition() != releaseServoStart){
                releaseRightServo.setPosition(releaseServoStart);
            }
            else {
                releaseRightServo.setPosition(releaseServoExtended);
            }
        }
        previousRightBumper = rightBumper;
    }
}
