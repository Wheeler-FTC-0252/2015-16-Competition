package org.wheeler.robotics;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by lucien on 9/17/15.
 */
public class minibotDrive extends OpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    boolean oldLeftBumper=false;
    boolean leftBumper;
    boolean oldRightBumper=false;
    boolean rightBumper;
    double oldLeftJoystick=0;
    double leftJoystick;
    double oldRightJoystick=0;
    double rightJoystick;
    double armControl=0;
    DcMotor armMotor;
    boolean autoMode=false;
    boolean oldAutoMode=false;

    private void autoChange(){
        if(autoMode && autoMode!=oldAutoMode){
            leftMotor.setPower(0);
            rightMotor.setPower(0);
        }
        oldAutoMode=autoMode;
    }
    /*private void noScope360(boolean left){
        int multiplier=0;
        double spinSpeed=1;
        autoMode=true;

        if (left) {
            multiplier = -1;
        }
        else{
            multiplier=1;
        }

        leftMotor.setPower(multiplier*spinSpeed);
        telemetry.addData("leftAuto", multiplier * spinSpeed);
        rightMotor.setPower(multiplier * spinSpeed * -1);
        telemetry.addData("rightAuto",multiplier*spinSpeed*-1);
    }

    public void right360NoScope(){
        noScope360(false);
    }

    public void left360NoScope(){
        noScope360(true);
    }*/

    public void init() {
        leftMotor = hardwareMap.dcMotor.get("left");
        rightMotor = hardwareMap.dcMotor.get("right");
        armMotor = hardwareMap.dcMotor.get("arm");
    }
    public void loop(){
        telemetry.addData("autoMode",autoMode);
        leftJoystick=gamepad1.left_stick_y;
        telemetry.addData("left",leftJoystick);
        if (oldLeftJoystick!=leftJoystick){
            autoChange();
            leftMotor.setPower(leftJoystick);
            telemetry.addData("leftMotor", leftJoystick);
            oldLeftJoystick=leftJoystick;
            autoMode=false;
        }

        rightJoystick=gamepad1.right_stick_y*-1;
        telemetry.addData("right", rightJoystick);
        if (oldRightJoystick!=rightJoystick){
            autoChange();
            rightMotor.setPower(rightJoystick);
            telemetry.addData("leftMotor", rightJoystick);
            oldRightJoystick=rightJoystick;

        }

        /*leftBumper=gamepad1.left_bumper;
        rightBumper=gamepad1.right_bumper;
        if(leftBumper && leftBumper!=oldLeftBumper){
            left360NoScope();
            oldLeftBumper=leftBumper;
        }
        else if(rightBumper && rightBumper!=oldRightBumper){
            right360NoScope();
            oldRightBumper=rightBumper;
        }*/
        armControl=gamepad2.left_stick_y;
        telemetry.addData("arm", armControl);
        armMotor.setPower(armControl);
    }
}
