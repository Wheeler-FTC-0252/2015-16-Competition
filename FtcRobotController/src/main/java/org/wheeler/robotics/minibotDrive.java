package org.wheeler.robotics;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by lucien on 9/17/15.
 */
public class minibotDrive extends OpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    double oldLeftJoystick=0;
    double leftJoystick;
    double oldRightJoystick=0;
    double rightJoystick;
    double armControl=0;
    DcMotor armMotor;

    public void init() {
        leftMotor = hardwareMap.dcMotor.get("left");

        rightMotor = hardwareMap.dcMotor.get("right");
        armMotor = hardwareMap.dcMotor.get("arm");
    }
    public void loop(){
        leftJoystick=gamepad1.left_stick_y;
        telemetry.addData("left",leftJoystick);
        if (oldLeftJoystick!=leftJoystick){
            leftMotor.setPower(leftJoystick);
            telemetry.addData("leftMotor", leftJoystick);
            oldLeftJoystick=leftJoystick;
        }

        rightJoystick=gamepad1.right_stick_y*-1;
        telemetry.addData("right", rightJoystick);
        if (oldRightJoystick!=rightJoystick){
            rightMotor.setPower(rightJoystick);
            telemetry.addData("leftMotor", rightJoystick);
            oldRightJoystick=rightJoystick;

        }

        armControl=gamepad2.left_stick_y;
        telemetry.addData("arm", armControl);
        armMotor.setPower(armControl);
    }
}
