package org.wheeler.robotics.compBot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by lucien on 10/28/15.
 */
public class drive extends OpMode {
    DcMotor[] leftMotors;
    double leftMotorSpeed;
    DcMotor[] rightMotors;
    double rightMotorSpeed;
    DcMotor armMotor;
    double armMotorSpeed;
    DcMotor liftArmMotor;
    double liftArmMotorSpeed;


    public void init(){
        leftMotors=new DcMotor[] {hardwareMap.dcMotor.get("fL"),
                hardwareMap.dcMotor.get("bL")};
        reverseMotorArray(leftMotors);
        rightMotors=new DcMotor[] {hardwareMap.dcMotor.get("fR"),
                hardwareMap.dcMotor.get("bR")};

        armMotor=hardwareMap.dcMotor.get("arm");
        liftArmMotor=hardwareMap.dcMotor.get("liftArm");
    }

    public void setMotorArray(DcMotor[] motors, double power){
        for (DcMotor ii : motors){
            ii.setPower(power);
        }
    }

    public void reverseMotorArray(DcMotor[] motors){
        for (DcMotor ii : motors){
            ii.setDirection(DcMotor.Direction.REVERSE);
        }
    }
    public void loop(){
        //Drive Motors
        leftMotorSpeed=gamepad1.left_stick_y;
        rightMotorSpeed=gamepad1.right_stick_y;
        setMotorArray(leftMotors, leftMotorSpeed);
        telemetry.addData("leftMotors: ", leftMotorSpeed);
        setMotorArray(rightMotors, rightMotorSpeed);
        telemetry.addData("rightMotors: ", rightMotorSpeed);

        //Arm Motors
        armMotorSpeed=gamepad2.left_stick_y;
        armMotor.setPower(armMotorSpeed);
        telemetry.addData("armMotor: ", armMotorSpeed);
        //Arm Lift Motors (Angle)
        liftArmMotorSpeed=gamepad2.right_stick_y;
        liftArmMotor.setPower(liftArmMotorSpeed);
        telemetry.addData("liftArmMotor: ", liftArmMotorSpeed);
    }
}
