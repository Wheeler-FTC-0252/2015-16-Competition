package org.wheeler.robotics;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by lucien on 9/17/15.
 */
public class minibotDrive extends OpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    public void init(){
        leftMotor = hardwareMap.dcMotor.get("left");
        rightMotor = hardwareMap.dcMotor.get("right");
    }
    public void loop(){
        leftMotor.setPower(gamepad1.left_stick_y);
        rightMotor.setPower(gamepad1.right_stick_y*-1);
    }
}
