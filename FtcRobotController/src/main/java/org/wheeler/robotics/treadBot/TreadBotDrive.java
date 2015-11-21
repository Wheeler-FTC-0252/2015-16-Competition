package org.wheeler.robotics.treadBot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by lucien on 11/20/15.
 */
public class TreadBotDrive extends OpMode {
    DcMotor leftMotor;
    double leftMotorSpeed;
    DcMotor rightMotor;
    double rightMotorSpeed;

    public void init() {
        leftMotor=hardwareMap.dcMotor.get("left");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor=hardwareMap.dcMotor.get("right");
        leftMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    public void loop() {
        leftMotorSpeed=gamepad1.left_stick_y;
        rightMotorSpeed=gamepad1.right_stick_y;

        leftMotor.setPower(leftMotorSpeed);
        rightMotor.setPower(rightMotorSpeed);
    }
}
