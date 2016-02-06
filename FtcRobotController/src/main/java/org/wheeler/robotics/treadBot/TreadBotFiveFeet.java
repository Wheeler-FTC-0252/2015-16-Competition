package org.wheeler.robotics.treadBot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by lucien on 1/24/16.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class TreadBotFiveFeet extends OpMode {
	double ENCODER_MULTIPLIER = 10000.0/90.0;  // 10000 ticks = 90 cm  --> encoder ticks = cm * (10000/90)
	double INITIAL_MOTOR_SPEED = 1;
	double INITIAL_DISTANCE = 140 * ENCODER_MULTIPLIER;
	double SLOW_DISTANCE = 40;

	DcMotor leftMotor;
	DcMotor rightMotor;

	public void init() {
		leftMotor = hardwareMap.dcMotor.get("left");
		leftMotor.setDirection(DcMotor.Direction.FORWARD);
		leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

		rightMotor = hardwareMap.dcMotor.get("right");
		rightMotor.setDirection(DcMotor.Direction.REVERSE);
		rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
	}

	public void start() {
		leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
	}

	public void loop() {
		int leftPos = leftMotor.getCurrentPosition();
		int rightPos = rightMotor.getCurrentPosition();

		telemetry.addData("Distance Left", leftPos);
		telemetry.addData("Distance Right", rightPos);

		if (leftPos < INITIAL_DISTANCE || rightPos < INITIAL_DISTANCE){
			leftMotor.setPower(INITIAL_MOTOR_SPEED);
			rightMotor.setPower(INITIAL_MOTOR_SPEED);
			telemetry.addData("Status",  "Still driving");
		}
		else {
			telemetry.addData("Status", "Got there");
			leftMotor.setPower(0);
			rightMotor.setPower(0);
		}
	}
}
