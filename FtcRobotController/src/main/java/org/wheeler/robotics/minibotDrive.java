package org.wheeler.robotics;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by lucien on 9/17/15.
 */
public class minibotDrive extends OpMode {
    DcMotor[] leftMotors;
    DcMotor[] rightMotors;
    double oldLeftJoystick=0;
    double leftJoystick;
    double oldRightJoystick=0;
    double rightJoystick;
    double joystickGain=0.5;

    public void init(){
        leftMotors= new DcMotor[] {hardwareMap.dcMotor.get("fL"),hardwareMap.dcMotor.get("bL")};
        rightMotors= new DcMotor[] {hardwareMap.dcMotor.get("fR"),hardwareMap.dcMotor.get("bR")};
    }
    public void loop(){
        leftJoystick=gamepad1.left_stick_y;
        telemetry.addData("left",leftJoystick);
        if (oldLeftJoystick!=leftJoystick){
            leftMotors[0].setPower(leftJoystick*joystickGain);
            leftMotors[1].setPower(leftJoystick*joystickGain);
            telemetry.addData("leftMotor", leftJoystick);
            oldLeftJoystick=leftJoystick;
        }

        rightJoystick=gamepad1.right_stick_y*-1;
        telemetry.addData("right", rightJoystick);
        if (oldRightJoystick!=rightJoystick){
            rightMotors[0].setPower(rightJoystick*joystickGain);
            rightMotors[1].setPower(rightJoystick*joystickGain);
            telemetry.addData("leftMotor", rightJoystick);
            oldRightJoystick=rightJoystick;
        }
    }
}
